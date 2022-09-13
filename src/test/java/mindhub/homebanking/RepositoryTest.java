package mindhub.homebanking;

import mindhub.homebanking.Dtos.ClientDto;
import mindhub.homebanking.models.Account;
import mindhub.homebanking.models.Loan;
import mindhub.homebanking.models.TransactionType;
import mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class RepositoryTest {


    @Autowired
    LoanRepository loanRepository;

    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existHipotecarioLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("hipotecario"))));

    }

    @Test
    public void existAutomotrizLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Automotriz"))));

    }


    @Autowired
    AccountRepository accountRepository;

            @Test
            public void existAccounts(){
                List<Account> accounts = accountRepository.findAll();

                assertThat(accounts, is(not(empty())));

            }

            @Test
            public void accountLengthNumbers(){
                long countAccounts = accountRepository.findAll().stream().count();
                long verifyLength = accountRepository.findAll().stream().filter(account -> account.getNumber().length() == 12).count();

                assertThat(verifyLength, equalToObject(countAccounts));
            }

            @Test
            public void accountsContainVin(){
                long countAccounts = accountRepository.findAll().stream().count();
                long verifyVin = accountRepository.findAll().stream().filter(account -> account.getNumber().contains("VIN")).count();


                assertThat(verifyVin, equalToObject(countAccounts));
            }


            @Autowired
            ClientRepository clientRepository;

            @Test
            public void haveATLeastOneAccount(){
                long countClients = clientRepository.findAll().stream().count();
                List<ClientDto> clientDtos = clientRepository.findAll().stream().map(client -> new ClientDto(client)).collect(Collectors.toList());

                long haveAccount = clientDtos.stream().filter(clientDto -> clientDto.getAccount() != null).count();
                assertThat(haveAccount, equalToObject(countClients));
            }


            @Test
            public void haveEmail(){
                long countClients = clientRepository.findAll().stream().count();
                long countEmails = clientRepository.findAll().stream().filter(client -> client.getEmail() != null).count();

                assertThat(countEmails, equalToObject(countClients));
            }


            @Autowired
            CardRepository cardRepository;

            @Test
            public void correctLength(){
                long countCards = cardRepository.findAll().stream().count();

                long correctLength = cardRepository.findAll().stream().filter(card -> card.getNumber().length() == 19).count();

                assertThat(correctLength, equalToObject(countCards));


            }

            @Test
    public void haveType(){
                long countCards = cardRepository.findAll().stream().count();

                long haveType = cardRepository.findAll().stream().filter(card -> card.getType() != null).count();

                assertThat(haveType, equalToObject(countCards));
            }


            @Autowired
    ClientLoanRepository clientLoanRepository;

            @Test
            public void haveClientId() {

                long countClientLoans = clientLoanRepository.findAll().stream().count();

                long countClientLoansWithClients = clientLoanRepository.findAll().stream().filter(clientLoan -> clientLoan.getClient() != null).count();

                assertThat(countClientLoansWithClients, equalToObject(countClientLoans));



            }


            @Test
    public void haveLoanId() {

        long countClientLoans = clientLoanRepository.findAll().stream().count();

        long countClientLoansWithLoans = clientLoanRepository.findAll().stream().filter(clientLoan -> clientLoan.getLoan() != null).count();

        assertThat(countClientLoansWithLoans, equalToObject(countClientLoans));



    }


    @Autowired
    TransactionRepository transactionRepository;

            @Test
    public void haveDestinyAccount() {

        long countTransactions = transactionRepository.findAll().stream().count();

        long countTransactionsWithDestiny = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccount() != null).count();

        assertThat(countTransactionsWithDestiny, equalToObject(countTransactions));



    }

    @Test
    public void haveTransactionWithCorrectType() {

        long countTransactions = transactionRepository.findAll().stream().count();

        long countTransactionsWithCorrectType = transactionRepository.findAll().stream().filter(transaction -> transaction.getType() == TransactionType.DEBITO || transaction.getType() == TransactionType.CREDITO).count();

        assertThat(countTransactionsWithCorrectType, equalToObject(countTransactions));



    }



}
