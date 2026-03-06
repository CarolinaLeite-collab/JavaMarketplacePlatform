package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionFactoryTest {

    private AuctionFactory factory;
    private Item item;
    private Price startingPrice;
    private Price outrightPrice;

    @BeforeEach
    void setUp() {
        factory = new AuctionFactory();

        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .genre(new Genre("action"))
                .build();

        item = new Item(publication, Condition.GOOD);
        startingPrice = new Price(10.0, Currency.EUR);
        outrightPrice = new Price(50.0, Currency.EUR);
    }

    @Test
    void createWithoutOutrightBuildsAuctionAndLinksItem() throws InstantiationException {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        // Act
        Auction auction = factory.create(item, startingPrice, start, end);

        // Assert
        assertNotNull(auction);
        assertSame(item, auction.getItem());
        assertNotNull(item.getAuction());
        assertSame(auction, item.getAuction());
    }

    @Test
    void createWithOutrightBuildsAuctionAndLinksItem() throws InstantiationException {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        // Act
        Auction auction = factory.create(item, startingPrice, outrightPrice, start, end);

        // Assert
        assertNotNull(auction);
        assertSame(item, auction.getItem());
        assertNotNull(item.getAuction());
        assertSame(auction, item.getAuction());
    }

    @Test
    void createWithoutOutrightWrapsInvalidStartDate() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(1);

        // Act
        InstantiationException ex = assertThrows(InstantiationException.class,
                () -> factory.create(item, startingPrice, start, end));

        // Assert
        assertTrue(ex.getMessage().contains("Invalid start date"));
    }

    @Test
    void createWithOutrightWrapsInvalidOutrightPrice() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);
        Price invalidOutright = new Price(10.0, Currency.EUR);

        // Act
        InstantiationException ex = assertThrows(InstantiationException.class,
                () -> factory.create(item, startingPrice, invalidOutright, start, end));

        // Assert
        assertTrue(ex.getMessage().contains("Invalid outright price"));
    }

    @Test
    void createWithMocksCallsItemSetAuction() throws InstantiationException {
        // Arrange
        Item mockedItem = mock(Item.class);
        Price mockedStarting = mock(Price.class);
        Price mockedOutright = mock(Price.class);
        when(mockedStarting.getValue()).thenReturn(10.0);
        when(mockedOutright.getValue()).thenReturn(20.0);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        // Act
        Auction auction = factory.create(mockedItem, mockedStarting, mockedOutright, start, end);

        // Assert
        assertNotNull(auction);
        verify(mockedItem).setAuction(any(Auction.class));
    }
}
