package mindhub.homebanking.controllers;

import mindhub.homebanking.Dtos.PosnetPaymentDto;
import mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> makeTransaction(@RequestParam Double ammount, @RequestParam String description,@RequestParam String sender, @RequestParam String receiver, Authentication authentication) {

        return transactionService.makeTransaction(ammount, description, sender, receiver, authentication);


    }


    @Transactional
    @PostMapping("/api/posnetpayment")
    public ResponseEntity<Object> paymentWithPosnet(@RequestBody PosnetPaymentDto posnetPaymentDto) {


        return transactionService.paymentWithPosnet(posnetPaymentDto);



    }



}