package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.auction.BidFactory;
import MITELOVERS.domain.valueobject.BidId;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.BidDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidAssemblerTest {

    @Test
    void shouldConvertBidToDataModel() {
        //Arrange
        BidFactory bidFactoryDouble = new BidFactory();
        Bid bidDouble = mock(Bid.class);
        Instant dateDouble = Instant.now();
        UserId userIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);
        BidId bidIdDouble = mock(BidId.class);

        when(bidDouble.getUserId()).thenReturn(userIdDouble);
        when(bidDouble.getOfferPrice()).thenReturn(priceDouble);
        when(bidDouble.getBidDate()).thenReturn(dateDouble);
        when(bidDouble.identity()).thenReturn(bidIdDouble);
        when(priceDouble.getValue()).thenReturn(100.0);
        when(priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(userIdDouble.toString()).thenReturn("test@email.com");
        when(bidIdDouble.toString()).thenReturn("some-bid-id");

        //SUT
        BidAssembler assembler = new BidAssembler(bidFactoryDouble);

        //Act
        BidDataModel result = assembler.toDataModel(bidDouble);

        //Assert
        assertNotNull(result);
        assertEquals(priceDouble.getValue(), result.getOfferPrice().getNumericValue());
        assertEquals(priceDouble.getCurrency().name(), result.getOfferPrice().getCurrency());
        assertEquals(dateDouble.toString(), result.getBidDate());
        assertEquals(userIdDouble.toString(), result.getUserId());
        assertEquals(bidIdDouble.toString(), result.getBidId());
    }

    @Test
    void shouldConvertBidDataModelToDomain() {
        //Arrange
        BidFactory bidFactoryDouble = new BidFactory();
        BidDataModel bidDmDouble = mock(BidDataModel.class);
        PriceDataModel priceDmDouble = mock(PriceDataModel.class);

        when(bidDmDouble.getUserId()).thenReturn("test@email.com");
        when(bidDmDouble.getBidId()).thenReturn(UUID.randomUUID().toString());
        when(bidDmDouble.getBidDate()).thenReturn(Instant.now().toString());

        when(priceDmDouble.getNumericValue()).thenReturn(100.0);
        when(priceDmDouble.getCurrency()).thenReturn("EUR");

        when(bidDmDouble.getOfferPrice()).thenReturn(priceDmDouble);

        //SUT
        BidAssembler assembler = new BidAssembler(bidFactoryDouble);

        //Act
        Bid result = assembler.toDomain(bidDmDouble);

        //Assert
        assertNotNull(result);
        assertEquals("test@email.com", result.getUserId().toString());
        assertEquals(100.0, result.getOfferPrice().getValue());
        assertEquals(Currency.EUR, result.getOfferPrice().getCurrency());

    }


}