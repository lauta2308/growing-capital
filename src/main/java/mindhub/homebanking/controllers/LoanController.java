package mindhub.homebanking.controllers;

import mindhub.homebanking.Dtos.LoanDto;
import mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoanController {

    @Autowired
    LoanService loanService;


    @GetMapping("/api/loans")
    public List<LoanDto> getLoans(){

        return loanService.getLoans();

    }



}
