package MITELOVERS.domain.directsale;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleFactoryTest {

    @Test
    void shouldCreateDirectSale() {
        //Arrange
        List<ItemId> itemsId = new ArrayList<>();
        ItemId itemIdDouble = mock(ItemId.class);
        itemsId.add(itemIdDouble);

        Price priceDouble = mock(Price.class);
        Duration timeLimit = Duration.ofDays(30);

        List<List<Object>> capturedArguments = new ArrayList<>();

        //SUT
        DirectSaleFactory directSaleFactory = new DirectSaleFactory();

        try (MockedConstruction<DirectSale> mockedConstruction = mockConstruction(DirectSale.class,
                (mock, context) -> capturedArguments.add(new ArrayList<>(context.arguments())))) {

            //Act
            DirectSale directSaleResult = directSaleFactory.createDirectSale(itemsId, priceDouble, timeLimit);

            //Assert
            assertNotNull(directSaleResult);
            List<Object> params = capturedArguments.get(0);
            assertSame(itemsId, params.get(0));
            assertSame(priceDouble, params.get(1));
            assertSame(timeLimit, params.get(2));
            assertEquals(1, mockedConstruction.constructed().size());
            assertEquals(mockedConstruction.constructed().get(0), directSaleResult);
        }

    }

    @Test
    void shouldRecreateDirectSaleWithDirectSaleIdAsArgument() {
        //Arrange
        DirectSaleId id = mock(DirectSaleId.class);
        List<ItemId> itemsId = mock(List.class);
        Price price = mock(Price.class);
        Duration timeLimit = Duration.ofDays(5);
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");

        //SUT
        DirectSaleFactory factory = new DirectSaleFactory();

        try (MockedConstruction<DirectSale> mocked =
                     mockConstruction(DirectSale.class,
                             (mock, context) -> {
                                 when(mock.identity())
                                         .thenReturn(id);
                             })) {

            //Act
            DirectSale newDirectSale = factory.createDirectSale(id, itemsId, price, timeLimit, creationDate);

            //Assert
            assertEquals(id, newDirectSale.identity());

        }
    }
}
