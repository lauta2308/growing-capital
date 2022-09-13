package mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private CardType type;

    private CardColor color;

    private String number;

    private long cvv;

    private LocalDate fromDate;

    private LocalDate thruDate;



    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client owner;

    private String cardHolder;

    @OneToOne(mappedBy="card")
    private Account account;

    private CardStatus status;


    public Card() {
    }

    public Card(CardType type, CardColor color, String number, long cvv, LocalDate fromDate, LocalDate thruDate, String cardHolder, CardStatus status) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cardHolder = cardHolder;
        this.status = status;
    }

    public Card(CardType type, CardColor color, String number, long cvv, LocalDate fromDate, LocalDate thruDate, Client owner, String cardHolder, CardStatus status) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.owner = owner;
        this.cardHolder = cardHolder;
        this.status = status;
    }

    public Card(CardType type, CardColor color, String number, long cvv, LocalDate fromDate, LocalDate thruDate, Client owner, String cardHolder, Account account, CardStatus status) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.owner = owner;
        this.cardHolder = cardHolder;
        this.account = account;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getCvv() {
        return cvv;
    }

    public void setCvv(long cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }
}
