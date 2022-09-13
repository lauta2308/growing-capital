package mindhub.homebanking.Dtos;

public class PosnetPaymentDto {

    private String cardNumber;

    private Long cardCvv;

    private Double amount;

    private String description;

    public PosnetPaymentDto() {
    }

    public PosnetPaymentDto(String cardNumber, Long cardCvv, Double amount, String description) {
        this.cardNumber = cardNumber;
        this.cardCvv = cardCvv;
        this.amount = amount;
        this.description = description;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Long getCardCvv() {
        return cardCvv;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
