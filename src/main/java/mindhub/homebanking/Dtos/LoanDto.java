package mindhub.homebanking.Dtos;

import mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDto {
    private long id;

    private String name;

    private double maxAmmount;

    private List<Integer> payments;

    private Integer fee;


    public LoanDto() {
    }

    public LoanDto(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmmount = loan.getMaxAmmount();
        this.payments = loan.getPayments();
        this.fee = loan.getFee();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Integer getFee() {
        return fee;
    }
}
