package mindhub.homebanking.Dtos;

import mindhub.homebanking.models.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

public class CardDto {

    private long id;
    private CardType type;

    private CardColor color;

    private String number;

    private long cvv;

    private LocalDate fromDate;

    private LocalDate thruDate;



    private String cardHolder;

    private CardStatus status;

    public CardDto() {
    }

    public CardDto(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cardHolder = card.getCardHolder();
        this.status = card.getStatus();
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public long getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }


    public String getCardHolder() {
        return cardHolder;
    }

    public CardStatus getStatus() {
        return status;
    }
}
