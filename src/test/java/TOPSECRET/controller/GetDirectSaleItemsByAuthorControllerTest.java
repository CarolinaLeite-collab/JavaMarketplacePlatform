package TOPSECRET.controller;

import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.Author.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetDirectSaleItemsByAuthorControllerTest {

    private User _userDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private Author _authorDouble;

    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _authorDouble = mock(Author.class);

    }

    @Test
    void testAConstructor(){

        //act / SUT
        new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userDouble);

    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnEmptyListWhenThereAreNoItems(){

        //arrange
        when(_iDirectSaleRepoDouble.getDirectSaleItemsByAuthor(_authorDouble)).thenReturn(List.of());

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userDouble);

        //act
        List<Item> listOfDirectSaleItemsByAuthor = ctl.getDirectSaleItemsByAuthor(_authorDouble);

        //assert
        assertTrue(listOfDirectSaleItemsByAuthor.isEmpty());

    }

    @Test
    void getDirectSaleItemsByAuthorReturnsListWithCorrectSize() throws InstantiationException {

        //arrange
        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        when(_iDirectSaleRepoDouble.getDirectSaleItemsByAuthor(_authorDouble)).thenReturn(List.of(_itemDouble1, _itemDouble2));

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userDouble);

        //act
        List<Item> listOfDirectSaleItemsByAuthor = ctl.getDirectSaleItemsByAuthor(_authorDouble);

        //assert
        assertEquals(2, listOfDirectSaleItemsByAuthor.size());

    }

    @Test
    void getDirectSaleItemsByAuthorReturnsListContainingCorrectItems() {

        //arrange
        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        when(_iDirectSaleRepoDouble.getDirectSaleItemsByAuthor(_authorDouble)).thenReturn(List.of(_itemDouble1, _itemDouble2));

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userDouble);

        //act
        List<Item> listOfDirectSaleItemsByAuthor = ctl.getDirectSaleItemsByAuthor(_authorDouble);

        //assert
        assertTrue(listOfDirectSaleItemsByAuthor.containsAll(List.of(_itemDouble1, _itemDouble2)));

    }

    @Test
    void getDirectSaleItemsByAuthorShouldCallRepoWithCorrectAuthor() {

        //arrange / SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userDouble);

        //act
        ctl.getDirectSaleItemsByAuthor(_authorDouble);

        //assert
        verify(_iDirectSaleRepoDouble, times(1)).getDirectSaleItemsByAuthor(_authorDouble);
    }

}