package MITELOVERS.mapper;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.AuctionNoPriceResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuctionNoPriceResponseDTOMapperTest {

    @Test
    void shouldCorrectlyConvertAllFieldsToAuctionNoPriceResponseDTO() {
        Auction auctionDouble = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);

        ZonedDateTime startDate = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endDate = ZonedDateTime.now().plusDays(2);

        UserId sellerDouble = mock(UserId.class);

        when(auctionDouble.identity()).thenReturn(auctionIdDouble);
        when(auctionIdDouble.toString()).thenReturn("A1");
        when(auctionDouble.getAuctionStartDate()).thenReturn(startDate.toInstant());
        when(auctionDouble.getAuctionEndDate()).thenReturn(endDate.toInstant());

        when(auctionDouble.getSeller()).thenReturn(sellerDouble);

        // SUT
        AuctionNoPriceResponseDTOMapper mapper = new AuctionNoPriceResponseDTOMapper();

        // act
        AuctionNoPriceResponseDTO dto = mapper.toDTO(auctionDouble);

        // assert
        assertEquals("A1", dto.getAuctionId());
        assertEquals(startDate.toInstant(), dto.getStartDate());
        assertEquals(endDate.toInstant(), dto.getEndDate());
    }

}
