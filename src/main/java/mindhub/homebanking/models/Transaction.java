package mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(strategy="native", name="native")
    private Long Id;

    private Double ammount;

    private String description;

    private LocalDateTime date;

    private TransactionType type;

    private double balanceLeft;

@ManyToOne(fetch=FetchType.EAGER)
@JoinColumn(name="account_id")
private Account account;


    public Transaction() {
    }

    public Transaction(Double ammount, String description, LocalDateTime date, TransactionType type, double balanceLeft, Account account) {
        this.ammount = ammount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.balanceLeft = balanceLeft;
        this.account = account;
    }

    public Long getId() {
        return Id;
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getBalanceLeft() {
        return balanceLeft;
    }

    public void setBalanceLeft(double balanceLeft) {
        this.balanceLeft = balanceLeft;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
