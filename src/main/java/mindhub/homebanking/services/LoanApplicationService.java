package mindhub.homebanking.services;

import mindhub.homebanking.Dtos.LoanApplicationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface LoanApplicationService {
    ResponseEntity<String> addLoanApplication(LoanApplicationDto loanApplicationDto, Authentication authentication);

    ResponseEntity<Object> createNewLoan(String name, double maxAmmount, Integer[] payments, Integer fee, Authentication authentication);
}
