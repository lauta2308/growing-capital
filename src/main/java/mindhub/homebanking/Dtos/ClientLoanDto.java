package mindhub.homebanking.Dtos;

import mindhub.homebanking.models.Client;
import mindhub.homebanking.models.ClientLoan;
import mindhub.homebanking.models.Loan;

public class ClientLoanDto {

    private Long Id;

    private Long loanId;

    private String loanName;

    private double loanAmmount;

    private Integer payments;

    public ClientLoanDto() {
    }

    public ClientLoanDto(ClientLoan clientLoan) {
        this.Id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.loanAmmount = clientLoan.getAmmount();
        this.payments = clientLoan.getPayments();
    }

    public Long getId() {
        return Id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public double getLoanAmmout() {
        return loanAmmount;
    }

    public Integer getPayments() {
        return payments;
    }


}
