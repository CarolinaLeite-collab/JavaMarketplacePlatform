package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAuctionItemsByPublicationControllerTest {

    private User _buyerDouble;
    private AuctionRepo _auctionRepoDouble;
    private Publication _publicationDouble;
    private GetAuctionItemsByPublicationController _controller;

    @BeforeEach
    void setUp() {
        _buyerDouble = mock(User.class);
        _auctionRepoDouble = mock(AuctionRepo.class);
        _publicationDouble = mock(Publication.class);

        _controller = new GetAuctionItemsByPublicationController(_auctionRepoDouble, _buyerDouble);
    }

    @Test
    void shouldReturnAuctionItemsForGivenPublication() {
        // Arrange
        Item _item1Double = mock(Item.class);
        Item _item2Double = mock(Item.class);

        when(_auctionRepoDouble.getAuctionItemsByPublication(_publicationDouble))
                .thenReturn(List.of(_item1Double, _item2Double));

        // Act
        List<Item> result = _controller.getAuctionItemsByPublication(_publicationDouble);

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.contains(_item1Double)),
                () -> assertTrue(result.contains(_item2Double))
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoAuctionMatchesPublication() {
        // Arrange
        Publication _publicationDouble = mock(Publication.class);

        when(_auctionRepoDouble.getAuctionItemsByPublication(_publicationDouble))
                .thenReturn(List.of());

        // Act
        List<Item> result = _controller.getAuctionItemsByPublication(_publicationDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}