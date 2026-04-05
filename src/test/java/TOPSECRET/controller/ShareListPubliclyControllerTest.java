package TOPSECRET.controller;

import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShareListPubliclyControllerTest {

    private ListOfItems _listOfItemsDouble;
    private IListOfItemsRepo _iListOfItemsRepoDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {
        _listOfItemsDouble = mock(ListOfItems.class);
        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _userIdDouble = mock(UserId.class);
    }

    @Test
    void returnListFromRepo() {
        //arrange
        when(_iListOfItemsRepoDouble.findListsByUserId(_userIdDouble)).thenReturn(List.of(_listOfItemsDouble));

        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_iListOfItemsRepoDouble, _userIdDouble);

        //act
        List<ListOfItems> result = _controller.getListOfLists(_userIdDouble);

        //assert
        assertEquals(1, result.size());
    }
}