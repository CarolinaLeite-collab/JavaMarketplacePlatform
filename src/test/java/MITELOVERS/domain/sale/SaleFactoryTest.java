package MITELOVERS.domain.sale;

import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleSaleStatus;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class SaleFactoryTest {

    @Test
    void shouldCreateSale() {

        //SUT
        SaleFactory saleFactory = new SaleFactory();

        try (MockedConstruction<Sale> mockedConstruction = mockConstruction(Sale.class)) {

            //Arrange
            UserId buyerIdDouble = mock(UserId.class);
            List<SaleLine> saleLinesDouble = new ArrayList<>();

            //Act
            Sale sale = saleFactory.createSale(
                    buyerIdDouble,
                    saleLinesDouble
            );

            //Assert
            assertNotNull(sale);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }

    @Test
    void shouldCreateSale() {

        //SUT
        SaleFactory saleFactory = new SaleFactory();

        try (MockedConstruction<Sale> mockedConstruction = mockConstruction(Sale.class)) {

            //Arrange
            SaleId saleIdDouble = mock(SaleId.class);
            UserId buyerIdDouble = mock(UserId.class);
            List<SaleLine> saleLinesDouble = new ArrayList<>();
            LocalDateTime createdAtDouble = mock(LocalDateTime.class);
            LocalDateTime completedAtDouble = mock(LocalDateTime.class);
            SaleSaleStatus saleSaleStatus = SaleSaleStatus.PENDING;

            //Act
            Sale sale = saleFactory.createSale(
                    saleIdDouble,
                    buyerIdDouble,
                    saleLinesDouble,
                    createdAtDouble,
                    completedAtDouble,
                    saleSaleStatus
            );

            //Assert
            assertNotNull(sale);
            assertEquals(1, mockedConstruction.constructed().size());

        }

    }
}