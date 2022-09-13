package mindhub.homebanking;

import mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {

    @Test
    public void cardNumberIsCreated(){

        String cardNumber = CardUtils.getCardNumber();

        assertThat(cardNumber,is(not(emptyOrNullString())));

    }

    @Test
    public void cardNumberCorrectLength(){

        String cardNumber = CardUtils.getCardNumber();

        assertThat(cardNumber, hasLength(19));

    }

    @Test
    public void cardCvvMinNumber(){

        long cardCvv = CardUtils.getCvv();
        long minNumber = 100;

        assertThat(cardCvv, greaterThan(minNumber));

    }

    @Test
    public void cardCVVMaxNumber(){

        long cardCvv = CardUtils.getCvv();
        long maxNumber = 999;

        assertThat(cardCvv, lessThan(maxNumber));

    }
}
