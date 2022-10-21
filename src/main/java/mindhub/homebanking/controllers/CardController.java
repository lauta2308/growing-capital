package mindhub.homebanking.controllers;

import com.sun.istack.NotNull;
import mindhub.homebanking.Dtos.AccountDto;
import mindhub.homebanking.Dtos.CardDto;
import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.CardRepository;
import mindhub.homebanking.repositories.ClientRepository;
import mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static mindhub.homebanking.models.CardType.CREDITO;
import static mindhub.homebanking.models.CardType.DEBITO;


@RestController
public class CardController {


    @Autowired
    CardRepository cardRepository;

    @GetMapping("/api/cards")
    public List<CardDto> getAllCards(Authentication authentication) {


        return cardRepository.findAll().stream().map(card -> new CardDto(card)).collect(Collectors.toList());

    }

    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/api/clients/current/cards")
    public List<CardDto> getCards(Authentication authentication) {

        List <Card> clientCards =  cardRepository.findAll().stream().filter(card -> card.getOwner().getEmail().equals(authentication.getName())).collect(Collectors.toList());
        return clientCards.stream().map(card -> new CardDto(card)).collect(Collectors.toList());

    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(CardType type, CardColor color, Authentication authentication){


        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());
        String cardNumber = "";
        long cardCvv = CardUtils.getCvv();
        long cantidadTarjetasTipoRequerido = clienteAutenticado.getCards().stream().filter(card -> card.getType() == type && card.getStatus() == CardStatus.ACTIVE).count();
        long colorYTipo = clienteAutenticado.getCards().stream().filter(card -> card.getType() == type && card.getColor() == color && card.getStatus() == CardStatus.ACTIVE).count();



        if(type == null || color == null){
            return new ResponseEntity<>("Error: Selecciona el tipo y color de tarjeta", HttpStatus.FORBIDDEN);
        } else if (type == DEBITO) {
            return new ResponseEntity<>("Error: Para crear una tarjeta de débito debes crear una cuenta", HttpStatus.FORBIDDEN);

        } else if(cantidadTarjetasTipoRequerido >= 3) {
            return new ResponseEntity<>("No puedes tener más de tres tarjetas del mismo tipo", HttpStatus.FORBIDDEN);

        } else if (colorYTipo == 1) {
            return new ResponseEntity<>("No puedes tener más de una tarjeta del mismo color y tipo, elige otra", HttpStatus.FORBIDDEN);
        } else {
            do{
                cardNumber = CardUtils.getCardNumber();
            } while(cardRepository.findByNumber(cardNumber) != null);





            Card nuevaTarjeta = new Card(type, color, cardNumber, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5), clienteAutenticado, clienteAutenticado.getname() + clienteAutenticado.getLastName(), CardStatus.ACTIVE);
            cardRepository.save(nuevaTarjeta);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }




    }

    @PatchMapping("/api/clients/current/accounts/cards/delete")
    public ResponseEntity<Object> changeCardStatus(@RequestBody CardDto cardDto, Authentication authentication){


        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());


        Card findCard = cardRepository.findById(cardDto.getId());

        Boolean validCardOwner = findCard.getOwner().getEmail().equals(clienteAutenticado.getEmail());


        if(clienteAutenticado == null){
            return new ResponseEntity<>("El cliente autenticado no se encuentra en la base de datos", HttpStatus.FORBIDDEN);
        } else if (findCard == null) {
            return new ResponseEntity<>("La tarjeta no se encuentra en la base de datos", HttpStatus.FORBIDDEN);

        } else if (!validCardOwner) {
            return new ResponseEntity<>("La tarjeta no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);

        } else if(findCard.getType() == DEBITO){
            return new ResponseEntity<>("No puedes eliminar una tarjeta asociada a una cuenta", HttpStatus.FORBIDDEN);
        }

        else {
            findCard.setStatus(CardStatus.INACTIVE);
            cardRepository.save(findCard);
            return new ResponseEntity<>("La tarjeta se ha eliminado con éxito", HttpStatus.CREATED);
        }


    }




    @DeleteMapping("/api/cards/delete")
    public ResponseEntity<Object> deleteAccount(@RequestBody CardDto cardDto, Authentication authentication){


        Client clienteAutenticado = clientRepository.findByEmail(authentication.getName());

        Card findCard = cardRepository.findById(cardDto.getId());

        Boolean isAdministrator = clienteAutenticado.getEmail().contains("admin");




        if(clienteAutenticado == null){
            return new ResponseEntity<>("El cliente autenticado no se encuentra en la base de datos", HttpStatus.FORBIDDEN);
        } else if (findCard == null) {
            return new ResponseEntity<>("La tarjeta no se encuentra en la base de datos", HttpStatus.FORBIDDEN);

        } else if (!isAdministrator) {
            return new ResponseEntity<>("Sólo los administradores pueden eliminar tarjetas", HttpStatus.FORBIDDEN);

        } else if (cardDto.getStatus() == CardStatus.ACTIVE) {
            return new ResponseEntity<>("No se pueden eliminar tarjetas activas", HttpStatus.FORBIDDEN);

        } else if (cardDto.getType() == DEBITO) {
            return new ResponseEntity<>("Para eliminar tarjetas de débito hay que eliminar antes la cuenta", HttpStatus.FORBIDDEN);

        }
        else {
            cardRepository.deleteById(findCard.getId());
            return new ResponseEntity<>("La cuenta se ha eliminado con éxito", HttpStatus.CREATED);
        }





    }


    
}
