package TOPSECRET.domain;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BidTest {

    @Test
    public void test_valid_bid_creation() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-200", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
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
        User bidder = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
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
        User bidder = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
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
        User bidder = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
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
        User bidder = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        Price offerPrice = new Price(100.0, Currency.EUR);

        // act
        Bid bid = new Bid(bidder, offerPrice);
        LocalDateTime result = bid.getBidDate();

        // assert
        assertNotNull(result);
    }


    @Test
    public void test_different_users_can_bid() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-420", null);
        Address address2 = new Address("Rua de S. Gonçalo", "Porto", Address.BuildingType.APARTMENT, "Porto", "Porto", Address.Country.PORTUGAL, "1269-400", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        Phone phoneNumber2 = new Phone(prefix1, "919999991");
        User bidder1 = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
        User bidder2 = new User (new Name("Leitor"), address1, new Email("example@exampling.com"), phoneNumber2);
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
    void test_same_user_multiple_bids() {

        // arrange
        Address address1 = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-420", null);
        PhonePrefix prefix1 = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix1, "919999999");
        User bidder = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
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
        User bidder = new User (new Name("Reader"), address1, new Email("reader@email.com"), phoneNumber1);
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
}