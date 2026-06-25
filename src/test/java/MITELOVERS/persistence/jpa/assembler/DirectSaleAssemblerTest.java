package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
        UserId sellerIdDouble = mock(UserId.class);
        DirectSaleStatus status = DirectSaleStatus.ACTIVE;

        Price price = mock(Price.class);
        when(price.getValue()).thenReturn(10.0);
        when(price.getCurrency()).thenReturn(Currency.EUR);

        Duration duration = Duration.ofDays(5);
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");

        DirectSaleFactory factory = mock(DirectSaleFactory.class);

        DirectSale directSale = mock(DirectSale.class);
        when(directSale.identity()).thenReturn(id);
        when(directSale.getItemsId()).thenReturn(List.of(item));
        when(directSale.getSellerId()).thenReturn(sellerIdDouble);
        when(directSale.getPrice()).thenReturn(price);
        when(directSale.getTimeLimit()).thenReturn(duration);
        when(directSale.getCreationDate()).thenReturn(creationDate);
        when(directSale.getDSStatus()).thenReturn(status);

        Long expectedResult = duration.toDays();

        //SUT
        DirectSaleAssembler assembler = new DirectSaleAssembler(factory);

        //Act
        DirectSaleDataModel dm = assembler.toDataModel(directSale);

        //Assert
        assertEquals(expectedResult,dm.getTimeLimit());

    }

    @Test
    void shouldConvertDataModelToDomain() {
        //Arrange
        DirectSale expected = mock(DirectSale.class);
        Instant creationDate = Instant.parse("2024-01-01T10:00:00Z");
        String email = "email@email.com";

        PriceDataModel priceDM = mock(PriceDataModel.class);
        when(priceDM.getNumericValue()).thenReturn(10.0);
        when(priceDM.getCurrency()).thenReturn("EUR");

        DirectSaleDataModel dm = mock(DirectSaleDataModel.class);
        when(dm.getDirectSaleId()).thenReturn("DS-ABCDEF12");
        when(dm.getItemsId()).thenReturn(List.of("ABC123DEF0"));
        when(dm.getUserId()).thenReturn(email);
        when(dm.getPrice()).thenReturn(priceDM);
        when(dm.getTimeLimit()).thenReturn(5L);
        when(dm.getCreationDate()).thenReturn(creationDate);
        when(dm.getStatus()).thenReturn("ACTIVE");



        DirectSaleFactory factory = mock(DirectSaleFactory.class);
        when(factory.createDirectSale(
                new DirectSaleId("DS-ABCDEF12"),
                List.of(new ItemId("ABC123DEF0")),
                new UserId(new Email(email)),
                new Price(10.0, Currency.EUR),
                Duration.ofDays(5),
                Instant.parse("2024-01-01T10:00:00Z"),
                DirectSaleStatus.ACTIVE
        )).thenReturn(expected);

        //SUT
        DirectSaleAssembler assembler = new DirectSaleAssembler(factory);

        //Act
        DirectSale result = assembler.toDomain(dm);

        //Assert
        assertSame(expected, result);
    }
}