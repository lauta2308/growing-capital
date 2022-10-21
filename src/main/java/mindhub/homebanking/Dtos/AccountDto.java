package mindhub.homebanking.Dtos;

import mindhub.homebanking.models.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDto {
    private long id;
    private String number;

    private AccountType accountType;
    private LocalDateTime creationDate;
    private double balance;

    private CardDto card;

    private Set<TransactionDTO> transactions;

    private AccountStatus status;

    public AccountDto() {
    }

    public AccountDto(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.accountType = account.getType();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.card = new CardDto(account.getCard());
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.status = account.getStatus();
    }



    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public CardDto getCard() {
        return card;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountStatus getStatus() {
        return status;
    }
}
