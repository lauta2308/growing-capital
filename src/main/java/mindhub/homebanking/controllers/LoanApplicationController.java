package mindhub.homebanking.controllers;

import mindhub.homebanking.Dtos.LoanApplicationDto;
import mindhub.homebanking.services.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
public class LoanApplicationController {
    @Autowired
    LoanApplicationService loanApplicationService;

    @Transactional
    @PostMapping("/api/clientloans")


    public ResponseEntity<String> addLoanApplication(@RequestBody LoanApplicationDto loanApplicationDto, Authentication authentication) {


       return loanApplicationService.addLoanApplication(loanApplicationDto, authentication);

        }


    @PostMapping("/admin/newloan")
    public ResponseEntity<Object> createNewLoan(String name, double maxAmmount, Integer[] payments, Integer fee, Authentication authentication){


        return loanApplicationService.createNewLoan(name, maxAmmount, payments, fee, authentication);





    }


}
