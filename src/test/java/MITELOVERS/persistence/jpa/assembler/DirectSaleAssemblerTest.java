package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DirectSaleAssemblerTest {

    @Test
    void directSaleAssemblerConstructorTest(){
        //Arrange
        DirectSaleFactory factory = mock(DirectSaleFactory.class);

        //Act
        //SUT
        new DirectSaleAssembler(factory);
    }

    @Test
    void shouldConvertDomainToDataModel() {
        //Arrange
        DirectSaleId id = mock(DirectSaleId.class);
        ItemId item = mock(ItemId.class);
        when(id.toString()).thenReturn("DS1");

        Price price = mock(Price.class);
        when(price.getValue()).thenReturn(10.0);
        when(price.getCurrency()).thenReturn(Currency.EUR);

        Duration duration = Duration.ofDays(5);
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");

        DirectSaleFactory factory = mock(DirectSaleFactory.class);

        DirectSale directSale = mock(DirectSale.class);
        when(directSale.identity()).thenReturn(id);
        when(directSale.getItemsId()).thenReturn(List.of(item));
        when(directSale.getPrice()).thenReturn(price);
        when(directSale.getTimeLimit()).thenReturn(duration);
        when(directSale.getCreationDate()).thenReturn(creationDate);

        String expectedResult = duration.toString();

        //SUT
        DirectSaleAssembler assembler = new DirectSaleAssembler(factory);

        //Act
        DirectSaleDataModel dm = assembler.domain2DM(directSale);

        //Assert
        assertEquals(expectedResult,dm.getTimeLimit());

    }

    @Test
    void shouldConvertDataModelToDomain() {
        //Arrange
        DirectSale expected = mock(DirectSale.class);
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");

        PriceDataModel priceDM = mock(PriceDataModel.class);
        when(priceDM.getNumericValue()).thenReturn(10.0);
        when(priceDM.getCurrency()).thenReturn("EUR");

        DirectSaleDataModel dm = mock(DirectSaleDataModel.class);
        when(dm.getDirectSaleId()).thenReturn("DS1");
        when(dm.getItemsId()).thenReturn(List.of("ABC123DEF0"));
        when(dm.getPrice()).thenReturn(priceDM);
        when(dm.getTimeLimit()).thenReturn(5L);
        when(dm.getCreationDate()).thenReturn(creationDate);



        DirectSaleFactory factory = mock(DirectSaleFactory.class);
        when(factory.createDirectSale(
                new DirectSaleId("DS1"),
                List.of(new ItemId("ABC123DEF0")),
                new Price(10.0, Currency.EUR),
                Duration.ofDays(5),
                Instant.parse("2024-01-01T10:00:00Z")
        )).thenReturn(expected);

        //SUT
        DirectSaleAssembler assembler = new DirectSaleAssembler(factory);

        //Act
        DirectSale result = assembler.DM2Domain(dm);

        //Assert
        assertSame(expected, result);
    }
}