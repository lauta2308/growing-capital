package mindhub.homebanking.services.implementations;

import mindhub.homebanking.Dtos.LoanDto;
import mindhub.homebanking.repositories.LoanRepository;
import mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplementations implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<LoanDto> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDto(loan)).collect(Collectors.toList());
    }
}
