package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetAuctionItemsByPublisherControllerTest {


    private User _buyer;
    private AuctionRepo _auctionRepo;
    private Publisher _publisher;
    private GetAuctionItemsByPublisherController _controller;

    @BeforeEach
    void setUp() {
        _buyer = new User(new Name("Buyer"), new Email("buyer@test.com"));
        _auctionRepo = new AuctionRepo();
        _publisher = new Publisher("Seneca");
        _controller = new GetAuctionItemsByPublisherController(_publisher, _auctionRepo, _buyer);
    }

    @Test
    void should_return_empty_list_when_no_auctions() {
        List<Item> items = _controller.getAuctionItemsByPublisher(_publisher);
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void should_return_correct_list_for_publisher() {
        Publisher publisher1 = new Publisher("publisher1");

        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(publisher1)
                .build();
        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        _auctionRepo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        //when publisher's name matches exactly
        List<Item> items1 = _controller.getAuctionItemsByPublisher(new Publisher("publisher1"));
        assertEquals(1, items1.size());
        assertSame(item, items1.get(0));

        //testing case insensitiveness
        List<Item> items2 = _controller.getAuctionItemsByPublisher(new Publisher("pUbLisheR1"));
        assertEquals(1, items2.size());
        assertSame(item, items2.get(0));
    }

    @Test
    void should_return_empty_when_publisher_does_not_match() {
        Publisher publisher1 = new Publisher("publisher1");

        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(publisher1)
                .build();
        Item item = new Item(pub, Condition.GOOD);;

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        _auctionRepo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> items = _controller.getAuctionItemsByPublisher(new Publisher("publisher2"));
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

}