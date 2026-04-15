package TOPSECRET.domain.auction;

import TOPSECRET.domain.valueobject.BidId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;
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
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //act & SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //assert
        assertNotNull(bid);
        assertEquals(userIdDouble, bid.getUserId());
        assertEquals(offerPrice, bid.getOfferPrice());
        assertNotNull(bid.getBidDate());
    }

    @Test
    void shouldUseFixedClockWhenCreatingBid() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        //act
        Bid bid = new Bid(userIdDouble, offerPrice, fixedClock);

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
        UserId userIdDouble = mock(UserId.class);

        //act
        Exception exception = assertThrows(
                IllegalArgumentException.class, () -> new Bid(userIdDouble, null));

        //assert
        assertEquals("Offer Price cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenClockIsNull() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Bid(userIdDouble, offerPrice, null));

        //assert
        assertEquals("Clock cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectBidder() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //act
        UserId result = bid.getUserId();

        //assert
        assertEquals(userIdDouble, result);
    }

    @Test
    void shouldReturnCorrectOfferPrice() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //act
        Price result = bid.getOfferPrice();

        //assert
        assertEquals(offerPrice, result);
    }

    @Test
    void shouldReturnValidBidDate() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //act
        Instant bidDate = bid.getBidDate();

        //assert
        assertNotNull(bidDate);
    }

    @Test
    void shouldBeEqualToItself() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //act & SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //assert
        assertEquals(bid, bid);
    }

    @Test
    void shouldNotBeEqualToNull() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //act & SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //assert
        assertNotEquals(bid, null);
    }

    @Test
    void shouldNotBeEqualWhenPricesAreDifferent() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);
        Price otherPrice = mock(Price.class);

        //act & SUT
        Bid bid1 = new Bid(userIdDouble, offerPrice);
        Bid bid2 = new Bid(userIdDouble, otherPrice);

        //assert
        assertNotEquals(bid1, bid2);
    }

    @Test
    void shouldHaveSameHashCodeWhenObjectsAreEqual() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        //act
        Bid bid1 = new Bid(userIdDouble, offerPrice, fixedClock);
        Bid bid2 = new Bid(userIdDouble, offerPrice, fixedClock);

        //assert
        assertEquals(bid1, bid2);
        assertEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //act
        String notABid = "not a bid";

        //assert
        assertNotEquals(bid, notABid);
        assertFalse(bid.equals(notABid));
    }

    @Test
    void shouldReturnConsistentHashCode() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

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
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        when(userIdDouble.toString()).thenReturn("MockUser");
        when(offerPrice.toString()).thenReturn("100 EUR");

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //act
        String result = bid.toString();

        //assert
        assertNotNull(result);
        assertTrue(result.contains("Bid{"));
        assertTrue(result.contains("userId="));
        assertTrue(result.contains("offerPrice="));
        assertTrue(result.contains("date="));
    }

    @Test
    void shouldContainCorrectValuesInToString() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        when(userIdDouble.toString()).thenReturn("MockUser");
        when(offerPrice.toString()).thenReturn("100 EUR");

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //act
        String result = bid.toString();

        //assert
        assertTrue(result.contains("MockUser"));
        assertTrue(result.contains("100 EUR"));
    }

    @Test
    void shouldKeepBidImmutable() {

        //arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        //SUT
        Bid bid = new Bid(userIdDouble, offerPrice);

        //act
        UserId retrievedBidder = bid.getUserId();
        Price retrievedPrice = bid.getOfferPrice();
        Instant retrievedDate = bid.getBidDate();

        //assert
        assertSame(userIdDouble, retrievedBidder);
        assertSame(offerPrice, retrievedPrice);
        assertNotNull(retrievedDate);
    }

    @Test
    void equalBidsHaveSameHashCode() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        Instant fixedInstant = Instant.parse("2024-01-01T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());

        // Act
        Bid bid1 = new Bid(userIdDouble, offerPrice, fixedClock);
        Bid bid2 = new Bid(userIdDouble, offerPrice, fixedClock);

        // Assert
        assertEquals(bid1, bid2);
        assertEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void hashCodeChangesWhenBidderChanges() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        UserId userIdDouble2 = mock(UserId.class);
        Price price = mock(Price.class);

        Instant instant = Instant.parse("2024-01-01T10:15:30Z");
        Clock clock = Clock.fixed(instant, ZoneId.systemDefault());

        Bid bid1 = new Bid(userIdDouble, price, clock);
        Bid bid2 = new Bid(userIdDouble2, price, clock);

        // Act & Assert
        assertNotEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void identityIsNotNull() {

        //Arrange
        UserId userIdDouble = mock(UserId.class);
        Price offerPrice1 = mock(Price.class);

        //SUT
        Bid bid1 = new Bid(userIdDouble, offerPrice1);

        //Act
        BidId bidId = bid1.identity();

        //Assert
        assertNotNull(bidId);
    }

    @Test
    void identityIsAlwaysTheSame() {
        //Arrange
        UserId userIdDouble = mock(UserId.class);
        Price price  = mock(Price.class);


        //SUT
        Bid bid = new Bid(userIdDouble, price);

        //Act
        BidId bidId = bid.identity();
        BidId bidId2 = bid.identity();

        //Assert
        assertEquals(bidId, bidId2);
    }

    @Test
    void identityIsDifferentForDifferentBids() {

        //Arrange
        UserId userIdDouble = mock(UserId.class);
        UserId userIdDouble2 = mock(UserId.class);
        Price price1 = mock(Price.class);
        Price price2 = mock(Price.class);

        //SUT
        Bid bid1 = new Bid(userIdDouble, price1);
        Bid bid2 = new Bid(userIdDouble2, price2);

        //Act
        BidId bidId1 = bid1.identity();
        BidId bidId2 = bid2.identity();

        //Assert
        assertNotEquals(bidId1, bidId2);
    }

    @Test
    void returnFalseWhenBidsAreNotSame() {

        //Arrange
        UserId userIdDouble = mock(UserId.class);
        Price price = mock(Price.class);

        //SUT
        Bid bid1 = new Bid(userIdDouble, price);
        Bid bid2 = new Bid(userIdDouble, price);

        //Act + Assert
        assertFalse(bid1.sameAs(bid2));
    }

    @Test
    void returnTrueWhenBidsAreSame() {

        //Arrange
        UserId userIdDouble = mock(UserId.class);
        Price price = mock(Price.class);

        //SUT
        Bid bid1 = new Bid(userIdDouble, price);

        //Act + Assert
        assertTrue(bid1.sameAs(bid1));
    }

    @Test
    void returnFalseWhenBidIsNull () {

        //Arrange
        UserId userIdDouble = mock(UserId.class);
        Price price = mock(Price.class);

        //SUT
        Bid bid1 = new Bid(userIdDouble, price);

        //Act + Assert
        assertFalse(bid1.sameAs(null));
    }

    @Test
    void returnFalseWhenObjectIsDifferentType() {

        //Arrange
        UserId userIdDouble = mock(UserId.class);
        Price price = mock(Price.class);

        //SUT
        Bid bid1 = new Bid(userIdDouble, price);

        //Act + Assert
        String bid2 = "Bid";
        assertFalse(bid1.sameAs(bid2));
    }
}