package mindhub.homebanking.controllers;

import mindhub.homebanking.Dtos.ClientDto;
import mindhub.homebanking.Dtos.LoanApplicationDto;
import mindhub.homebanking.Dtos.LoanDto;
import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanApplicationController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Transactional
    @PostMapping("/api/clientloans")


    public ResponseEntity<String> addLoanApplication(@RequestBody LoanApplicationDto loanApplicationDto, Authentication authentication) {

        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());

        long validLoan = loanRepository.findAll().stream().filter(loan -> loan.getId() == loanApplicationDto.getLoan()).count();

        if(validLoan == 0){
            return new ResponseEntity<>("El tipo de préstamo solicitado no es válido", HttpStatus.FORBIDDEN);

        }

        Loan loan = loanRepository.findById(loanApplicationDto.getLoan());

        Account destinyAccount = accountRepository.findByNumber(loanApplicationDto.getDestinyAccount());

        Boolean validDestinyAccount = clienteAutenticado.getAccount().contains(destinyAccount);

        ClientDto clientdto = new ClientDto(clienteAutenticado);

        long loanId = loanApplicationDto.getLoan();
        long hasLoan = clientdto.getLoans().stream().filter(clientLoanDto -> clientLoanDto.getLoanId().equals(loanId)).count();

        long paymentsAvailable = loan.getPayments().stream().filter(payment -> payment.equals(loanApplicationDto.getPayments())).count();

        Boolean ammountAvailable = loanApplicationDto.getAmount() <= loan.getMaxAmmount();

        Boolean notValidAmmount = loanApplicationDto.getAmount().isInfinite() || loanApplicationDto.getAmount().isNaN() || loanApplicationDto.getAmount() < 20000;




        if(hasLoan > 0){
            return new ResponseEntity<>("No puedes pedir más de un préstamo del mismo tipo antes de cancelar el que ya tienes", HttpStatus.FORBIDDEN);
        }
        else if(loan == null){
            return new ResponseEntity<>("El préstamo debe ser válido", HttpStatus.FORBIDDEN);
        } else if(!ammountAvailable) {
            return new ResponseEntity<>("El monto solicitado es mayor al máximo", HttpStatus.FORBIDDEN);
        } else if(notValidAmmount) {
            return new ResponseEntity<>("El monto solicitado no es válido", HttpStatus.FORBIDDEN);
        } else if(paymentsAvailable == 0) {
            return new ResponseEntity<>("Las cuotas solicitadas no son válidas para este préstamo", HttpStatus.FORBIDDEN);
        } else if(destinyAccount == null){
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
        else if(!validDestinyAccount) {
            return new ResponseEntity<>("La cuenta de destino no es válida", HttpStatus.FORBIDDEN);
        }

        else {

            Integer loanFee = loan.getFee();

            ClientLoan clientLoan = new ClientLoan((loanApplicationDto.getAmount()*loanFee/100)+loanApplicationDto.getAmount(), loanApplicationDto.getPayments(), clienteAutenticado, loan);

            clientLoanRepository.save(clientLoan);

            Transaction transaction = new Transaction(loanApplicationDto.getAmount(), "Loan approved", LocalDateTime.now(), TransactionType.CREDITO,destinyAccount.getBalance() + loanApplicationDto.getAmount(), destinyAccount);

            transactionRepository.save(transaction);

            destinyAccount.setBalance(destinyAccount.getBalance() + loanApplicationDto.getAmount());
            accountRepository.save(destinyAccount);

            return new ResponseEntity<>("Préstamo aprobado", HttpStatus.CREATED);
        }







    }



    @PostMapping("/admin/newloan")
    public ResponseEntity<Object> createNewLoan(String name, double maxAmmount, Integer[] payments, Integer fee, Authentication authentication){

        if(name.isEmpty() || maxAmmount <= 0 || payments == null || fee == null){
            return new ResponseEntity<>("Quedan campos sin completar", HttpStatus.FORBIDDEN);
        }


        Client adminAutenticado = clientRepository.findByEmail(authentication.getName());

        if(adminAutenticado == null) {
            return new ResponseEntity<>("Sólo los administradores pueden crear préstamos", HttpStatus.FORBIDDEN);
        }

        Loan nombrePrestamoDuplicado = loanRepository.findByName(name);

        if(nombrePrestamoDuplicado != null) {
            return new ResponseEntity<>("Ya existe un préstamo con este nombre", HttpStatus.FORBIDDEN);
        } else if (fee <= 0) {
            return new ResponseEntity<>("El interés del préstamo debe ser mayor a  0", HttpStatus.FORBIDDEN);

        } else {
            Loan loan = new Loan(name, maxAmmount, Arrays.asList(payments), fee);

            loanRepository.save(loan);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }


    }


}
