package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import MITELOVERS.persistence.jpa.datamodel.BidDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionAssemblerTest {

    @Test
    void shouldReturnAuctionDataModel() {
        //Arrange
        BidAssembler bidAssemblerDouble = mock(BidAssembler.class);
        AuctionFactory auctionFactoryDouble = mock(AuctionFactory.class);
        Auction auction = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);
        Price startingPriceDouble = mock(Price.class);
        when(startingPriceDouble.getValue()).thenReturn(100.0);
        when(startingPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        Price reservePriceDouble = mock(Price.class);
        when(reservePriceDouble.getValue()).thenReturn(100.0);
        when(reservePriceDouble.getCurrency()).thenReturn(Currency.EUR);
        Price outrightPriceDouble = mock(Price.class);
        when(outrightPriceDouble.getValue()).thenReturn(100.0);
        when(outrightPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        Instant auctionStartDateDouble = Instant.now();
        Instant auctionEndDateDouble = Instant.now();
        UserId userIdDouble = mock(UserId.class);
        Price finalPrice = mock(Price.class);
        when(finalPrice.getValue()).thenReturn(100.0);
        when(finalPrice.getCurrency()).thenReturn(Currency.EUR);
        Bid bid1Double = mock(Bid.class);
        Bid bid2Double = mock(Bid.class);
        List<Bid> bidsDouble = List.of(bid1Double, bid2Double);

        when(auction.identity()).thenReturn(auctionIdDouble);
        when(auction.getStartingPrice()).thenReturn(startingPriceDouble);
        when(auction.getReservePrice()).thenReturn(reservePriceDouble);
        when(auction.getOutrightPrice()).thenReturn(outrightPriceDouble);
        when(auction.getAuctionStartDate()).thenReturn(auctionStartDateDouble);
        when(auction.getAuctionEndDate()).thenReturn(auctionEndDateDouble);
        when(auction.getUserId()).thenReturn(userIdDouble);
        when(auction.getFinalPrice()).thenReturn(finalPrice);
        when(auction.getBids()).thenReturn(bidsDouble);

        //SUT
        AuctionAssembler assembler = new AuctionAssembler(bidAssemblerDouble, auctionFactoryDouble);

        //Act
        AuctionDataModel result = assembler.toDataModel(auction);

        //Assert
        assertNotNull(result);
        assertEquals(100.0, result.getStartingPrice().getNumericValue());
        assertEquals("EUR", result.getStartingPrice().getCurrency());
        assertEquals(auctionStartDateDouble, result.getAuctionStartDate());
        assertEquals(auctionEndDateDouble, result.getAuctionEndDate());
        assertEquals(2, result.getBids().size());
    }

    @Test
    void shouldReturnAuctionDomain() {
        //Arrange
        BidAssembler bidAssemblerDouble = mock(BidAssembler.class);
        AuctionFactory auctionFactoryDouble = new AuctionFactory();
        AuctionDataModel auctionDm = mock(AuctionDataModel.class);
        List<String> itensIdDouble = List.of("A1B2C3D4E5", "F6A7B8C9D0");
        PriceDataModel startingPriceDm = mock(PriceDataModel.class);
        PriceDataModel reservePriceDm = mock(PriceDataModel.class);
        PriceDataModel outrightPriceDm = mock(PriceDataModel.class);
        PriceDataModel finalPriceDm = mock(PriceDataModel.class);
        BidDataModel bidDM1 = mock(BidDataModel.class);
        BidDataModel bidDM2 = mock(BidDataModel.class);
        List<BidDataModel> bidsDouble = new ArrayList<>();
        bidsDouble.add(bidDM1);
        bidsDouble.add(bidDM2);
        UserId sellerDouble = mock(UserId.class);

        when(auctionDm.getAuctionId()).thenReturn("AU-1234ABCD");
        when(auctionDm.getItemsId()).thenReturn(itensIdDouble);
        when(auctionDm.getAuctionStartDate()).thenReturn(Instant.now());
        when(auctionDm.getAuctionEndDate()).thenReturn(Instant.now());
        when(startingPriceDm.getNumericValue()).thenReturn(100.0);
        when(startingPriceDm.getCurrency()).thenReturn("EUR");
        when(auctionDm.getStartingPrice()).thenReturn(startingPriceDm);
        when(reservePriceDm.getNumericValue()).thenReturn(100.0);
        when(reservePriceDm.getCurrency()).thenReturn("EUR");
        when(auctionDm.getReservePrice()).thenReturn(reservePriceDm);
        when(outrightPriceDm.getNumericValue()).thenReturn(100.0);
        when(outrightPriceDm.getCurrency()).thenReturn("EUR");
        when(auctionDm.getOutrightPrice()).thenReturn(outrightPriceDm);
        when(auctionDm.getAuctionStartDate()).thenReturn(Instant.now());
        when(auctionDm.getAuctionEndDate()).thenReturn(Instant.now());
        when(auctionDm.getUserId()).thenReturn("teste@email.com");
        when(finalPriceDm.getNumericValue()).thenReturn(100.0);
        when(finalPriceDm.getCurrency()).thenReturn("EUR");
        when(auctionDm.getFinalPrice()).thenReturn(finalPriceDm);
        when(auctionDm.getBids()).thenReturn(bidsDouble);
        when(sellerDouble.toString()).thenReturn("pedro@aeiou.com");
        when(auctionDm.getSeller()).thenReturn("pedro@aeiou.com");

        //SUT
        AuctionAssembler assembler = new AuctionAssembler(bidAssemblerDouble, auctionFactoryDouble);

        //Act
        Auction result = assembler.toDomain(auctionDm);

        //Assert
        assertNotNull(result);
        assertEquals(100.0, result.getStartingPrice().getValue());
        assertEquals(Currency.EUR, result.getStartingPrice().getCurrency());
        assertEquals(100.0, result.getReservePrice().getValue());
        assertEquals(Currency.EUR, result.getReservePrice().getCurrency());
        assertEquals(2, result.getItemsId().size());
    }

    @Test
    void shouldReturnAuctionDataModelWithNullOptionalFields() {
        // Arrange
        BidAssembler bidAssemblerDouble = mock(BidAssembler.class);
        AuctionFactory auctionFactoryDouble = mock(AuctionFactory.class);
        Auction auction = mock(Auction.class);
        AuctionId auctionIdDouble = mock(AuctionId.class);
        Price startingPriceDouble = mock(Price.class);
        when(startingPriceDouble.getValue()).thenReturn(100.0);
        when(startingPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        Price reservePriceDouble = mock(Price.class);
        when(reservePriceDouble.getValue()).thenReturn(100.0);
        when(reservePriceDouble.getCurrency()).thenReturn(Currency.EUR);

        when(auction.identity()).thenReturn(auctionIdDouble);
        when(auction.getStartingPrice()).thenReturn(startingPriceDouble);
        when(auction.getReservePrice()).thenReturn(reservePriceDouble);
        when(auction.getOutrightPrice()).thenReturn(null);
        when(auction.getFinalPrice()).thenReturn(null);
        when(auction.getUserId()).thenReturn(null);
        when(auction.getBids()).thenReturn(List.of());
        when(auction.getItemsId()).thenReturn(List.of());
        when(auction.getAuctionStartDate()).thenReturn(Instant.now());
        when(auction.getAuctionEndDate()).thenReturn(Instant.now());

        AuctionAssembler sut = new AuctionAssembler(bidAssemblerDouble, auctionFactoryDouble);

        // Act
        AuctionDataModel result = sut.toDataModel(auction);

        // Assert
        assertNotNull(result);
        assertNull(result.getOutrightPrice());
        assertNull(result.getFinalPrice());
        assertNull(result.getUserId());
    }

    @Test
    void shouldReturnAuctionDomainWithNullOptionalFields() {
        // Arrange
        BidAssembler bidAssemblerDouble = mock(BidAssembler.class);
        AuctionFactory auctionFactoryDouble = new AuctionFactory();
        AuctionDataModel auctionDm = mock(AuctionDataModel.class);
        PriceDataModel startingPriceDm = mock(PriceDataModel.class);
        PriceDataModel reservePriceDm = mock(PriceDataModel.class);
        UserId sellerDouble = mock(UserId.class);

        when(auctionDm.getAuctionId()).thenReturn("AU-1234ABCD");
        when(auctionDm.getItemsId()).thenReturn(List.of());
        when(auctionDm.getAuctionStartDate()).thenReturn(Instant.now());
        when(auctionDm.getAuctionEndDate()).thenReturn(Instant.now());
        when(startingPriceDm.getNumericValue()).thenReturn(100.0);
        when(startingPriceDm.getCurrency()).thenReturn("EUR");
        when(auctionDm.getStartingPrice()).thenReturn(startingPriceDm);
        when(reservePriceDm.getNumericValue()).thenReturn(100.0);
        when(reservePriceDm.getCurrency()).thenReturn("EUR");
        when(auctionDm.getReservePrice()).thenReturn(reservePriceDm);
        when(auctionDm.getOutrightPrice()).thenReturn(null);
        when(auctionDm.getFinalPrice()).thenReturn(null);
        when(auctionDm.getUserId()).thenReturn(null);
        when(auctionDm.getBids()).thenReturn(List.of());
        when(auctionDm.getItemsId()).thenReturn(List.of("A1B2C3D4E5"));
        when(sellerDouble.toString()).thenReturn("pedro@aeiou.com");
        when(auctionDm.getSeller()).thenReturn("pedro@aeiou.com");

        AuctionAssembler sut = new AuctionAssembler(bidAssemblerDouble, auctionFactoryDouble);

        // Act
        Auction result = sut.toDomain(auctionDm);

        // Assert
        assertNotNull(result);
        assertNull(result.getOutrightPrice());
        assertNull(result.getFinalPrice());
        assertNull(result.getUserId());
    }
}