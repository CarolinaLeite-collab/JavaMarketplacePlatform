package TOPSECRET.controller;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.MemoAuctionRepo;
import TOPSECRET.domain.Publication;
import TOPSECRET.domain.User;
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
 *   <li>{@link MemoAuctionRepo} — mocked collaborator (repository dependency)</li>
 *   <li>{@link User} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Publication} — mocked dummy (structural input, no behaviour required)</li>
 *   <li>{@link Item} — mocked dummy (used only as return value)</li>
 * </ul>
 */

class GetAuctionItemsByPublicationControllerTest {

    private User _buyerDouble;
    private MemoAuctionRepo _iAuctionRepoDouble;
    private Publication _publicationDouble;

    @BeforeEach
    void setUp() {

        _buyerDouble = mock(User.class);
        _iAuctionRepoDouble = mock(MemoAuctionRepo.class);
        _publicationDouble = mock(Publication.class);
    }

    @Test
    void testAuctionItemsByPublicationController(){

        // SUT
        new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerDouble);
    }

    @Test
    void shouldReturnAuctionItemsForGivenPublication() {

        // Arrange
        Item item1Double = mock(Item.class);
        Item item2Double = mock(Item.class);

        when(_iAuctionRepoDouble.getAuctionItemsByPublication(_publicationDouble))
                .thenReturn(List.of(item1Double, item2Double));

        // SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(_iAuctionRepoDouble, _buyerDouble);

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
        when(_iAuctionRepoDouble.getAuctionItemsByPublication(_publicationDouble))
                .thenReturn(List.of());

        // SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(_iAuctionRepoDouble, _buyerDouble);

        // Act
        List<Item> result = controller.getAuctionItemsByPublication(_publicationDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}