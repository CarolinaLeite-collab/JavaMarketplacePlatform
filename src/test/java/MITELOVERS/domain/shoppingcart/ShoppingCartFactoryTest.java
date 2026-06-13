package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class ShoppingCartFactoryTest {

    @Test
    void shouldCreateShoppingCart() {

        //SUT
        ShoppingCartFactory shoppingCartFactory = new ShoppingCartFactory();

        try (MockedConstruction<ShoppingCart> mockedConstruction = mockConstruction(ShoppingCart.class)) {

            //Arrange
            UserId buyerIdDouble = mock(UserId.class);

            //Act
            ShoppingCart shoppingCart = shoppingCartFactory
                    .createShoppingCart(
                            buyerIdDouble
                    );

            //Assert
            assertNotNull(shoppingCart);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

    @Test
    void shouldCreateShoppingCartWithId() {

        //SUT
        ShoppingCartFactory shoppingCartFactory = new ShoppingCartFactory();

        try (MockedConstruction<ShoppingCart> mockedConstruction = mockConstruction(ShoppingCart.class)) {

            //Arrange
            ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
            UserId buyerIdDouble = mock(UserId.class);
            Price totalAmountDouble = mock(Price.class);
            List<ShoppingCartLine> cartLinesDouble = new ArrayList<>();


            //Act
            ShoppingCart shoppingCart = shoppingCartFactory
                    .createShoppingCart(
                            cartIdDouble,
                            buyerIdDouble,
                            totalAmountDouble,
                            cartLinesDouble);

            //Assert
            assertNotNull(shoppingCart);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }



}