package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetDirectSaleItemsByGenreControllerTest {

    private User _buyerDouble;
    private DirectSaleRepo _directSaleRepoDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _buyerDouble = mock(User.class);
        _genreDouble = mock (Genre.class);
        _directSaleRepoDouble = mock(DirectSaleRepo.class);

    }

    @Test
    void testDirectSaleItemsByGenreController(){

        // SUT
        new GetDirectSaleItemsByGenreController(_directSaleRepoDouble, _buyerDouble);

    }

    @Test
    void testGetAuctionItemsByGenreWithAuctionShouldReturnANotEmptyList(){

        // arrange
        Item _itemDouble = mock(Item.class);
        List<Item> itemsList = List.of(_itemDouble);
        when(_directSaleRepoDouble.getDirectSaleItemsByGenre(_genreDouble)).thenReturn(itemsList);

        // SUT
        GetDirectSaleItemsByGenreController controller = new GetDirectSaleItemsByGenreController(_directSaleRepoDouble, _buyerDouble);

        // act
        List<Item> items = controller.getDirectSaleItemsByGenre(_genreDouble);

        // assert
        assertEquals(itemsList, items);

    }

    @Test
    void testGetAuctionItemsByGenreWithNoAuctionShouldReturnEmptyList(){

        // arrange
        when(_directSaleRepoDouble.getDirectSaleItemsByGenre(_genreDouble)).thenReturn(List.of());

        // SUT
        GetDirectSaleItemsByGenreController controller = new GetDirectSaleItemsByGenreController(_directSaleRepoDouble, _buyerDouble);

        // act
        List<Item> listOfDirectSaleItemsByGenre = controller.getDirectSaleItemsByGenre(_genreDouble);

        //assert
        assertTrue(listOfDirectSaleItemsByGenre.isEmpty());

    }

}
