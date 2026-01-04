package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PaymentProcessingTest {

        @Test
        void shouldCreatePaymentProcessing_whenAllArgumentsAreValid() {
            // Arrange
            Price price = new Price();
            User buyer = new User();
            User seller = new User();
            PaymentProcessing.PaymentMethod method = PaymentProcessing.PaymentMethod.TRANSFER;

            // Act
            PaymentProcessing pp = new PaymentProcessing(price, buyer, seller, method);

            // Assert
            assertNotNull(pp);
        }

        @Test
        void shouldThrowException_whenPriceIsNull() {
            // Arrange
            User buyer = new User();
            User seller = new User();
            PaymentProcessing.PaymentMethod method = PaymentProcessing.PaymentMethod.TRANSFER;

            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new PaymentProcessing(null, buyer, seller, method);
            });
        }

        @Test
        void shouldThrowException_whenBuyerIsNull() {
            // Arrange
            Price price = new Price();
            User seller = new User();
            PaymentProcessing.PaymentMethod method = PaymentProcessing.PaymentMethod.TRANSFER;

            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new PaymentProcessing(price, null, seller, method);
            });
        }

        @Test
        void shouldThrowException_whenSellerIsNull() {
            // Arrange
            Price price = new Price();
            User buyer = new User();
            PaymentProcessing.PaymentMethod method = PaymentProcessing.PaymentMethod.TRANSFER;

            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new PaymentProcessing(price, buyer, null, method);
            });
        }

        @Test
        void shouldThrowException_whenPaymentMethodIsNull() {
            // Arrange
            Price price = new Price();
            User buyer = new User();
            User seller = new User();

            // Act + Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new PaymentProcessing(price, buyer, seller, null);
            });
        }
    }
}