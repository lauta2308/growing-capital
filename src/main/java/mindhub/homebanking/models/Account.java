package mindhub.homebanking.models;



import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private long id;
    private String number;

    private AccountType type;

    private LocalDateTime creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;


    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    private AccountStatus status;

    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, double balance, AccountStatus status) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.status = status;
    }

    public Account(String number, AccountType type, LocalDateTime creationDate, double balance, AccountStatus status) {
        this.number = number;
        this.type = type;
        this.creationDate = creationDate;
        this.balance = balance;
        this.status = status;

    }



    public Account(String number, AccountType type, LocalDateTime creationDate, double balance, Client client, AccountStatus status) {
        this.number = number;
        this.type = type;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.status = status;

    }



    public Account(String number, AccountType type, LocalDateTime creationDate, double balance, Client client, Card card, AccountStatus status) {
        this.number = number;
        this.type = type;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.card = card;
        this.status = status;

    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public void addCard(Card newCard){
        newCard.setAccount(this);
        card = newCard;
    }




}
