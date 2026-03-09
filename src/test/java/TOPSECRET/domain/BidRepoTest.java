package TOPSECRET.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidRepoTest {
    private BidRepo bidRepo;
    private BidFactory bidFactory;


    @BeforeEach
    void setUp() {
        bidRepo = new BidRepo(bidFactory);
    }

    //test constructor
    @Test
    void getHighestBidFail_EmptyBidList() {
        //assert
        Assertions.assertThrows(IllegalStateException.class, () -> bidRepo.getHighestBid());
    }

    @Test
    void getHighestBid_SingleBid() {
        //arrange
        Bid bid = mock(Bid.class);

        //act
        bidRepo.addBid(bid);
        when(bid.getOfferPrice()).thenReturn(new Price(100.0, Currency.EUR));
        double result = bidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(100, result);
    }

    @Test
    void getHighestBid_MultipleBids_firstHighest() {
        //arrange
        Bid bid1 = mock(Bid.class);
        Bid bid2 = mock(Bid.class);

        //act
        when(bid1.getOfferPrice()).thenReturn(new Price(105.0, Currency.EUR));
        when(bid2.getOfferPrice()).thenReturn(new Price(102.0, Currency.EUR));
        bidRepo.addBid(bid1);
        bidRepo.addBid(bid2);
        double result = bidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(105, result);
    }

    @Test
    void getHighestBid_MultipleBids_lastHighest() {
        //arrange
        Bid  bid1 = mock(Bid.class);
        Bid bid2 = mock(Bid.class);

        //act
        when(bid1.getOfferPrice()).thenReturn(new Price(100.0, Currency.EUR));
        when(bid2.getOfferPrice()).thenReturn(new Price(102.0, Currency.EUR));
        bidRepo.addBid(bid1);
        bidRepo.addBid(bid2);
        double result = bidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(102, result);

    }
}
