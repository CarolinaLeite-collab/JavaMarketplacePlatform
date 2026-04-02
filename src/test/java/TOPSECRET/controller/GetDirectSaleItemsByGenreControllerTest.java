package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetDirectSaleItemsByGenreControllerTest {

    private User _buyerDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _buyerDouble = mock(User.class);
        _genreDouble = mock (Genre.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);

    }

    @Test
    void testDirectSaleItemsByGenreController(){

        // SUT
        new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble, _buyerDouble);

    }

    @Test
    void testGetAuctionItemsByGenreWithAuctionShouldReturnANotEmptyList(){

        // arrange
        Item _itemDouble = mock(Item.class);
        List<Item> itemsList = List.of(_itemDouble);
        when(_iDirectSaleRepoDouble.getDirectSaleItemsByGenre(_genreDouble)).thenReturn(itemsList);

        // SUT
        GetDirectSaleItemsByGenreController controller = new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble, _buyerDouble);

        // act
        List<Item> items = controller.getDirectSaleItemsByGenre(_genreDouble);

        // assert
        assertEquals(itemsList, items);

    }

    @Test
    void testGetAuctionItemsByGenreWithNoAuctionShouldReturnEmptyList(){

        // arrange
        when(_iDirectSaleRepoDouble.getDirectSaleItemsByGenre(_genreDouble)).thenReturn(List.of());

        // SUT
        GetDirectSaleItemsByGenreController controller = new GetDirectSaleItemsByGenreController(_iDirectSaleRepoDouble, _buyerDouble);

        // act
        List<Item> listOfDirectSaleItemsByGenre = controller.getDirectSaleItemsByGenre(_genreDouble);

        //assert
        assertTrue(listOfDirectSaleItemsByGenre.isEmpty());

    }

}
