package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentProcessingTest {

    @Test
    void shouldCreatePaymentProcessing_whenAllArgumentsAreValid() {
        // Arrange
        Price price = new Price(5,Currency.EUR);
        Country country = new Country("Portugal");
        User buyer = new User(
                new Name("Ana"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("user@hotmail.com"),
                new Phone (new PhonePrefix("+351"), "930914359")
        );
        User seller = new User(
                new Name("Joao"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("user@hotmail.com"),
                new Phone (new PhonePrefix("+351"), "930914359")
        );

        // Act
        PaymentProcessing pp = new PaymentProcessing(price, buyer, seller);

        // Assert
        assertNotNull(pp);
    }

    @Test
    void shouldThrowException_whenPriceIsNull() {
        // Arrange
        Country country = new Country("Portugal");
        User buyer = new User(
                new Name("Ana"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("user@hotmail.com"),
                new Phone (new PhonePrefix("+351"), "930914359")
        );
        User seller = new User(
                new Name("Joao"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("user@hotmail.com"),
                new Phone (new PhonePrefix("+351"), "930914359")
        );

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new PaymentProcessing(null, buyer, seller);
        });
    }

    @Test
    void shouldThrowException_whenBuyerIsNull() {
        // Arrange
        Country country = new Country("Portugal");
        Price price = new Price(5,Currency.EUR);
        User seller = new User(
                new Name("Joao"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("user@hotmail.com"),
                new Phone (new PhonePrefix("+351"), "930914359")
        );

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new PaymentProcessing(price, null, seller);
        });
    }

    @Test
    void shouldThrowException_whenSellerIsNull() {
        // Arrange
        Price price = new Price(5,Currency.EUR);
        Country country = new Country("Portugal");
        User buyer = new User(
                new Name("Ana"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("user@hotmail.com"),
                new Phone (new PhonePrefix("+351"), "930914359")
        );
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new PaymentProcessing(price, buyer, null);
        });
    }

}