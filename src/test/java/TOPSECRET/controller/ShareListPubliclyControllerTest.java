package TOPSECRET.controller;

import TOPSECRET.domain.IListOfPublicationsRepo;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShareListPubliclyControllerTest {

    private ListOfPublications _listOfPublicationsDouble;
    private IListOfPublicationsRepo _iListOfPublicationsRepoDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {
        _listOfPublicationsDouble = mock(ListOfPublications.class);
        _iListOfPublicationsRepoDouble = mock(IListOfPublicationsRepo.class);
        _userDouble = mock(User.class);
    }

    @Test
    void returnListFromRepo() {
        //arrange
        when(_iListOfPublicationsRepoDouble.findListsByUser(_userDouble)).thenReturn(List.of(_listOfPublicationsDouble));

        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_iListOfPublicationsRepoDouble, _userDouble);

        //act
        List<ListOfPublications> result = _controller.getListOfLists(_userDouble);

        //assert
        assertEquals(1, result.size());
    }
}
