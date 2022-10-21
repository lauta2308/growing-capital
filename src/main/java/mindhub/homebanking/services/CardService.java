package mindhub.homebanking.services;

import mindhub.homebanking.Dtos.CardDto;
import mindhub.homebanking.models.CardColor;
import mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface CardService {
    List<CardDto> getAllCards();

    Set<CardDto> getClientCards(Authentication authentication);

    ResponseEntity<Object> createCard(CardType type, CardColor color, Authentication authentication);

    ResponseEntity<Object> changeCardStatus(CardDto cardDto, Authentication authentication);

    ResponseEntity<Object> deleteAccount(CardDto cardDto, Authentication authentication);
}
