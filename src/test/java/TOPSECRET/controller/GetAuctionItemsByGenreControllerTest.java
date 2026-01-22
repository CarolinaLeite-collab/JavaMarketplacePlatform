package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetAuctionItemsByGenreControllerTest {

    private User _user;
    private AuctionRepo _auctionRepo;
    private Genre _genre;
    private GetAuctionItemsByGenreController  _controller;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email("test@isep.pt"));
        _auctionRepo = new AuctionRepo();
        _genre = new Genre("Action");
        _controller = new GetAuctionItemsByGenreController(_auctionRepo, _user);

    }

    @Test
    void test_auction_items_by_genre_controller(){

        //act
        new GetAuctionItemsByGenreController(_auctionRepo, _user);

    }

    @Test
    void test_get_auction_items_by_genre_with_auction_should_return_a_not_empty_list(){

        Publication _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .genre(_genre)
                .build();
        Item item = new Item(_publication, Condition.FAIR);

        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");

        Price price = new Price(5.0, Currency.EUR);

        _auctionRepo.createAuction(item, price, _startDate, _endDate );

        List<Item> items = _controller.getAuctionItemsByGenre(_genre);

        assertNotNull(items);
        assertFalse(items.isEmpty());



    }

    @Test
    void test_get_auction_items_by_genre_with_no_auction_should_return_empty_list(){

        //arrange and act
        List<Item> listOfAuctionItemsByGenre = _controller.getAuctionItemsByGenre(_genre);

        //assert
        assertNotNull(listOfAuctionItemsByGenre);
        assertTrue(listOfAuctionItemsByGenre.isEmpty());

    }

}