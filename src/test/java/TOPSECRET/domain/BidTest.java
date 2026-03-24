package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BidTest {

    @Test
    void shouldCreateValidBid() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        assertNotNull(bid);
        assertEquals(bidder, bid.getBidder());
        assertEquals(offerPrice, bid.getOfferPrice());
        assertNotNull(bid.getBidDate());
    }

    @Test
    void shouldUseFixedClockWhenCreatingBid() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        Bid bid = new Bid(bidder, offerPrice, fixedClock);

        assertEquals(fixedInstant, bid.getBidDate());
    }

    @Test
    void shouldThrowExceptionWhenBidderIsNull() {

        Price offerPrice = mock(Price.class);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(null, offerPrice)
        );

        assertEquals("Bidder cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOfferPriceIsNull() {

        User bidder = mock(User.class);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(bidder, null)
        );

        assertEquals("Offer Price cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenClockIsNull() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bid(bidder, offerPrice, null)
        );

        assertEquals("Clock cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectBidder() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        User result = bid.getBidder();

        assertEquals(bidder, result);
    }

    @Test
    void shouldReturnCorrectOfferPrice() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        Price result = bid.getOfferPrice();

        assertEquals(offerPrice, result);
    }

    @Test
    void shouldReturnValidBidDate() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        Instant bidDate = bid.getBidDate();

        assertNotNull(bidDate);
    }

    @Test
    void shouldBeEqualToItself() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        assertEquals(bid, bid);
    }

    @Test
    void shouldNotBeEqualToNull() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        assertNotEquals(bid, null);
    }

    @Test
    void shouldNotBeEqualWhenPricesAreDifferent() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);
        Price otherPrice = mock(Price.class);

        Bid bid1 = new Bid(bidder, offerPrice);
        Bid bid2 = new Bid(bidder, otherPrice);

        assertNotEquals(bid1, bid2);
    }

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        Bid bid1 = new Bid(bidder, offerPrice, fixedClock);
        Bid bid2 = new Bid(bidder, offerPrice, fixedClock);

        assertEquals(bid1, bid2);
        assertEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        String notABid = "not a bid";

        assertNotEquals(bid, notABid);
        assertFalse(bid.equals(notABid));
    }

    @Test
    void shouldReturnConsistentHashCode() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        int hash1 = bid.hashCode();
        int hash2 = bid.hashCode();
        int hash3 = bid.hashCode();

        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);
        assertEquals(hash1, hash3);
    }

    @Test
    void shouldContainExpectedFieldsInToString() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        when(bidder.toString()).thenReturn("MockUser");
        when(offerPrice.toString()).thenReturn("100 EUR");

        Bid bid = new Bid(bidder, offerPrice);

        String result = bid.toString();

        assertNotNull(result);
        assertTrue(result.contains("Bid{"));
        assertTrue(result.contains("bidder="));
        assertTrue(result.contains("offerPrice="));
        assertTrue(result.contains("date="));
    }

    @Test
    void shouldContainCorrectValuesInToString() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        when(bidder.toString()).thenReturn("MockUser");
        when(offerPrice.toString()).thenReturn("100 EUR");

        Bid bid = new Bid(bidder, offerPrice);

        String result = bid.toString();

        assertTrue(result.contains("MockUser"));
        assertTrue(result.contains("100 EUR"));
    }

    @Test
    void shouldKeepBidImmutable() {

        User bidder = mock(User.class);
        Price offerPrice = mock(Price.class);

        Bid bid = new Bid(bidder, offerPrice);

        User retrievedBidder = bid.getBidder();
        Price retrievedPrice = bid.getOfferPrice();
        Instant retrievedDate = bid.getBidDate();

        assertSame(bidder, retrievedBidder);
        assertSame(offerPrice, retrievedPrice);
        assertNotNull(retrievedDate);
    }
}