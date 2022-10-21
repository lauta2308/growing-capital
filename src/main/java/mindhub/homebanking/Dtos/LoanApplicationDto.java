package mindhub.homebanking.Dtos;


public class LoanApplicationDto {

    private long loan;

    private Double amount;

    private Integer payments;

    private String destinyAccount;



    public LoanApplicationDto() {
    }

    public LoanApplicationDto(long loan, Double amount, Integer payments, String destinyAccount) {
        this.loan = loan;
        this.amount = amount;
        this.payments = payments;
        this.destinyAccount = destinyAccount;
    }

    public long getLoan() {
        return loan;
    }

    public void setLoan(long loan) {
        this.loan = loan;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getDestinyAccount() {
        return destinyAccount;
    }

    public void setDestinyAccount(String destinyAccount) {
        this.destinyAccount = destinyAccount;
    }
}
