package mindhub.homebanking.controllers;

import mindhub.homebanking.Dtos.CardDto;
import mindhub.homebanking.models.CardColor;
import mindhub.homebanking.models.CardType;

import mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
public class CardController {


    @Autowired
    CardService cardService;


    @GetMapping("/api/cards")
    public List<CardDto> getAllCards(Authentication authentication) {

        return cardService.getAllCards();


    }



    @GetMapping("/api/clients/current/cards")
    public Set<CardDto> getClientCards(Authentication authentication) {

        return cardService.getClientCards(authentication);


    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(CardType type, CardColor color, Authentication authentication){

        return cardService.createCard(type, color, authentication);






    }

    @PatchMapping("/api/clients/current/accounts/cards/delete")
    public ResponseEntity<Object> changeCardStatus(@RequestBody CardDto cardDto, Authentication authentication){


        return cardService.changeCardStatus(cardDto, authentication);




    }




    @DeleteMapping("/api/cards/delete")
    public ResponseEntity<Object> deleteAccount(@RequestBody CardDto cardDto, Authentication authentication){

        return cardService.deleteAccount(cardDto, authentication);






    }


    
}
