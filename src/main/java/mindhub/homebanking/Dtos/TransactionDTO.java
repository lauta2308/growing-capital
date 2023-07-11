package mindhub.homebanking.Dtos;

import mindhub.homebanking.models.Transaction;
import mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private Double ammount;

    private String description;

    private LocalDateTime date;

    private TransactionType type;

    private double balanceLeft;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {

        this.id = transaction.getId();
        this.ammount = transaction.getAmmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.balanceLeft = transaction.getBalanceLeft();
    }

    public Long getId() {
        return id;
    }

    public Double getAmmount() {
        return ammount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public double getBalanceLeft() {
        return balanceLeft;
    }

    public String toString(){
        return "La id es: " + getId() + " la descripción es: " + getDescription() + " el monto es " + getAmmount() + " la fecha es " + getDate() + " el tipo es " + getType() + " el balance left es " + getBalanceLeft();

    }
}
