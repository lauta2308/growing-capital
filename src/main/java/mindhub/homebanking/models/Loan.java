package mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    private long id;

    private String name;

    private double maxAmmount;

    @ElementCollection
    @Column(name="payments")
    private List <Integer> payments = new ArrayList<>();



    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    private Integer fee;

    public Loan() {
    }

    public Loan(String name, Double maxAmmount, List<Integer> payments) {
        this.name = name;
        this.maxAmmount = maxAmmount;
        this.payments = payments;
    }


    public Loan(String name, double maxAmmount, List<Integer> payments, Integer fee) {
        this.name = name;
        this.maxAmmount = maxAmmount;
        this.payments = payments;
        this.clientLoans = clientLoans;
        this.fee = fee;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmmount() {
        return maxAmmount;
    }

    public void setMaxAmmount(double maxAmmount) {
        this.maxAmmount = maxAmmount;
    }


    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;

    }



}
