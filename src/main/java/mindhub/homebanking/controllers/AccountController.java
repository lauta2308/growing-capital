package mindhub.homebanking.controllers;

import mindhub.homebanking.Dtos.AccountDto;
import mindhub.homebanking.models.AccountType;
import mindhub.homebanking.models.CardColor;
import mindhub.homebanking.services.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;



    @GetMapping("/api/accounts")
    public List<AccountDto> getAccounts(Authentication authentication) {

            return accountService.getAccounts().stream().map(account -> new AccountDto(account)).collect(Collectors.toList());


        }


    @GetMapping("/api/accounts/{id}")
    public AccountDto getAccount(@PathVariable Long id) {

        return accountService.getAccount(id);

    }

    @GetMapping("/api/clients/current/accounts")
    public Set<AccountDto> getClientAccounts(Authentication authentication) {

        return accountService.getClientAccounts(authentication).stream().map(account -> new AccountDto(account)).collect(Collectors.toSet());

    }

    @GetMapping("/api/clients/current/accounts/{id}")
    public AccountDto getClientAccount(@PathVariable Long id) {

        return accountService.getClientAccount(id);

    }




   @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType accountType, @RequestParam CardColor cardColor, Authentication authentication) {

      return accountService.createAccount(accountType, cardColor, authentication);


       }




    @PatchMapping("/api/clients/current/accounts/delete")
    public ResponseEntity<Object> changeAccountStatus(@RequestBody AccountDto accountDto, Authentication authentication){


        return accountService.changeAccountStatus(accountDto, authentication);



    }


    @DeleteMapping("/api/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestBody AccountDto accountDto, Authentication authentication){

        return accountService.deleteAccount(accountDto, authentication);


    }





}
