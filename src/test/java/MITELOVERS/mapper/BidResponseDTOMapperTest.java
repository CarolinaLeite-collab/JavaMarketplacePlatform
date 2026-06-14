package MITELOVERS.mapper;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.BidId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.BidResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidResponseDTOMapperTest {

    @Test
    void toDTOMapsAllFieldsCorrectly() {

        // Arrange
        Auction auctionDouble = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);

        Bid bidDouble = mock(Bid.class);
        BidId bidIdDouble = mock(BidId.class);
        UserId userIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);

        Instant bidDate = Instant.now();

        when(auctionDouble.identity()).thenReturn(auctionIdDouble);
        when(auctionIdDouble.toString()).thenReturn("A1");

        when(bidDouble.identity()).thenReturn(bidIdDouble);
        when(bidIdDouble.toString()).thenReturn("B123");

        when(bidDouble.getUserId()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user@example.com");

        when(bidDouble.getOfferPrice()).thenReturn(priceDouble);
        when(priceDouble.getValue()).thenReturn(14.50);
        when(priceDouble.getCurrency()).thenReturn(MITELOVERS.domain.valueobject.Currency.EUR);

        when(bidDouble.getBidDate()).thenReturn(bidDate);

        // SUT
        BidResponseDTOMapper mapper = new BidResponseDTOMapper();

        // Act
        BidResponseDTO dto = mapper.toDTO(auctionDouble, bidDouble);

        // assert
        assertEquals("B123", dto.getBidId());
        assertEquals("A1", dto.getAuctionId());
        assertEquals("user@example.com", dto.getBuyerId());
        assertEquals(14.50, dto.getOfferPrice());
        assertEquals("EUR", dto.getCurrency());
        assertEquals(bidDate, dto.getBidDate());
    }
}