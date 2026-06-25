package MITELOVERS.mapper;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.AuctionResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionResponseDTOMapperTest {

    @Test
    void toDTOWithOutrightPriceMapsAllFields() {
        // arrange
        Auction auctionDouble = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);

        Price startingPriceDouble = mock(Price.class);
        Price reservePriceDouble = mock(Price.class);
        Price outrightPriceDouble = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(2);

        UserId sellerDouble = mock(UserId.class);

        when(auctionDouble.identity()).thenReturn(auctionIdDouble);
        when(auctionIdDouble.toString()).thenReturn("A1");

        when(auctionDouble.getStartingPrice()).thenReturn(startingPriceDouble);
        when(startingPriceDouble.getValue()).thenReturn(10.0);

        when(auctionDouble.getReservePrice()).thenReturn(reservePriceDouble);
        when(reservePriceDouble.getValue()).thenReturn(20.0);

        when(auctionDouble.getOutrightPrice()).thenReturn(outrightPriceDouble);
        when(outrightPriceDouble.getValue()).thenReturn(30.0);

        when(startingPriceDouble.getCurrency()).thenReturn(Currency.EUR);

        when(auctionDouble.getAuctionStartDate()).thenReturn(startDate.toInstant());
        when(auctionDouble.getAuctionEndDate()).thenReturn(endDate.toInstant());

        when(auctionDouble.getSeller()).thenReturn(sellerDouble);
        when(auctionDouble.getCurrentPrice()).thenReturn(startingPriceDouble);

        // SUT
        AuctionResponseDTOMapper mapper = new AuctionResponseDTOMapper();

        // act
        AuctionResponseDTO dto = mapper.toDTO(auctionDouble);

        // assert
        assertEquals("A1", dto.getAuctionId());
        assertEquals(10.0, dto.getStartingPrice());
        assertEquals(20.0, dto.getReservePrice());
        assertEquals(30.0, dto.getOutrightPrice());
        assertEquals("EUR", dto.getPriceCurrency());
        assertEquals(startDate.toInstant(), dto.getStartDate());
        assertEquals(endDate.toInstant(), dto.getEndDate());
    }

    @Test
    void toDTOWhenOutrightPriceIsNullMapsNullOutrightPrice() {
        // arrange
        Auction auctionDouble = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);

        Price startingPriceDouble = mock(Price.class);
        Price reservePriceDouble = mock(Price.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(2);

        UserId sellerDouble = mock(UserId.class);

        when(auctionDouble.identity()).thenReturn(auctionIdDouble);
        when(auctionIdDouble.toString()).thenReturn("A1");

        when(auctionDouble.getStartingPrice()).thenReturn(startingPriceDouble);
        when(startingPriceDouble.getValue()).thenReturn(10.0);

        when(auctionDouble.getReservePrice()).thenReturn(reservePriceDouble);
        when(reservePriceDouble.getValue()).thenReturn(20.0);

        when(startingPriceDouble.getCurrency()).thenReturn(Currency.EUR);

        when(auctionDouble.getAuctionStartDate()).thenReturn(startDate.toInstant());
        when(auctionDouble.getAuctionEndDate()).thenReturn(endDate.toInstant());

        when(auctionDouble.getSeller()).thenReturn(sellerDouble);
        when(auctionDouble.getCurrentPrice()).thenReturn(startingPriceDouble);

        // SUT
        AuctionResponseDTOMapper mapper = new AuctionResponseDTOMapper();

        // act
        AuctionResponseDTO dto = mapper.toDTO(auctionDouble);

        // assert
        assertNull(dto.getOutrightPrice());
    }

}