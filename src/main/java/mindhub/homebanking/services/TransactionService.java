package mindhub.homebanking.services;

import mindhub.homebanking.Dtos.PosnetPaymentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface TransactionService {
    ResponseEntity<Object> makeTransaction(Double ammount, String description, String sender, String receiver, Authentication authentication);

    ResponseEntity<Object> paymentWithPosnet(PosnetPaymentDto posnetPaymentDto);
}
