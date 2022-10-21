package mindhub.homebanking.services.implementations;

import mindhub.homebanking.Dtos.CardDto;
import mindhub.homebanking.models.*;
import mindhub.homebanking.repositories.CardRepository;
import mindhub.homebanking.repositories.ClientRepository;
import mindhub.homebanking.services.CardService;
import mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static mindhub.homebanking.models.CardType.DEBITO;

@Service
public class CardServiceImplementations implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream().map(card -> new CardDto(card)).collect(Collectors.toList());
    }

    @Override
    public Set<CardDto> getClientCards(Authentication authentication) {
        return  clientRepository.findByEmail(authentication.getName()).getCards().stream().map(card -> new CardDto(card)).collect(Collectors.toSet());
    }

    @Override
    public ResponseEntity<Object> createCard(CardType type, CardColor color, Authentication authentication) {
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

    @Override
    public ResponseEntity<Object> changeCardStatus(CardDto cardDto, Authentication authentication) {
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

    @Override
    public ResponseEntity<Object> deleteAccount(CardDto cardDto, Authentication authentication) {
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
            return new ResponseEntity<>("La tarjeta se ha eliminado con éxito", HttpStatus.CREATED);
        }
    }
}
