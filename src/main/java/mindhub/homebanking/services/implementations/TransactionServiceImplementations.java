package mindhub.homebanking.services.implementations;

import com.sun.istack.NotNull;
import mindhub.homebanking.Dtos.*;
import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.AccountRepository;
import mindhub.homebanking.repositories.CardRepository;
import mindhub.homebanking.repositories.ClientRepository;
import mindhub.homebanking.repositories.TransactionRepository;
import mindhub.homebanking.services.ClientService;
import mindhub.homebanking.services.TransactionService;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TransactionServiceImplementations implements TransactionService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientService clientService;

    @Override
    public ResponseEntity<Object> makeTransaction(Double ammount, String description, String sender, String receiver, Authentication authentication) {
        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());
        long validSender = accountRepository.findAll().stream().filter(account -> account.getNumber().equals(sender)).count();

        if(validSender == 0){
            return new ResponseEntity<>("La cuenta de origen no es válida", HttpStatus.FORBIDDEN);
        }


        Account senderAccount = accountRepository.findByNumber(sender);
        Account receiverAccount = accountRepository.findByNumber(receiver);
        Boolean cuentaDelClienteAutenticado = clienteAutenticado.getAccount().contains(senderAccount);
        Boolean montoDisponibleSender = senderAccount.getBalance() >= ammount;
        Boolean notValidAmmount = ammount.isInfinite() || ammount.isNaN() || ammount <= 0;


        if (notValidAmmount || description.isEmpty() || sender.isEmpty() || receiver.isEmpty()) {

            return new ResponseEntity<>("Quedan campos sin completar o el monto no es válido", HttpStatus.FORBIDDEN);


        } else if (senderAccount.equals(receiverAccount) ) {

            return new ResponseEntity<>("El remitente y receptor de la transacción no puede ser la misma cuenta", HttpStatus.FORBIDDEN);
        } else if (!senderAccount.getNumber().equals(sender)) {

            return new ResponseEntity<>("La cuenta de origen no es válida", HttpStatus.FORBIDDEN);
        } else if (receiverAccount == null) {

            return new ResponseEntity<>("La cuenta de destino no es válida", HttpStatus.FORBIDDEN);
        } else if (!cuentaDelClienteAutenticado ) {

            return new ResponseEntity<>("La cuenta que intenta enviar el dinero no es del cliente autenticado", HttpStatus.FORBIDDEN);
        } else if (!montoDisponibleSender) {

            return new ResponseEntity<>("La cuenta no dispone del monto suficiente para la transacción", HttpStatus.FORBIDDEN);
        } else {


            Transaction transaction1 = new Transaction(-ammount, description, LocalDateTime.now(), TransactionType.DEBITO, senderAccount.getBalance() - ammount,senderAccount);
            Transaction transaction2 = new Transaction(ammount, description, LocalDateTime.now(), TransactionType.CREDITO, receiverAccount.getBalance() + ammount,  receiverAccount);

            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);


            senderAccount.setBalance(senderAccount.getBalance() - ammount);
            receiverAccount.setBalance(receiverAccount.getBalance() + ammount);
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);



            return new ResponseEntity<>("transacción realizada con éxito", HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Object> paymentWithPosnet(PosnetPaymentDto posnetPaymentDto) {

        if(posnetPaymentDto.getCardNumber().isEmpty()){
            return new ResponseEntity<>("No se recibió un numero de tarjeta", HttpStatus.FORBIDDEN);
        }

        if(posnetPaymentDto.getDescription().isEmpty()){
            return new ResponseEntity<>("No se recibió una descripción", HttpStatus.FORBIDDEN);
        }

        if(posnetPaymentDto.getAmount() <= 0){
            return new ResponseEntity<>("El monto no puede ser menor o igual a 0", HttpStatus.FORBIDDEN);
        }

        if(posnetPaymentDto.getCardCvv() == null){
            return new ResponseEntity<>("No se recibió un Cvv válido", HttpStatus.FORBIDDEN);
        }



        long cardFound = cardRepository.findAll().stream().filter(card -> card.getNumber().equals(posnetPaymentDto.getCardNumber())).count();


        if(cardFound <= 0) {
            return new ResponseEntity<>("El número de tarjeta no es válido", HttpStatus.FORBIDDEN);
        }


        Card findCard = cardRepository.findByNumber(posnetPaymentDto.getCardNumber());

        Boolean sameCard = findCard.getCvv() == posnetPaymentDto.getCardCvv();

        if(findCard.getThruDate().isBefore(LocalDate.now()) ){
            return new ResponseEntity<>("La tarjeta está vencida.", HttpStatus.FORBIDDEN);

        }


        Client cardOwner = clientRepository.findByEmail(findCard.getOwner().getEmail());
        Set<Account> clientAccounts = cardOwner.getAccount();

        if(cardOwner == null) {
            return new ResponseEntity<>("No se encontró al cliente en la base de datos", HttpStatus.FORBIDDEN);
        }

        if(!sameCard) {
            return new ResponseEntity<>("El numero de tarjeta y cvv no coinciden con nuestros registros", HttpStatus.FORBIDDEN);
        }

        if(findCard.getType() == CardType.CREDITO) {

            Set <Account> accountsAvailable =  clientAccounts.stream().filter(account -> account.getBalance() >= posnetPaymentDto.getAmount()).collect(Collectors.toSet());

            if(accountsAvailable.size() == 0){
                return new ResponseEntity<>("No posees fondos suficientes en ninguna de tus cuentas", HttpStatus.FORBIDDEN);
            } else {
                Account accountToDebit = accountsAvailable.stream().findFirst().orElse(null);

                Transaction transaction = new Transaction(posnetPaymentDto.getAmount(), posnetPaymentDto.getDescription(), LocalDateTime.now(), TransactionType.DEBITO, accountToDebit.getBalance() - posnetPaymentDto.getAmount(), accountToDebit);

                accountToDebit.setBalance(accountToDebit.getBalance() - posnetPaymentDto.getAmount());

                transactionRepository.save(transaction);
                return new ResponseEntity<>("Se ha efectuado la compra correctamente", HttpStatus.CREATED);
            }


        }


        Account accountOwnerOfCard = findCard.getAccount();

        if(accountOwnerOfCard.getBalance() < posnetPaymentDto.getAmount()){
            return new ResponseEntity<>("La cuenta asociada no posee los fondos suficientes para la compra", HttpStatus.FORBIDDEN);
        } else {

            Transaction transaction = new Transaction(posnetPaymentDto.getAmount(), posnetPaymentDto.getDescription(), LocalDateTime.now(), TransactionType.DEBITO, accountOwnerOfCard.getBalance() - posnetPaymentDto.getAmount(), accountOwnerOfCard);

            accountOwnerOfCard.setBalance(accountOwnerOfCard.getBalance() - posnetPaymentDto.getAmount());

            transactionRepository.save(transaction);
            return new ResponseEntity<>("Se ha efectuado la compra correctamente", HttpStatus.CREATED);

        }

    }

    @Override
    public Set<TransactionDTO> filterTransactions(Authentication authentication, TransactionFilterDto transactionFilterDto) {
        // Convierto los string de fecha a localedate

        String firstDate = transactionFilterDto.getInitialDate() + " " + "00:00:00";
        String lastDate = transactionFilterDto.getFinalDate() + " " + "00:00:00";


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime initialLocaleDate = LocalDateTime.parse(firstDate, formatter);
        LocalDateTime finallLocaleDate = LocalDateTime.parse(lastDate, formatter);

        // Busco al cliente autenticado
        ClientDto clientDto = clientService.getCurrentClient(authentication);


        long accountId = Long.valueOf(transactionFilterDto.getId());
        // Busco la lista de cuentas que coincidan con la id de la cuenta recibida por parámetro
        List<AccountDto> accountDtoList = clientDto.getAccount().stream().filter(accountDto1 ->  accountDto1.getId() == accountId).collect(Collectors.toList());

        // Asigno la cuenta encontrada a una variable (sin la lista)

        AccountDto accountDto = accountDtoList.get(0);


        // Creo un set de transacciones  y filtro las transacciones por fecha

        Set<TransactionDTO> transactionDTOSet = accountDto.getTransactions().stream().filter(transactionDTO ->
                        transactionDTO.getDate().isEqual(initialLocaleDate) || transactionDTO.getDate().isAfter(initialLocaleDate))
                .filter(transactionDTO -> transactionDTO.getDate().isEqual(finallLocaleDate) || transactionDTO.getDate().isBefore(finallLocaleDate))
                .collect(Collectors.toSet());



        return transactionDTOSet;
    }


}
