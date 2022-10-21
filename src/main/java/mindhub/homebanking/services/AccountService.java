package mindhub.homebanking.services;

import mindhub.homebanking.Dtos.AccountDto;
import mindhub.homebanking.models.Account;
import mindhub.homebanking.models.AccountType;
import mindhub.homebanking.models.CardColor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface AccountService {
    List<Account> getAccounts();

    AccountDto getAccount(Long id);

    Set<Account> getClientAccounts(Authentication authentication);

    ResponseEntity<Object> createAccount(AccountType accountType, CardColor cardColor, Authentication authentication);

    ResponseEntity<Object> changeAccountStatus(AccountDto accountDto, Authentication authentication);

    ResponseEntity<Object> deleteAccount(AccountDto accountDto, Authentication authentication);

    AccountDto getClientAccount(Long id);
}
