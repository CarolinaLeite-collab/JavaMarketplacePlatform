package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuctionItemsByGenreControllerTest {

    private User _buyerUserDouble;
    private MemoAuctionRepo _iAuctionRepoDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _buyerUserDouble = mock(User.class);
        _genreDouble = mock (Genre.class);
        _iAuctionRepoDouble = mock(MemoAuctionRepo.class);
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
        when(_iAuctionRepoDouble.getAuctionItemsByGenre(_genreDouble)).thenReturn(itemsList);

        // SUT
        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerUserDouble);

        // Act
        List<Item> items = controller.getAuctionItemsByGenre(_genreDouble);

        // Assert
        assertEquals(itemsList, items);
        verify(_iAuctionRepoDouble).getAuctionItemsByGenre(_genreDouble);
    }

    @Test
    void testGetAuctionItemsByGenreWithNoAuctionShouldReturnEmptyList(){

        // Arrange
        when(_iAuctionRepoDouble.getAuctionItemsByGenre(_genreDouble)).thenReturn(List.of());

        // SUT
        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerUserDouble);

        // Act
        List<Item> listOfAuctionItemsByGenre = controller.getAuctionItemsByGenre(_genreDouble);

        // Assert
        assertTrue(listOfAuctionItemsByGenre.isEmpty());
        verify(_iAuctionRepoDouble).getAuctionItemsByGenre(_genreDouble);
    }
}