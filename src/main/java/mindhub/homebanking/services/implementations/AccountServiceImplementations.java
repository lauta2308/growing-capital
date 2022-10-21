package mindhub.homebanking.services.implementations;

import mindhub.homebanking.Dtos.AccountDto;
import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.AccountRepository;
import mindhub.homebanking.repositories.CardRepository;
import mindhub.homebanking.repositories.ClientRepository;
import mindhub.homebanking.repositories.TransactionRepository;
import mindhub.homebanking.services.AccountService;
import mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplementations implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Account> getAccounts() {
        return  accountRepository.findAll();
    }

    @Override
    public AccountDto getAccount(Long id) {
        return new AccountDto(accountRepository.findById(id).get());
    }

    @Override
    public Set<Account> getClientAccounts(Authentication authentication) {

        return  clientRepository.findByEmail(authentication.getName()).getAccount();
    }

    @Override
    public AccountDto getClientAccount(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDto(account)).orElse(null);
    }

    @Override
    public ResponseEntity<Object> createAccount(AccountType accountType, CardColor cardColor, Authentication authentication) {

        if(accountType == null){
            return new ResponseEntity<>("Debes seleccionar un tipo de cuenta", HttpStatus.FORBIDDEN);
        }

        if(accountType != AccountType.CORRIENTE && accountType != AccountType.AHORRO){
            return new ResponseEntity<>("Selecciona un tipo de cuenta correcto", HttpStatus.FORBIDDEN);
        }

        if(cardColor == null){
            return new ResponseEntity<>("Debes seleccionar un color de tarjeta", HttpStatus.FORBIDDEN);
        }

        if(cardColor != CardColor.SILVER && cardColor != CardColor.GOLD && cardColor != CardColor.TITANIUM){
            return new ResponseEntity<>("El color de tarjeta elegido no es correcto", HttpStatus.FORBIDDEN);
        }
        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());

        if(clienteAutenticado == null){
            return new ResponseEntity<>("Cliente no encontrado en la base de datos", HttpStatus.FORBIDDEN);
        }

        Set<Account> clientAccounts = clienteAutenticado.getAccount().stream().filter(account -> account.getStatus() == AccountStatus.ACTIVE).collect(Collectors.toSet());

        if(clientAccounts.size() >= 3){
            return new ResponseEntity<>("No puedes tener más de 3 cuentas", HttpStatus.FORBIDDEN);
        }


        long clientDebitCards = clienteAutenticado.getCards().stream().filter(card -> card.getType() == CardType.DEBITO && card.getStatus() == CardStatus.ACTIVE).count();

        if(clientDebitCards >= 3){
            return new ResponseEntity<>("Alcanzaste el límite máximo de tarjetas de débito", HttpStatus.FORBIDDEN);
        }

        long hasSameDebitColor = clienteAutenticado.getCards().stream().filter(card -> card.getType() == CardType.DEBITO && card.getColor() == cardColor && card.getStatus() == CardStatus.ACTIVE).count();

        if(hasSameDebitColor > 0){
            return new ResponseEntity<>("Ya tienes una tarjeta de débito de ese color, elige otra", HttpStatus.FORBIDDEN);
        }



        String cardNumber = "";
        long cardCvv = CardUtils.getCvv();

        do{
            cardNumber = CardUtils.getCardNumber();
        } while(cardRepository.findByNumber(cardNumber) != null);


        Card nuevaTarjeta = new Card(CardType.DEBITO , cardColor, cardNumber, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5), clienteAutenticado, clienteAutenticado.getname() + clienteAutenticado.getLastName(), CardStatus.ACTIVE);

        cardRepository.save(nuevaTarjeta);


        String numeroAleatorio = "";
        Account verificarNumero;

        do{
            numeroAleatorio = "VIN-" + Math.round(10000000 + Math.random() * 90000000);
            verificarNumero = accountRepository.findByNumber(numeroAleatorio);

        }while(
                verificarNumero != null

        );


        Account cuentaSinRepetir = new Account(numeroAleatorio, accountType, LocalDateTime.now(), 0,clienteAutenticado, nuevaTarjeta, AccountStatus.ACTIVE);

        accountRepository.save(cuentaSinRepetir);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> changeAccountStatus(AccountDto accountDto, Authentication authentication) {
        if(accountDto.getBalance() > 0){
            return new ResponseEntity<>("No se puede eliminar una cuenta con balance mayor a  0", HttpStatus.FORBIDDEN);
        }


        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());


        Account findAccount = accountRepository.findById(accountDto.getId());


        Boolean validCardOwner = findAccount.getClient().getEmail().equals(clienteAutenticado.getEmail());


        if(clienteAutenticado == null){
            return new ResponseEntity<>("El cliente autenticado no se encuentra en la base de datos", HttpStatus.FORBIDDEN);
        } else if (findAccount == null) {
            return new ResponseEntity<>("La cuenta no se encuentra en la base de datos", HttpStatus.FORBIDDEN);

        } else if (!validCardOwner) {
            return new ResponseEntity<>("La cuenta no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);

        }

        else {
            findAccount.setStatus(AccountStatus.PENDINGDELETE);
            findAccount.getCard().setStatus(CardStatus.INACTIVE);
            accountRepository.save(findAccount);
            cardRepository.save(findAccount.getCard());
            return new ResponseEntity<>("La cuenta se ha eliminado con éxito", HttpStatus.CREATED);
        }

    }

    @Override
    public ResponseEntity<Object> deleteAccount(AccountDto accountDto, Authentication authentication) {
        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());

        Account findAccount = accountRepository.findById(accountDto.getId());

        Card accountCard = findAccount.getCard();

        Boolean isAdministrator = clienteAutenticado.getEmail().contains("admin");

        List<Transaction> findAccountTransactions = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccount().equals(findAccount)).collect(Collectors.toList());


        if(clienteAutenticado == null){
            return new ResponseEntity<>("El cliente autenticado no se encuentra en la base de datos", HttpStatus.FORBIDDEN);
        } else if (findAccount == null) {
            return new ResponseEntity<>("La cuenta no se encuentra en la base de datos", HttpStatus.FORBIDDEN);

        } else if (!isAdministrator) {
            return new ResponseEntity<>("Sólo los administradores pueden eliminar cuentas", HttpStatus.FORBIDDEN);

        }  else {
            accountRepository.deleteById(findAccount.getId());
            transactionRepository.deleteAll(findAccountTransactions);
            cardRepository.deleteById(accountCard.getId());
            return new ResponseEntity<>("La cuenta se ha eliminado con éxito", HttpStatus.CREATED);
        }

    }




}
