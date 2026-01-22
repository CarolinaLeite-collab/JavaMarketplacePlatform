package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetItemsOnAuctionOfAGivenAuthorControllerTest {

    private User _buyer;
    private AuctionRepo _auctionRepo;
    private Author _author;
    private GetItemsOnAuctionOfAGivenAuthorController _controller;

    @BeforeEach
    void setUp() {
        _buyer = new User(new Name("Buyer"), new Email("buyer@test.com"));
        _auctionRepo = new AuctionRepo();
        _author = new Author("Seneca");
        _controller = new GetItemsOnAuctionOfAGivenAuthorController(_auctionRepo, _buyer);
    }

    @Test
    void constructor_acceptsValidDependencies() {
        new GetItemsOnAuctionOfAGivenAuthorController(_auctionRepo, _buyer);
    }

    @Test
    void getAuctionItemsByAuthor_returnsEmpty_whenNoAuctions() {
        List<Item> items = _controller.getAuctionItemsByAuthor(_author);
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthor_returnsItemsForMatchingAuthor() {
        // arrange publication and item
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(_author)
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        _auctionRepo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> items = _controller.getAuctionItemsByAuthor(new Author("sEnEcA"));
        assertEquals(1, items.size());
        assertSame(item, items.get(0));
    }

    @Test
    void getAuctionItemsByAuthor_returnsEmpty_whenAuthorDoesNotMatch() {
        // arrange publication and item with different author
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Different"))
                .publisher(new Publisher("Penguin"))
                .build();
        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        _auctionRepo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> items = _controller.getAuctionItemsByAuthor(_author);
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }
}

