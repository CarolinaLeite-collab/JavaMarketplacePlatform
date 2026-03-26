package TOPSECRET.domain.valueobject;

import TOPSECRET.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PaymentProcessingTest {

    @Test
    void payment_should_be_successful() {

        // Arrange
        Price price = mock(Price.class);
        User buyer = mock(User.class);
        User seller = mock(User.class);

        //Sut
        PaymentProcessing payment = new PaymentProcessing(price, buyer, seller);

        // Act
        boolean result = payment.isSuccessful();

        // Assert
        assertTrue(result);
    }

}