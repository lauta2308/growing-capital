package mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private Long Id;

    private double ammount;

    private Integer payments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;


    public ClientLoan() {
    }

    public ClientLoan(double ammount, Integer payments, Client client, Loan loan) {
        this.ammount = ammount;
        this.payments = payments;
        this.client = client;
        this.loan = loan;
    }

    public ClientLoan(double ammount, Integer payments, Loan loan) {
        this.ammount = ammount;
        this.payments = payments;
        this.loan = loan;
    }

    public Long getId() {
        return Id;
    }


    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }


}
