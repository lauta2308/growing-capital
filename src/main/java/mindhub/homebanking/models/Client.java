package mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private String lastName;
    private String email;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy="owner", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    private String password;


    public Client() {
    }

    public Client(String name, String lastName, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Client(String name, String lastName, String email, Set<Account> accounts, Set<ClientLoan> clientLoans, Set<Card> cards, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accounts;
        this.clientLoans = clientLoans;
        this.cards = cards;
        this.password = password;
    }

    public long getId() {
        return id;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccount() {return accounts;}

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }



    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void addLoans(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);

    }

    public void addCard(Card card) {
        card.setOwner(this);
        cards.add(card);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
