package TOPSECRET.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BidsTest {
    //test constructor
    @Test
    void getHighestBidFail_EmptyBidList() {
        //arrange
        Bids bids = new Bids();
        //act
        //assert
        Assertions.assertThrows(IllegalStateException.class, () -> bids.getHighestBid());
    }

    @Test
    void getHighestBid_SingleBid() {
        //arrange
        Bids bids = new Bids();
        Country country = new Country("Português");
        User user = new User(
                new Name("Tiago"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("1252008@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "918902632")
        );
        double expectedResult = 100.0;
        Price offerPrice = new Price(expectedResult, Currency.EUR);
        Bid bid = new Bid(user, offerPrice);

        //act
        bids.addBid(bid);
        double result = bids.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(expectedResult, result);
    }

    @Test
    void getHighestBid_MultipleBids_firstHighest() {
        //arrange
        Bids bids = new Bids();
        Country country = new Country("Português");
        User user1 = new User(
                new Name("Tiago"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("1252008@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "918902632")
        );
        User user2 = new User(
                new Name("Carla"),
                new Address("Rua Vasco", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Porto", country, "1000-205", null),
                new Email("125228@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "918972632")
        );
        double expectedResult = 100.0;
        Price offerPrice = new Price(expectedResult, Currency.EUR);
        Price offerPrice2 = new Price(expectedResult - 1, Currency.EUR);
        Bid bid1 = new Bid(user1, offerPrice);
        Bid bid2 = new Bid(user2, offerPrice2);

        //act
        bids.addBid(bid1);
        bids.addBid(bid2);
        double result = bids.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(expectedResult, result);
    }

    @Test
    void getHighestBid_MultipleBids_lastHighest() {
        //arrange
        Bids bids = new Bids();
        Country country = new Country("Português");
        User user1 = new User(
                new Name("Tiago"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", country, "1000-205", null),
                new Email("1252008@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "918902632")
        );
        User user2 = new User(
                new Name("Carla"),
                new Address("Rua Vasco", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Porto", country, "1000-205", null),
                new Email("125228@isep.ipp.pt"),
                new Phone(new PhonePrefix("+351"), "918972632")
        );
        double expectedResult = 100.0;
        Price offerPrice = new Price(expectedResult, Currency.EUR);
        Price offerPrice2 = new Price(expectedResult - 1, Currency.EUR);
        Bid bid1 = new Bid(user1, offerPrice);
        Bid bid2 = new Bid(user2, offerPrice2);

        //act
        bids.addBid(bid2);
        bids.addBid(bid1);
        double result = bids.getHighestBid().getOfferPrice().getValue();

        //assert
        assertEquals(expectedResult, result);

    }
}
