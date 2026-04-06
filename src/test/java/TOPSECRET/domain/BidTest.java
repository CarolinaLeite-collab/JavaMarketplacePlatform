package TOPSECRET.domain;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidTest {

    @Test
    void shouldCreateValidBid() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //act & SUT
        Bid bid = new Bid(bidder, offerPrice);

        //assert
        assertNotNull(bid);
        assertEquals(bidder, bid.getBidder());
        assertEquals(offerPrice, bid.getOfferPrice());
        assertNotNull(bid.getBidDate());
    }

    @Test
    void shouldUseFixedClockWhenCreatingBid() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        //act
        Bid bid = new Bid(bidder, offerPrice, fixedClock);

        //assert
        assertEquals(fixedInstant, bid.getBidDate());
    }

    @Test
    void shouldThrowExceptionWhenBidderIsNull() {

        //arrange
        Price offerPrice = mock(Price.class);

        //act
        Exception exception = assertThrows(
                IllegalArgumentException.class, () -> new Bid(null, offerPrice));

        //assert
        assertEquals("Bidder cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOfferPriceIsNull() {

        //arrange
        User bidder = mock(User.class);

        //act
        Exception exception = assertThrows(
                IllegalArgumentException.class, () -> new Bid(bidder, null));

        //assert
        assertEquals("Offer Price cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenClockIsNull() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Bid(bidder, offerPrice, null));

        //assert
        assertEquals("Clock cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectBidder() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        User result = bid.getBidder();

        //assert
        assertEquals(bidder, result);
    }

    @Test
    void shouldReturnCorrectOfferPrice() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        Price result = bid.getOfferPrice();

        //assert
        assertEquals(offerPrice, result);
    }

    @Test
    void shouldReturnValidBidDate() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        Instant bidDate = bid.getBidDate();

        //assert
        assertNotNull(bidDate);
    }

    @Test
    void shouldBeEqualToItself() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //act & SUT
        Bid bid = new Bid(bidder, offerPrice);

        //assert
        assertEquals(bid, bid);
    }

    @Test
    void shouldNotBeEqualToNull() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //act & SUT
        Bid bid = new Bid(bidder, offerPrice);

        //assert
        assertNotEquals(bid, null);
    }

    @Test
    void shouldNotBeEqualWhenPricesAreDifferent() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);
        Price otherPrice = mock(Price.class);

        //act & SUT
        Bid bid1 = new Bid(bidder, offerPrice);
        Bid bid2 = new Bid(bidder, otherPrice);

        //assert
        assertNotEquals(bid1, bid2);
    }

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        //act
        Bid bid1 = new Bid(bidder, offerPrice, fixedClock);
        Bid bid2 = new Bid(bidder, offerPrice, fixedClock);

        //assert
        assertEquals(bid1, bid2);
        assertEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        String notABid = "not a bid";

        //assert
        assertNotEquals(bid, notABid);
        assertFalse(bid.equals(notABid));
    }

    @Test
    void shouldReturnConsistentHashCode() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        int hash1 = bid.hashCode();
        int hash2 = bid.hashCode();
        int hash3 = bid.hashCode();

        //assert
        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
        assertEquals(hash1, hash3);
    }

    @Test
    void shouldContainExpectedFieldsInToString() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        when(bidder.toString()).thenReturn("MockUser");
        when(offerPrice.toString()).thenReturn("100 EUR");

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        String result = bid.toString();

        //assert
        assertNotNull(result);
        assertTrue(result.contains("Bid{"));
        assertTrue(result.contains("bidder="));
        assertTrue(result.contains("offerPrice="));
        assertTrue(result.contains("date="));
    }

    @Test
    void shouldContainCorrectValuesInToString() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        when(bidder.toString()).thenReturn("MockUser");
        when(offerPrice.toString()).thenReturn("100 EUR");

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        String result = bid.toString();

        //assert
        assertTrue(result.contains("MockUser"));
        assertTrue(result.contains("100 EUR"));
    }

    @Test
    void shouldKeepBidImmutable() {

        //arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(bidder, offerPrice);

        //act
        User retrievedBidder = bid.getBidder();
        Price retrievedPrice = bid.getOfferPrice();
        Instant retrievedDate = bid.getBidDate();

        //assert
        assertSame(bidder, retrievedBidder);
        assertSame(offerPrice, retrievedPrice);
        assertNotNull(retrievedDate);
    }

    @Test
    void equalBidsHaveSameHashCode() {
        // Arrange
        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        // Act
        Bid bid1 = new Bid(bidder, offerPrice, fixedClock);
        Bid bid2 = new Bid(bidder, offerPrice, fixedClock);

        // Assert
        assertEquals(bid1, bid2);
        assertEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void hashCodeChangesWhenBidderChanges() {
        // Arrange
        User bidder1 = mock(User.class);
        User bidder2 = mock(User.class);
        Price price = mock(Price.class);

        Instant instant = Instant.parse("2024-01-01T10:15:30Z");
        Clock clock = Clock.fixed(instant, ZoneId.systemDefault());

        Bid bid1 = new Bid(bidder1, price, clock);
        Bid bid2 = new Bid(bidder2, price, clock);

        // Act & Assert
        assertNotEquals(bid1.hashCode(), bid2.hashCode());
    }

}