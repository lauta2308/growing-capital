package mindhub.homebanking.controllers;

import mindhub.homebanking.Dtos.LoanDto;
import mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    LoanRepository loanRepository;


    @GetMapping("/api/loans")
    public List<LoanDto> getLoans(){
        return loanRepository.findAll().stream().map(loan -> new LoanDto(loan)).collect(Collectors.toList());
    }



}
