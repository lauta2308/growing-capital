package mindhub.homebanking.controllers;

import com.sun.istack.NotNull;
import mindhub.homebanking.Dtos.AccountDto;
import mindhub.homebanking.Dtos.CardDto;
import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.AccountRepository;
import mindhub.homebanking.repositories.CardRepository;
import mindhub.homebanking.repositories.ClientRepository;
import mindhub.homebanking.repositories.TransactionRepository;
import mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static mindhub.homebanking.models.CardType.DEBITO;


@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/api/accounts")
    public List<AccountDto> getAccounts(Authentication authentication) {


            return accountRepository.findAll().stream().map(account -> new AccountDto(account)).collect(Collectors.toList());

        }


    @GetMapping("/api/accounts/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(account -> new AccountDto(account)).orElse(null);
    }

    @GetMapping("/api/clients/current/accounts")
    public List<AccountDto> getClientAccounts(Authentication authentication) {


        List <Account> clientAccounts =  accountRepository.findAll().stream().filter(account -> account.getClient().getEmail().equals(authentication.getName())).collect(Collectors.toList());
        return clientAccounts.stream().map(account -> new AccountDto(account)).collect(Collectors.toList());

    }

    @GetMapping("/api/clients/current/accounts/{id}")
    public AccountDto getClientAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(account -> new AccountDto(account)).orElse(null);
    }


    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;



   @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType accountType, @RequestParam CardColor cardColor, Authentication authentication) {

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




    @Autowired
    TransactionRepository transactionRepository;


    @PatchMapping("/api/clients/current/accounts/delete")
    public ResponseEntity<Object> changeAccountStatus(@RequestBody AccountDto accountDto, Authentication authentication){

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



    @DeleteMapping("/api/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestBody AccountDto accountDto, Authentication authentication){


        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());

        Account findAccount = accountRepository.findById(accountDto.getId());

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
            return new ResponseEntity<>("La cuenta se ha eliminado con éxito", HttpStatus.CREATED);
        }





    }



}
