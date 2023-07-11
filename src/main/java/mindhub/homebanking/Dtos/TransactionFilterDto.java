package mindhub.homebanking.Dtos;

public class TransactionFilterDto {

    private String id;

    private String initialDate;

    private String finalDate;

    public TransactionFilterDto(String id, String initialDate, String finalDate) {
        this.id = id;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public String getId() {
        return id;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }
}
