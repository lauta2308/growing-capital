package mindhub.homebanking.controllers;


import mindhub.homebanking.Dtos.ClientDto;
import mindhub.homebanking.Dtos.ClientLoanDto;
import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.AccountRepository;
import mindhub.homebanking.repositories.CardRepository;
import mindhub.homebanking.repositories.ClientLoanRepository;
import mindhub.homebanking.repositories.ClientRepository;
import mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ClientController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @GetMapping("/api/clients")
    public List<ClientDto> getClients(){

        return clientRepository.findAll().stream().map(client -> new ClientDto(client)).collect(Collectors.toList());
    }



    @GetMapping("/api/clients/{id}")
    public ClientDto getClients(@PathVariable Long id){


            return clientRepository.findById(id).map(client -> new ClientDto(client)).orElse(null);



    }



    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/clients")

    public ResponseEntity<Object> register(

            @RequestParam String name, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }


        if (clientRepository.findByEmail(email) != null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }

        Client nuevoCliente = new Client(name, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(nuevoCliente);

            String cardNumber = "";
            long cardCvv = CardUtils.getCvv();

            do{
                cardNumber = CardUtils.getCardNumber();
            } while(cardRepository.findByNumber(cardNumber) != null);


            Card nuevaTarjeta = new Card(CardType.DEBITO , CardColor.SILVER, cardNumber, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5), nuevoCliente, name + lastName, CardStatus.ACTIVE);

            cardRepository.save(nuevaTarjeta);


            String numeroAleatorio = "";
            Account verificarNumero;

                do{
                    numeroAleatorio = "VIN-" + Math.round(10000000 + Math.random() * 90000000);
                    verificarNumero = accountRepository.findByNumber(numeroAleatorio);

                }while(
                        verificarNumero != null

                );


            Account cuentaSinRepetir = new Account(numeroAleatorio, AccountType.CORRIENTE, LocalDateTime.now(), 0,nuevoCliente, nuevaTarjeta, AccountStatus.ACTIVE);

            accountRepository.save(cuentaSinRepetir);





            return new ResponseEntity<>("Usuario creado con éxito",HttpStatus.CREATED);
        }


        @GetMapping("/api/clients/current")

        public ClientDto getClient (Authentication authentication){

            return new ClientDto(clientRepository.findByEmail(authentication.getName()));

        }



}
