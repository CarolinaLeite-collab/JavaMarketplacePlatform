package TOPSECRET.controller;

import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShareListPubliclyControllerTest {

    private ListOfItems _listOfItemsDouble;
    private IListOfItemsRepo _iListOfItemsRepoDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {
        _listOfItemsDouble = mock(ListOfItems.class);
        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _userDouble = mock(User.class);
    }

    @Test
    void returnListFromRepo() {
        //arrange
        when(_iListOfItemsRepoDouble.findListsByUser(_userDouble)).thenReturn(List.of(_listOfItemsDouble));

        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_iListOfItemsRepoDouble, _userDouble);

        //act
        List<ListOfItems> result = _controller.getListOfLists(_userDouble);

        //assert
        assertEquals(1, result.size());
    }
}
