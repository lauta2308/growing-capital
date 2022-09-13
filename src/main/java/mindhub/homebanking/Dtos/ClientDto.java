package mindhub.homebanking.Dtos;

import mindhub.homebanking.models.Account;
import mindhub.homebanking.models.Client;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDto {

    private String name;
    private String lastName;
    private String email;

    private Long Id;

    private String password;

    private Set <AccountDto> account;

    private Set <ClientLoanDto> loans;

    private Set <CardDto> cards;

    public ClientDto() {
    }



    public ClientDto(Client client) {
        this.name = client.getname();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.Id = client.getId();
        this.password = client.getPassword();
        this.account = client.getAccount().stream().map(account -> new AccountDto(account)).collect(Collectors.toSet());
        this.loans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDto(clientLoan)).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(card -> new CardDto(card)).collect(Collectors.toSet());

    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return Id;
    }

    public Set<AccountDto> getAccount() {
        return account;
    }

    public Set<ClientLoanDto> getLoans() {
        return loans;
    }

    public Set<CardDto> getCards() {
        return cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
