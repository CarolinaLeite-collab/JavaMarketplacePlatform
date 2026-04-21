package MITELOVERS.domain.directsale;

import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class DirectSaleFactoryTest {

    @Test
    void shouldCreateDirectSale() {
        // Arrange
        List<ItemId> itemsId = new ArrayList<>();
        ItemId itemIdDouble = mock(ItemId.class);
        itemsId.add(itemIdDouble);

        Price priceDouble = mock(Price.class);
        Period timeLimit = Period.ofDays(30);

        List<List<Object>> capturedArguments = new ArrayList<>();

        // SUT
        DirectSaleFactory directSaleFactory = new DirectSaleFactory();

        try (MockedConstruction<DirectSale> mockedConstruction = mockConstruction(DirectSale.class,
                (mock, context) -> capturedArguments.add(new ArrayList<>(context.arguments())))) {

            // Act
            DirectSale directSaleResult = directSaleFactory.createDirectSale(itemsId, priceDouble, timeLimit);

            // Assert
            assertNotNull(directSaleResult);
            List<Object> params = capturedArguments.get(0);
            assertSame(itemsId, params.get(0));
            assertSame(priceDouble, params.get(1));
            assertSame(timeLimit, params.get(2));
            assertEquals(1, mockedConstruction.constructed().size());
            assertEquals(mockedConstruction.constructed().get(0), directSaleResult);
        }

    }

}
