package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.Period;
import java.time.Year;
import java.time.ZonedDateTime;

class ItemTest {

    private ZonedDateTime auctionStartDate = ZonedDateTime.now().plusDays(1);
    private ZonedDateTime auctionEndDate = ZonedDateTime.now().plusDays(2);

    @Test
    void itemIsCreatedWithPublicationAndCondition() {
        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(publication, Condition.GOOD);

        assertEquals(Condition.GOOD, item.getCondition());
    }

    @Test
    void canSetDirectSaleWhenNoAuctionExists() {
        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(publication, Condition.LIKE_NEW);

        DirectSale directSale =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));

        assertDoesNotThrow(() -> item.setDirectSale(directSale));
    }

    @Test
    void canSetAuctionWhenNoDirectSaleExists() {
        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(publication, Condition.FAIR);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                auctionStartDate,
                auctionEndDate
        );

        assertDoesNotThrow(() -> item.setAuction(auction));
    }

    @Test
    void cannotSetDirectSaleIfAuctionAlreadyExists() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                auctionStartDate,
                auctionEndDate
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
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        DirectSale ds =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));
        item.setDirectSale(ds);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                auctionStartDate,
                auctionEndDate
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> item.setAuction(auction)
        );

        assertEquals("Item is already in a direct sale.", exception.getMessage());
    }

    @Test
    void settingDirectSaleDoesNotOverwriteCondition() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.POOR);

        DirectSale ds =
                new DirectSale(item, new Price(10.0, Currency.EUR), Period.ofMonths(3));
        item.setDirectSale(ds);

        assertEquals(Condition.POOR, item.getCondition());
    }

    @Test
    void settingAuctionDoesNotOverwriteCondition() {
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.LIKE_NEW);

        AuctionRepo repo = new AuctionRepo();
        Auction auction = repo.createAuction(
                item,
                new Price(5.0, Currency.EUR),
                auctionStartDate,
                auctionEndDate
        );
        item.setAuction(auction);

        assertEquals(Condition.LIKE_NEW, item.getCondition());
    }

    @Test
    void puttingPublicationOnAuctionWrongAuctionItem(){

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();

        Item item = new Item(testPub, Condition.GOOD);
        Auction wrongAuctionItem = new Auction(new Item(testPub, Condition.POOR), new Price(10, Currency.EUR), ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(8));

        assertThrows(IllegalArgumentException.class, () -> item.setAuction(wrongAuctionItem),
                "This Auction does not belong to this Item.");

    }

    @Test
    void puttingPublicationOnDirectSaleWrongDirectSaleItem(){

        Publication testPub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();

        Item item = new Item(testPub, Condition.GOOD);
        DirectSale wrongDirectSaleItem = new DirectSale(new Item(testPub, Condition.POOR), new Price(10.0, Currency.EUR), Period.ofMonths(3));

        assertThrows(IllegalArgumentException.class, () -> item.setDirectSale(wrongDirectSaleItem),
                "This DirectSale does not belong to this Item.");

    }

    @Test
    void testGetDirectSale_WhenDirectSaleIsSet() {
        // Arrange
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();

        Item item = new Item(pub, Condition.GOOD);
        DirectSale sale = new DirectSale(item, new Price(10, Currency.EUR), Period.ofDays(30));

        // Act
        item.setDirectSale(sale);

        // Assert
        assertEquals(sale, item.getDirectSale(),
                "Getter must return the DirectSale previously assigned");
    }

    @Test
    void testGetDirectSale_WhenNoDirectSaleWasAssigned() {
        // Arrange
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780141036144"))
                .year(Year.of(2012))
                .title(new Title("1984"))
                .author(new Author("George Orwell"))
                .publisher(new Publisher("Penguin"))
                .build();

        Item item = new Item(pub, Condition.GOOD);

        // Act + Assert
        assertNull(item.getDirectSale(),
                "Getter must return null when no DirectSale is assigned");
    }

}