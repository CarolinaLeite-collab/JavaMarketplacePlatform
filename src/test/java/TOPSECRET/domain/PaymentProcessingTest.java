package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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