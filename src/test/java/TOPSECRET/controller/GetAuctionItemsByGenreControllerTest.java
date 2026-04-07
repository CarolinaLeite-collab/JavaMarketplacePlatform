package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.GenreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetAuctionItemsByGenreControllerTest {

    private User _buyerUserDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private GenreId _genreIdDouble;

    @BeforeEach
    void setUp() {

        _buyerUserDouble = mock(User.class);
        _genreIdDouble = mock (GenreId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
    }

    @Test
    void testAuctionItemsByGenreController(){

        // SUT
        new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerUserDouble);
    }

    @Test
    void testGetAuctionItemsByGenreWithAuctionShouldReturnANotEmptyList(){

        // Arrange
        Item _itemDouble = mock(Item.class);
        List<Item> itemsList = List.of(_itemDouble);
        when(_iAuctionRepoDouble.getAuctionItemsByGenre(_genreIdDouble)).thenReturn(itemsList);

        // SUT
        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerUserDouble);

        // Act
        List<Item> items = controller.getAuctionItemsByGenre(_genreIdDouble);

        // Assert
        assertEquals(itemsList, items);
        verify(_iAuctionRepoDouble).getAuctionItemsByGenre(_genreIdDouble);
    }

    @Test
    void testGetAuctionItemsByGenreWithNoAuctionShouldReturnEmptyList(){

        // Arrange
        when(_iAuctionRepoDouble.getAuctionItemsByGenre(_genreIdDouble)).thenReturn(List.of());

        // SUT
        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerUserDouble);

        // Act
        List<Item> listOfAuctionItemsByGenre = controller.getAuctionItemsByGenre(_genreIdDouble);

        // Assert
        assertTrue(listOfAuctionItemsByGenre.isEmpty());
        verify(_iAuctionRepoDouble).getAuctionItemsByGenre(_genreIdDouble);
    }
}