package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.valueobject.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetDirectSaleItemsByAuthorControllerTest {

    private User _userDouble;
    private IDirectSaleRepo _dsrDouble;
    private Author _authorDouble;

    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _dsrDouble = mock(IDirectSaleRepo.class);
        _authorDouble = mock(Author.class);

    }

    @Test
    void testAConstructor(){

        //act / SUT
        new GetDirectSaleItemsByAuthorController(_dsrDouble, _userDouble);

    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnEmptyListWhenThereAreNoItems(){

        //arrange
        when(_dsrDouble.getDirectSaleItemsByAuthor(_authorDouble)).thenReturn(List.of());

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_dsrDouble, _userDouble);

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

        when(_dsrDouble.getDirectSaleItemsByAuthor(_authorDouble)).thenReturn(List.of(_itemDouble1, _itemDouble2));

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_dsrDouble, _userDouble);

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

        when(_dsrDouble.getDirectSaleItemsByAuthor(_authorDouble)).thenReturn(List.of(_itemDouble1, _itemDouble2));

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_dsrDouble, _userDouble);

        //act
        List<Item> listOfDirectSaleItemsByAuthor = ctl.getDirectSaleItemsByAuthor(_authorDouble);

        //assert
        assertTrue(listOfDirectSaleItemsByAuthor.containsAll(List.of(_itemDouble1, _itemDouble2)));

    }

    @Test
    void getDirectSaleItemsByAuthorShouldCallRepoWithCorrectAuthor() {

        //arrange / SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_dsrDouble, _userDouble);

        //act
        ctl.getDirectSaleItemsByAuthor(_authorDouble);

        //assert
        verify(_dsrDouble, times(1)).getDirectSaleItemsByAuthor(_authorDouble);
    }

}