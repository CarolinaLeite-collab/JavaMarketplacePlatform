package TOPSECRET.controller;

import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAuctionItemsByPublicationControllerTest {

    private UserId _buyerIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private Publication _publicationDouble;

    @BeforeEach
    void setUp() {

        _buyerIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _publicationDouble = mock(Publication.class);
    }

    @Test
    void testAuctionItemsByPublicationController(){

        // SUT
        new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerIdDouble);
    }

    @Test
    void shouldReturnAuctionItemsForGivenPublication() {

        // Arrange
        Item item1Double = mock(Item.class);
        Item item2Double = mock(Item.class);

        when(_iAuctionRepoDouble.getAuctionItemsByPublication(_publicationDouble))
                .thenReturn(List.of(item1Double, item2Double));

        // SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(_iAuctionRepoDouble, _buyerIdDouble);

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
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(_iAuctionRepoDouble, _buyerIdDouble);

        // Act
        List<Item> result = controller.getAuctionItemsByPublication(_publicationDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}