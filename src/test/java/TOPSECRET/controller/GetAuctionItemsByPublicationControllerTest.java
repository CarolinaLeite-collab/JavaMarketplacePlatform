package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetAuctionItemsByPublicationControllerTest {

    private User _buyer;
    private AuctionRepo _auctionRepo;
    private Publication _publication;
    private GetAuctionItemsByPublicationController _controller;

    @BeforeEach
    void setUp() {

        _buyer = new User(new Name("Zé Isep"), new Email("test@isep.pt"));
        _auctionRepo = new AuctionRepo();
        _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        _controller = new GetAuctionItemsByPublicationController(_auctionRepo, _buyer);

    }

    @Test
    void shouldReturnAuctionItemsForGivenPublication() {
        // Arrange
        Item item1 = new Item(_publication, Condition.GOOD);
        Item item2 = new Item(_publication, Condition.FAIR);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = start.plusDays(5);

        _auctionRepo.createAuction(item1, new Price(10,Currency.EUR), start, end);
        _auctionRepo.createAuction(item2, new Price(20,Currency.EUR), start, end);

        // Act
        List<Item> result = _controller.getAuctionItemsByPublication(_publication);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(item1));
        assertTrue(result.contains(item2));
    }

    @Test
    void shouldReturnEmptyListWhenNoAuctionMatchesPublication() {
        // Arrange
        Publication otherPublication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2020))
                .title(new Title("Different Book"))
                .author(new Author("Different Author"))
                .publisher(new PublishingCompany("Other Publisher"))
                .build();

        // Act
        List<Item> result = _controller.getAuctionItemsByPublication(otherPublication);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}