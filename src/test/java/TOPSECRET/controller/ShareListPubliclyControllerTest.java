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

    private ListOfPublications _listOfPublications;
    private IListOfPublicationsRepo _iListOfPublicationsRepo;
    private User _user;

    @BeforeEach
    void setUp() {
        _listOfPublications = mock(ListOfPublications.class);
        _iListOfPublicationsRepo = mock(IListOfPublicationsRepo.class);
        _user = mock(User.class);
    }

    @Test
    void returnListFromRepo() {
        //arrange
        when(_iListOfPublicationsRepo.findListsByUser(_user)).thenReturn(List.of(_listOfPublications));

        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_iListOfPublicationsRepo);

        //act
        List<ListOfPublications> result = _controller.getListOfLists(_user);

        //assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFalseWhenSelectedListIsNull() {
        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_iListOfPublicationsRepo);

        //act
        boolean result = _controller.shareListPublicly(null);

        //assert
        assertFalse(result);
    }

    @Test
    void makesListPublicWhenInitiallyPrivate() {
        //arrange
        doNothing().when(_listOfPublications).makePublic();

        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_iListOfPublicationsRepo);

        //act
        boolean result = _controller.shareListPublicly(_listOfPublications);

        //assert
        assertTrue(result);
        verify(_listOfPublications).makePublic();
    }
}
