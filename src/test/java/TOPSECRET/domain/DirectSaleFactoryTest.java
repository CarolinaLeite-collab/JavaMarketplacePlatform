package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class DirectSaleFactoryTest {

    @Test
    void shouldCreateDirectSale() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Price priceDouble = mock(Price.class);
        Period timeLimit = Period.ofDays(30);

        // SUT
        DirectSaleFactory directSaleFactory = new DirectSaleFactory();

        try (MockedConstruction<DirectSale> mockedConstruction = mockConstruction(DirectSale.class)) {

            // Act
            DirectSale directSaleResult = directSaleFactory.createDirectSale(itemDouble, priceDouble, timeLimit);

            // Assert
            assertNotNull(directSaleResult);
            assertEquals(1, mockedConstruction.constructed().size());
            assertEquals(mockedConstruction.constructed().get(0), directSaleResult);
        }
    }
}