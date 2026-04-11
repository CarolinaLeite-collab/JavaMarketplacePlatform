package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetDirectSaleItemsByAuthorControllerTest {

    private UserId _userIdDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private AuthorId _authorIdDouble;

    @BeforeEach
    void setUp() {

        _userIdDouble = mock(UserId.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _authorIdDouble = mock(AuthorId.class);

    }

    @Test
    void testAConstructor(){

        //act / SUT
        new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userIdDouble);

    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnEmptyListWhenThereAreNoItems(){

        //arrange
        when(_iDirectSaleRepoDouble.getDirectSaleItemsByAuthor(_authorIdDouble)).thenReturn(List.of());

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userIdDouble);

        //act
        List<Item> listOfDirectSaleItemsByAuthor = ctl.getDirectSaleItemsByAuthor(_authorIdDouble);

        //assert
        assertTrue(listOfDirectSaleItemsByAuthor.isEmpty());

    }

    @Test
    void getDirectSaleItemsByAuthorReturnsListWithCorrectSize() throws InstantiationException {

        //arrange
        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        when(_iDirectSaleRepoDouble.getDirectSaleItemsByAuthor(_authorIdDouble)).thenReturn(List.of(_itemDouble1, _itemDouble2));

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userIdDouble);

        //act
        List<Item> listOfDirectSaleItemsByAuthor = ctl.getDirectSaleItemsByAuthor(_authorIdDouble);

        //assert
        assertEquals(2, listOfDirectSaleItemsByAuthor.size());

    }

    @Test
    void getDirectSaleItemsByAuthorReturnsListContainingCorrectItems() {

        //arrange
        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        when(_iDirectSaleRepoDouble.getDirectSaleItemsByAuthor(_authorIdDouble)).thenReturn(List.of(_itemDouble1, _itemDouble2));

        //SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userIdDouble);

        //act
        List<Item> listOfDirectSaleItemsByAuthor = ctl.getDirectSaleItemsByAuthor(_authorIdDouble);

        //assert
        assertTrue(listOfDirectSaleItemsByAuthor.containsAll(List.of(_itemDouble1, _itemDouble2)));

    }

    @Test
    void getDirectSaleItemsByAuthorShouldCallRepoWithCorrectAuthor() {

        //arrange / SUT
        GetDirectSaleItemsByAuthorController ctl = new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userIdDouble);

        //act
        ctl.getDirectSaleItemsByAuthor(_authorIdDouble);

        //assert
        verify(_iDirectSaleRepoDouble, times(1)).getDirectSaleItemsByAuthor(_authorIdDouble);
    }

}