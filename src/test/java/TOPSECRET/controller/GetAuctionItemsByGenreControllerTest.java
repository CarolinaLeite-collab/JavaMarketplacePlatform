package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuctionItemsByGenreControllerTest {

    private User _buyerUserDouble;
    private AuctionRepo _auctionRepoDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _buyerUserDouble = mock(User.class);
        _genreDouble = mock (Genre.class);
        _auctionRepoDouble = mock(AuctionRepo.class);
    }

    @Test
    void testAuctionItemsByGenreController(){

        // SUT
        new GetAuctionItemsByGenreController(_auctionRepoDouble, _buyerUserDouble);
    }

    @Test
    void testGetAuctionItemsByGenreWithAuctionShouldReturnANotEmptyList(){

        // Arrange
        Item _itemDouble = mock(Item.class);
        List<Item> itemsList = List.of(_itemDouble);
        when(_auctionRepoDouble.getAuctionItemsByGenre(_genreDouble)).thenReturn(itemsList);

        // SUT
        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController(_auctionRepoDouble, _buyerUserDouble);

        // Act
        List<Item> items = controller.getAuctionItemsByGenre(_genreDouble);

        // Assert
        assertEquals(itemsList, items);
        verify(_auctionRepoDouble).getAuctionItemsByGenre(_genreDouble);
    }

    @Test
    void testGetAuctionItemsByGenreWithNoAuctionShouldReturnEmptyList(){

        // Arrange
        when(_auctionRepoDouble.getAuctionItemsByGenre(_genreDouble)).thenReturn(List.of());

        // SUT
        GetAuctionItemsByGenreController controller = new GetAuctionItemsByGenreController(_auctionRepoDouble, _buyerUserDouble);

        // Act
        List<Item> listOfAuctionItemsByGenre = controller.getAuctionItemsByGenre(_genreDouble);

        // Assert
        assertTrue(listOfAuctionItemsByGenre.isEmpty());
        verify(_auctionRepoDouble).getAuctionItemsByGenre(_genreDouble);
    }
}