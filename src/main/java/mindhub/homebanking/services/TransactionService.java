package mindhub.homebanking.services;

import mindhub.homebanking.Dtos.PosnetPaymentDto;
import mindhub.homebanking.Dtos.TransactionDTO;
import mindhub.homebanking.Dtos.TransactionFilterDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface TransactionService {
    ResponseEntity<Object> makeTransaction(Double ammount, String description, String sender, String receiver, Authentication authentication);

    ResponseEntity<Object> paymentWithPosnet(PosnetPaymentDto posnetPaymentDto);

    Set<TransactionDTO> filterTransactions(Authentication authentication, TransactionFilterDto transactionFilterDto);


}
