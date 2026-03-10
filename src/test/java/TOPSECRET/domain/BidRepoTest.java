package TOPSECRET.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidRepoTest {
    private BidRepo bidRepo;
    private BidFactory bidFactoryDouble;


    //test constructor
    @Test
    void getHighestBidFail_EmptyBidList() {
        //arrange
        bidFactoryDouble = mock(BidFactory.class);
        bidRepo = new BidRepo(bidFactoryDouble);

        //act + assert
        Assertions.assertThrows(IllegalStateException.class, () -> bidRepo.getHighestBid());
    }

    @Test
    void getHighestBid_SingleBid() {
        //arrange
        bidFactoryDouble = mock(BidFactory.class);
        bidRepo = new BidRepo(bidFactoryDouble);

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(100.0);

        Bid bidDouble = mock(Bid.class);
        when(bidDouble.getOfferPrice()).thenReturn(priceDouble);

        //act
        bidRepo.addBid(bidDouble);

        double result = bidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(100, result);
    }

    @Test
    void getHighestBid_MultipleBids_firstHighest() {
        //arrange
        bidFactoryDouble = mock(BidFactory.class);
        bidRepo = new BidRepo(bidFactoryDouble);

        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble1.getValue()).thenReturn(105.0);


        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble2.getValue()).thenReturn(102.0);

        Bid bidDouble1 = mock(Bid.class);
        when(bidDouble1.getOfferPrice()).thenReturn(priceDouble1);

        Bid bidDouble2 = mock(Bid.class);
        when(bidDouble2.getOfferPrice()).thenReturn(priceDouble2);

        //act
        bidRepo.addBid(bidDouble1);
        bidRepo.addBid(bidDouble2);
        double result = bidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(105, result);
    }

    @Test
    void getHighestBid_MultipleBids_lastHighest() {
        //arrange
        bidFactoryDouble = mock(BidFactory.class);
        bidRepo = new BidRepo(bidFactoryDouble);

        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble1.getValue()).thenReturn(100.0);


        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getCurrency()).thenReturn(Currency.EUR);
        when(priceDouble2.getValue()).thenReturn(102.0);

        Bid bidDouble1 = mock(Bid.class);
        when(bidDouble1.getOfferPrice()).thenReturn(priceDouble1);

        Bid bidDouble2 = mock(Bid.class);
        when(bidDouble2.getOfferPrice()).thenReturn(priceDouble2);

        //act
        bidRepo.addBid(bidDouble1);
        bidRepo.addBid(bidDouble2);
        double result = bidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(102, result);

    }
}
