package TOPSECRET.domain.directsale;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleFactoryTest {

    @Test
    void shouldCreateDirectSale() {
        // Arrange
        List<Item> items = new ArrayList<>();
        Item itemDouble = mock(Item.class);
        items.add(itemDouble);

        Price priceDouble = mock(Price.class);
        Period timeLimit = Period.ofDays(30);

        List<List<Object>> capturedArguments = new ArrayList<>();

        // SUT
        DirectSaleFactory directSaleFactory = new DirectSaleFactory();

        try (MockedConstruction<DirectSale> mockedConstruction = mockConstruction(DirectSale.class,
                (mock, context) -> capturedArguments.add(new ArrayList<>(context.arguments())))) {

            // Act
            DirectSale directSaleResult = directSaleFactory.createDirectSale(items, priceDouble, timeLimit);

            // Assert
            assertNotNull(directSaleResult);
            List<Object> params = capturedArguments.get(0);
            assertSame(items, params.get(0));
            assertSame(priceDouble, params.get(1));
            assertSame(timeLimit, params.get(2));
            assertEquals(1, mockedConstruction.constructed().size());
            assertEquals(mockedConstruction.constructed().get(0), directSaleResult);
        }

    }

}