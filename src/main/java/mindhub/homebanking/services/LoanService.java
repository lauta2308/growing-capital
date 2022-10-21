package mindhub.homebanking.services;

import mindhub.homebanking.Dtos.LoanDto;

import java.util.List;

public interface LoanService {
    List<LoanDto> getLoans();
}
