package TOPSECRET.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BidTest {

    @Test
    public void test_valid_bid_creation() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-200", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price offerPrice = new Price(100.0, Currency.EUR);

        //act
        Bid bid = new Bid(bidder, offerPrice);

        // assert
        assertNotNull(bid);                             //Bid Created?
        assertEquals(bidder, bid.getBidder());          //Correct bidder?
        assertEquals(offerPrice, bid.getOfferPrice());  //Price correct?
        assertNotNull(bid.getBidDate());                //Date defined?
    }

    @Test
    void test_null_bidder_throws_exception() {

        // arrange
        User bidder = null;
        Price offerPrice = new Price(100.0, Currency.EUR);

        // act and assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Bid(bidder, offerPrice));
        assertEquals("Bidder cannot be null", exception.getMessage());

    }

    @Test
    void test_null_offer_prices_throws_exception() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price offerPrice = null;

        // act and assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Bid(bidder, offerPrice));
        assertEquals("Offer Price cannot be null", exception.getMessage());
    }


    // Test getBidder returns correct user
    @Test
    void test_getBidder() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price offerPrice = new Price(150.0, Currency.EUR);
        Bid bid = new Bid(bidder, offerPrice);

        // act
        User result = bid.getBidder();

        // assert
        assertEquals(bidder, result);
    }

    // Test getOfferPrice returns correct price
    @Test
    void test_getOfferPrice() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price offerPrice = new Price(250.0, Currency.EUR);
        Bid bid = new Bid(bidder, offerPrice);

        // act
        Price result = bid.getOfferPrice();

        // assert
        assertEquals(offerPrice, result);
    }

    // Test getBidDate returns valid timestamp
    @Test
    void test_getBidDate_is_set() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price offerPrice = new Price(100.0, Currency.EUR);

        // act
        Bid bid = new Bid(bidder, offerPrice);
        LocalDateTime bidDate = bid.getBidDate();

        // assert
        assertNotNull(bidDate);

    }

    @Test
    void test_getBidDate_is_recent() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price offerPrice = new Price(100.0, Currency.EUR);
        LocalDateTime beforeCreation = LocalDateTime.now();

        // act
        Bid bid = new Bid(bidder, offerPrice);
        LocalDateTime bidDate = bid.getBidDate();
        LocalDateTime afterCreation = LocalDateTime.now();

        // assert - verify bidDate is between before and after creation
        assertNotNull(bidDate);
        assertTrue(bidDate.isAfter(beforeCreation.minusSeconds(1)) || bidDate.isEqual(beforeCreation));
        assertTrue(bidDate.isBefore(afterCreation.plusSeconds(1)) || bidDate.isEqual(afterCreation));
        // Verify it's within a reasonable time window (2 seconds)
        assertTrue(ChronoUnit.SECONDS.between(beforeCreation, bidDate) <= 2);
        assertTrue(ChronoUnit.SECONDS.between(bidDate, afterCreation) <= 2);
    }



    @Test
    public void test_different_bidders_can_bid() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-420", null);
        Address address2 = new Address("Rua de S. Gonçalo", "Porto", Address.BuildingType.APARTMENT, "Porto", "Porto", Address.Country.PORTUGAL, "1269-400", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        Phone phoneNumber2 = new Phone(prefix1, "919999991");
        User bidder1 = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        User bidder2 = new User(new Name("Leitor"), address1, new Email("example@exampling.com"), phoneNumber2);
        Price price1 = new Price(100.0, Currency.EUR);
        Price price2 = new Price(150.0, Currency.EUR);

        // act
        Bid bid1 = new Bid(bidder1, price1);
        Bid bid2 = new Bid(bidder2, price2);

        // assert
        assertEquals(bidder1, bid1.getBidder());
        assertEquals(bidder2, bid2.getBidder());
        assertNotEquals(bid1.getBidder(), bid2.getBidder());
    }


    @Test
    void test_same_bidder_multiple_bids() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-420", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price price1 = new Price(100.0, Currency.EUR);
        Price price2 = new Price(150.0, Currency.EUR);

        // act
        Bid bid1 = new Bid(bidder, price1);
        Bid bid2 = new Bid(bidder, price2);

        // assert
        assertEquals(bidder, bid1.getBidder());
        assertEquals(bidder, bid2.getBidder());
        assertNotEquals(bid1.getOfferPrice(), bid2.getOfferPrice());
    }

    @Test
    void test_bids_with_different_currencies() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-420", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User(new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price priceEUR = new Price(100.0, Currency.EUR);
        Price priceUSD = new Price(100.0, Currency.USD);
        Price priceGBP = new Price(100.0, Currency.GBP);

        // act
        Bid bidEUR = new Bid(bidder, priceEUR);
        Bid bidUSD = new Bid(bidder, priceUSD);
        Bid bidGBP = new Bid(bidder, priceGBP);

        // assert
        assertEquals(Currency.EUR, bidEUR.getOfferPrice().getCurrency());
        assertEquals(Currency.USD, bidUSD.getOfferPrice().getCurrency());
        assertEquals(Currency.GBP, bidGBP.getOfferPrice().getCurrency());
        assertNotEquals(bidEUR.getOfferPrice(), bidUSD.getOfferPrice());

    }

    @Test
    void test_bid_with_very_large_price() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price largePrice = new Price(Double.MAX_VALUE / 2, Currency.EUR);

        // act
        Bid bid = new Bid(bidder, largePrice);

        // assert
        assertNotNull(bid);
        assertEquals(largePrice, bid.getOfferPrice());
        assertEquals(bidder, bid.getBidder());
        assertNotNull(bid.getBidDate());
    }

    @Test
    void test_bid_with_small_price() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price smallPrice = new Price(0.01, Currency.EUR);

        // act
        Bid bid = new Bid(bidder, smallPrice);

        // assert
        assertNotNull(bid);
        assertEquals(smallPrice, bid.getOfferPrice());
        assertEquals(bidder, bid.getBidder());
        assertNotNull(bid.getBidDate());
    }



    @Test
    void test_equals_same_object() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act & assert
        assertEquals(bid, bid);
    }



    @Test
    void test_equals_null() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act & assert
        assertNotEquals(bid, null);
    }

    @Test
    void test_equals_different_bidder() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone1 = new Phone(prefix, "919999999");
        Phone phone2 = new Phone(prefix, "919999998");
        User bidder1 = new User(new Name("Reader"), address, new Email("reader@email.com"), phone1);
        User bidder2 = new User(new Name("Writer"), address, new Email("writer@email.com"), phone2);
        Price price = new Price(100.0, Currency.EUR);

        Bid bid1 = new Bid(bidder1, price);
        Bid bid2 = new Bid(bidder2, price);

        // act & assert
        assertNotEquals(bid1, bid2);
    }

    @Test
    void test_equals_different_price() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price1 = new Price(100.0, Currency.EUR);
        Price price2 = new Price(200.0, Currency.EUR);

        Bid bid1 = new Bid(bidder, price1);
        Bid bid2 = new Bid(bidder, price2);

        // act & assert
        assertNotEquals(bid1, bid2);
    }

    @Test
    void test_equals_different_class_type() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);
        String notABid = "not a bid";

        // act & assert
        assertNotEquals(bid, notABid);
        assertFalse(bid.equals(notABid));
    }

    @Test
    void test_equals_and_hashCode() {
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE,
                "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);

        Bid bid1 = new Bid(bidder, price);
        Bid bid2 = new Bid(bidder, price);

        // Equals tests
        assertEquals(bid1, bid1);          // same object
        assertNotEquals(bid1, null);       // null
        assertNotEquals(bid1, "string");   // different class

        // HashCode consistency
        int hash1 = bid1.hashCode();
        int hash2 = bid1.hashCode();
        assertEquals(hash1, hash2);        // repeated calls consistent

        // HashCode for different objects
        int hashBid2 = bid2.hashCode();
        assertNotEquals(hash1, hashBid2);

    }

    @Test
    void test_hashCode_different_bids() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone1 = new Phone(prefix, "919999999");
        Phone phone2 = new Phone(prefix, "919999998");
        User bidder1 = new User(new Name("Reader"), address, new Email("reader@email.com"), phone1);
        User bidder2 = new User(new Name("Writer"), address, new Email("writer@email.com"), phone2);
        Price price = new Price(100.0, Currency.EUR);

        Bid bid1 = new Bid(bidder1, price);
        Bid bid2 = new Bid(bidder2, price);

        // act & assert
        assertNotEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void test_hashCode_consistency_multiple_calls() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act - call hashCode multiple times
        int hashCode1 = bid.hashCode();
        int hashCode2 = bid.hashCode();
        int hashCode3 = bid.hashCode();

        // assert - hashCode should be consistent
        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode2, hashCode3);
        assertEquals(hashCode1, hashCode3);
    }


    @Test
    void test_toString_format() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act
        String result = bid.toString();

        // assert
        assertNotNull(result);
        assertTrue(result.contains("Bid{"));
        assertTrue(result.contains("bidder="));
        assertTrue(result.contains("offerPrice="));
        assertTrue(result.contains("date="));
    }

    @Test
    void test_toString_contains_correct_values() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act
        String result = bid.toString();

        // assert
        assertTrue(result.contains(bidder.toString()));
        assertTrue(result.contains(price.toString()));
    }

    @Test
    void test_toString_date_format_using_DateTimeFormatter() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address,
                new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act
        String result = bid.toString();

        // assert
        assertNotNull(result);

        int dateStart = result.indexOf("date=");
        assertTrue(dateStart > 0, "toString should contain 'date='");

        String datePart = result.substring(dateStart + 5, result.length() - 1);

        // DateTimeFormatter with expected format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        assertDoesNotThrow(() -> {
            LocalDateTime.parse(datePart, formatter);
        }, "Date format should be dd/MM/yyyy HH:mm:ss, but was: " + datePart);
    }

    @Test
    void test_toString_complete_structure() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act
        String result = bid.toString();

        // assert - verify complete structure
        assertNotNull(result);
        assertTrue(result.startsWith("Bid{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains("bidder="));
        assertTrue(result.contains("offerPrice="));
        assertTrue(result.contains("date="));
        // Verify all three fields are present
        int bidderIndex = result.indexOf("bidder=");
        int offerPriceIndex = result.indexOf("offerPrice=");
        int dateIndex = result.indexOf("date=");
        assertTrue(bidderIndex < offerPriceIndex && offerPriceIndex < dateIndex,
                "Fields should appear in order: bidder, offerPrice, date");
    }

    @Test
    void test_bid_immutability() {
        // arrange
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-400", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phone = new Phone(prefix, "919999999");
        User bidder = new User(new Name("Reader"), address, new Email("reader@email.com"), phone);
        Price price = new Price(100.0, Currency.EUR);
        Bid bid = new Bid(bidder, price);

        // act
        User retrievedBidder = bid.getBidder();
        Price retrievedPrice = bid.getOfferPrice();
        LocalDateTime retrievedDate = bid.getBidDate();

        // assert - verify getters return the same objects (immutability)
        assertSame(bidder, retrievedBidder);
        assertSame(price, retrievedPrice);
        assertNotNull(retrievedDate); // LocalDateTime is immutable by design
    }
}