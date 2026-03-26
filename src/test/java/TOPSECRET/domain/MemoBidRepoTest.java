package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Currency;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoBidRepoTest {
    private BidFactory _bidFactoryDouble;

    @BeforeEach
    void setUp() {

        _bidFactoryDouble = mock(BidFactory.class);

    }

    @Test
    void shouldSuccessfullyCreateBid(){
        //arrange
        _bidFactoryDouble = mock(BidFactory.class);

        //SUT
        MemoBidRepo _memoBidRepo = new MemoBidRepo(_bidFactoryDouble);

        User bidderDouble = mock(User.class);
        Price offerPriceDouble = mock(Price.class);
        Bid expectedBidDouble = mock(Bid.class);

        when(_bidFactoryDouble.createBid(bidderDouble, offerPriceDouble))
                .thenReturn(expectedBidDouble);

        // act
        Bid bid = _memoBidRepo.createBid(bidderDouble, offerPriceDouble);

        // assert
        assertNotNull(bid);
    }

    @Test
    void getHighestBidFail_EmptyBidList() {
        //arrange
        _bidFactoryDouble = mock(BidFactory.class);

        //SUT
        MemoBidRepo _memoBidRepo = new MemoBidRepo(_bidFactoryDouble);

        //act + assert
        Assertions.assertThrows(IllegalStateException.class, () -> _memoBidRepo.getHighestBid());
    }

    @Test
    void getHighestBid_SingleBid() {
        //arrange
        _bidFactoryDouble = mock(BidFactory.class);

        //SUT
        MemoBidRepo _memoBidRepo = new MemoBidRepo(_bidFactoryDouble);

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(100.0);

        Bid bidDouble = mock(Bid.class);
        when(bidDouble.getOfferPrice()).thenReturn(priceDouble);

        //act
        _memoBidRepo.addBid(bidDouble);

        double result = _memoBidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertNotNull(bidDouble);
        assertEquals(100, result);
    }

    @Test
    void getHighestBid_MultipleBids_firstHighest() {
        //arrange
        _bidFactoryDouble = mock(BidFactory.class);
        Bid bidDouble1 = mock(Bid.class);
        Bid bidDouble2 = mock(Bid.class);
        Price priceDouble1 = mock(Price.class);
        Price priceDouble2 = mock(Price.class);

        when(priceDouble1.getValue()).thenReturn(105.0);
        when(bidDouble1.getOfferPrice()).thenReturn(priceDouble1);
        when(bidDouble2.getOfferPrice()).thenReturn(priceDouble1);

        // SUT
        MemoBidRepo memoBidRepo = new MemoBidRepo(_bidFactoryDouble);

        // Act
        memoBidRepo.addBid(bidDouble1);
        memoBidRepo.addBid(bidDouble2);

        Bid result = memoBidRepo.getHighestBid();

        //assert
        assertEquals(bidDouble1, result);
    }

    @Test
    void getHighestBid_MultipleBids_lastHighest() {
        //arrange
        _bidFactoryDouble = mock(BidFactory.class);

        //SUT
        MemoBidRepo _memoBidRepo = new MemoBidRepo(_bidFactoryDouble);

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
        _memoBidRepo.addBid(bidDouble1);
        _memoBidRepo.addBid(bidDouble2);
        double result = _memoBidRepo.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(102, result);
    }
}
