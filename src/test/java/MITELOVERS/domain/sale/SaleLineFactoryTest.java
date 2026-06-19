package MITELOVERS.domain.sale;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class SaleLineFactoryTest {

    @Test
    void shouldCreateSaleLine() {

        //SUT
        SaleLineFactory saleLineFactory = new SaleLineFactory();

        try (MockedConstruction<SaleLine> mockedConstruction = mockConstruction(SaleLine.class)) {

            //Arrange
            UserId sellerIdDouble = mock(UserId.class);
            Price priceAtSaleDouble = mock(Price.class);
            DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);

            //Act
            SaleLine saleLine = saleLineFactory.createSaleLine(
                    sellerIdDouble,
                    priceAtSaleDouble,
                    directSaleIdDouble
            );

            //Assert
            assertNotNull(saleLine);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

    @Test
    void shouldRehydratSale() {

        //SUT
        SaleLineFactory saleLineFactory = new SaleLineFactory();

        try (MockedConstruction<SaleLine> mockedConstruction = mockConstruction(SaleLine.class)) {

            //Arrange
            SaleLineId saleLineIdDouble = mock(SaleLineId.class);
            UserId sellerIdDouble = mock(UserId.class);
            Price priceAtSaleDouble = mock(Price.class);
            DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);

            //Act
            SaleLine saleLine = saleLineFactory.createSale(
                    saleLineIdDouble,
                    sellerIdDouble,
                    priceAtSaleDouble,
                    directSaleIdDouble
            );

            //Assert
            assertNotNull(saleLine);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }
}