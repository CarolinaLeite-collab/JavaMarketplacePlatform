package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.Period;
import java.time.ZonedDateTime;

class ItemTest {

    private ZonedDateTime futureStart = ZonedDateTime.now().plusDays(1);
    private ZonedDateTime futureEnd = ZonedDateTime.now().plusDays(2);

    @Test
    void itemIsCreatedWithPublicationAndCondition() {
        Publication publication = new Publication("Test Title");
        Item item = new Item(publication, Condition.GOOD);

        assertEquals(Condition.GOOD, item.getCondition());
    }

    @Test
    void canSetDirectSaleWhenNoAuctionExists() {
        Publication publication = new Publication("Test Title");
        Item item = new Item(publication, Condition.LIKE_NEW);

        DirectSale directSale =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));

        assertDoesNotThrow(() -> item.setDirectSale(directSale));
    }

    @Test
    void canSetAuctionWhenNoDirectSaleExists() {
        Publication publication = new Publication("Test Title");
        Item item = new Item(publication, Condition.FAIR);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                futureStart,
                futureEnd
        );

        assertDoesNotThrow(() -> item.setAuction(auction));
    }

    @Test
    void cannotSetDirectSaleIfAuctionAlreadyExists() {
        Publication pub = new Publication("Test Title");
        Item item = new Item(pub, Condition.GOOD);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                futureStart,
                futureEnd
        );
        item.setAuction(auction);

        DirectSale directSale =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> item.setDirectSale(directSale)
        );

        assertEquals("Item is already in an auction.", exception.getMessage());
    }

    @Test
    void cannotSetAuctionIfDirectSaleAlreadyExists() {
        Publication pub = new Publication("Test Title");
        Item item = new Item(pub, Condition.GOOD);

        DirectSale ds =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));
        item.setDirectSale(ds);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                futureStart,
                futureEnd
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> item.setAuction(auction)
        );

        assertEquals("Item is already in a direct sale.", exception.getMessage());
    }

    @Test
    void settingDirectSaleDoesNotOverwriteCondition() {
        Publication pub = new Publication("Test Title");
        Item item = new Item(pub, Condition.POOR);

        DirectSale ds =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));
        item.setDirectSale(ds);

        assertEquals(Condition.POOR, item.getCondition());
    }

    @Test
    void settingAuctionDoesNotOverwriteCondition() {
        Publication pub = new Publication("Test Title");
        Item item = new Item(pub, Condition.LIKE_NEW);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                futureStart,
                futureEnd
        );
        item.setAuction(auction);

        assertEquals(Condition.LIKE_NEW, item.getCondition());
    }
}

