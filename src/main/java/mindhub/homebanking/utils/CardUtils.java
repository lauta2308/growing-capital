package mindhub.homebanking.utils;

public final class CardUtils {

    private CardUtils() {
    }


    public static long getCvv() {
        long cardCvv = Math.round(100 + Math.random() * 900);
        return cardCvv;
    }

    public static String getCardNumber() {
        String cardNumber;
        cardNumber = Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000) + "-" + Math.round(1000 + Math.random() * 9000);
        return cardNumber;
    }

}
