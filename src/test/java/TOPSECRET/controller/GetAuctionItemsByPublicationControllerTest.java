package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GetAuctionItemsByPublicationController}.
 *
 * <p>The System Under Test (SUT) is isolated using Mockito doubles:
 * <ul>
 *   <li>{@link AuctionRepo} — mocked collaborator (repository dependency)</li>
 *   <li>{@link User} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Publication} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Item} — mocked dummy (used only as return value)</li>
 * </ul>
 */

class GetAuctionItemsByPublicationControllerTest {

    private User _buyerDouble;
    private AuctionRepo _auctionRepoDouble;
    private Publication _publicationDouble;

    @BeforeEach
    void setUp() {

        _buyerDouble = mock(User.class);
        _auctionRepoDouble = mock(AuctionRepo.class);
        _publicationDouble = mock(Publication.class);
    }

    @Test
    void testAuctionItemsByPublicationController(){

        // SUT
        new GetAuctionItemsByGenreController(_auctionRepoDouble, _buyerDouble);
    }

    @Test
    void shouldReturnAuctionItemsForGivenPublication() {

        // Arrange
        Item item1Double = mock(Item.class);
        Item item2Double = mock(Item.class);

        when(_auctionRepoDouble.getAuctionItemsByPublication(_publicationDouble))
                .thenReturn(List.of(item1Double, item2Double));

        // SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(_auctionRepoDouble, _buyerDouble);

        // Act
        List<Item> result = controller.getAuctionItemsByPublication(_publicationDouble);

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.contains(item1Double)),
                () -> assertTrue(result.contains(item2Double))
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoAuctionMatchesPublication() {

        // Arrange
        when(_auctionRepoDouble.getAuctionItemsByPublication(_publicationDouble))
                .thenReturn(List.of());

        // SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(_auctionRepoDouble, _buyerDouble);

        // Act
        List<Item> result = controller.getAuctionItemsByPublication(_publicationDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}