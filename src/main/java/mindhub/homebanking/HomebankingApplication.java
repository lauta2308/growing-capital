package mindhub.homebanking;

import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);


	}



	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers

			Client cliente1 = new Client("Melba", "Morel", "melba@test.com", passwordEncoder.encode("1234"));
			clientRepository.save(cliente1);

			Client cliente2 = new Client("Lautaro", "Yosbere", "lauta@test.com", passwordEncoder.encode("4567"));
			clientRepository.save(cliente2);


			Card card1 = new Card(CardType.DEBITO, CardColor.GOLD, Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000), Math.round(100 + Math.random() * 900), LocalDate.of(2022,04,05), LocalDate.of(2022,05,02), cliente1, cliente1.getname()+ " " + cliente1.getLastName(),CardStatus.ACTIVE);


			cardRepository.save(card1);



			Account cuenta1 = accountRepository.save(new Account("VIN-" + Math.round(10000000 + Math.random() * 90000000),AccountType.CORRIENTE, LocalDateTime.now(), 50000.00, cliente1, card1, AccountStatus.ACTIVE));


			clientRepository.save(cliente1);
			accountRepository.save(cuenta1);





			Transaction transaction1 = new Transaction(5000.00, "test", LocalDateTime.now(), TransactionType.DEBITO,cuenta1.getBalance() - 5000.00, cuenta1);
			transactionRepository.save(transaction1);
			cuenta1.setBalance(cuenta1.getBalance() - transaction1.getAmmount());
			accountRepository.save(cuenta1);
			Transaction transaction2 = new Transaction(8000.00, "test", LocalDateTime.now().plusDays(1), TransactionType.CREDITO, cuenta1.getBalance() + 8000.00,cuenta1);
			transactionRepository.save(transaction2);
			cuenta1.setBalance(cuenta1.getBalance() + transaction2.getAmmount());


			accountRepository.save(cuenta1);


			Loan loan1 = new Loan("Hipotecario", 500000.00, Arrays.asList(12,24,36,48, 60), 40);
			Loan loan2 = new Loan("Personal", 100000.00, Arrays.asList(6,12,24), 20);
			Loan loan3 = new Loan("Automotriz", 300000.00, Arrays.asList(6,12,24,36), 30);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			

			ClientLoan clientLoan1 = new ClientLoan(400000.00, 60, cliente1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000.00, 12, cliente1, loan2);

			clientRepository.save(cliente1);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan(100000.00, 24, cliente2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000.00, 36, cliente2, loan3);


			clientRepository.save(cliente1);
			clientRepository.save(cliente2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);





			Card card4 = new Card(CardType.CREDITO, CardColor.TITANIUM, Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000), Math.round(100 + Math.random() * 900), LocalDate.of(2022,04,05), LocalDate.of(2027,03,02), cliente1, cliente1.getname()+ " " + cliente1.getLastName(), CardStatus.ACTIVE);

			Card card5 = new Card(CardType.CREDITO, CardColor.TITANIUM, Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000), Math.round(100 + Math.random() * 900), LocalDate.of(2022,04,05), LocalDate.of(2027,03,02), cliente2, cliente2.getname() + " " + cliente2.getLastName(), CardStatus.ACTIVE);


			cardRepository.save(card4);
			cardRepository.save(card5);

			clientRepository.save(cliente1);
			clientRepository.save(cliente2);

			Card card6 = new Card(CardType.CREDITO, CardColor.SILVER, Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000), Math.round(100 + Math.random() * 900), LocalDate.of(2022,04,05), LocalDate.of(2027,03,02), cliente1, cliente1.getname() + " " + cliente1.getLastName(), CardStatus.ACTIVE);




			cardRepository.save(card6);
			clientRepository.save(cliente1);

			Client cliente4 = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("1234"));
			clientRepository.save(cliente4);




		};
	}

}
