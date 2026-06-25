package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartLineId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class ShoppingCartLineFactoryTest {

    @Test
    void shouldCreateShoppingCartLine() {

        //SUT
        ShoppingCartLineFactory shoppingCartLineFactory = new ShoppingCartLineFactory();

        try (MockedConstruction<ShoppingCartLine> mockedConstruction = mockConstruction(ShoppingCartLine.class)) {

            //Arrange
            DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
            UserId buyerIdDouble = mock(UserId.class);
            Price priceDouble = mock(Price.class);

            //Act
            ShoppingCartLine shoppingCartLine = shoppingCartLineFactory
                    .createNewShoppingCartLine(
                            directSaleIdDouble,
                            buyerIdDouble,
                            priceDouble
                    );

            //Assert
            assertNotNull(shoppingCartLine);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

    @Test
    void shouldCreateShoppingCartLineWithId() {

        //SUT
        ShoppingCartLineFactory shoppingCartLineFactory = new ShoppingCartLineFactory();

        try (MockedConstruction<ShoppingCartLine> mockedConstruction = mockConstruction(ShoppingCartLine.class)) {

            //Arrange
            ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);
            DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
            UserId buyerIdDouble = mock(UserId.class);
            Price priceDouble = mock(Price.class);
            LocalDateTime time = LocalDateTime.now();


            //Act
            ShoppingCartLine shoppingCartLine = shoppingCartLineFactory
                    .createNewShoppingCartLine(
                            lineIdDouble,
                            directSaleIdDouble,
                            buyerIdDouble,
                            priceDouble,
                            time
                            );

            //Assert
            assertNotNull(shoppingCartLine);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

}