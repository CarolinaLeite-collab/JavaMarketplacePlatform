package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.ddd.IListOfPublicationsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShareListPubliclyControllerTest {

    private ListOfPublications _listOfPublications;
    private IListOfPublicationsRepo _listOfPublicationsRepo;
    private User _user;

    @BeforeEach
    void setUp() {
        _listOfPublications = mock(ListOfPublications.class);
        _listOfPublicationsRepo = mock(IListOfPublicationsRepo.class);
        _user = mock(User.class);
    }

    @Test
    void returnListFromRepo() {
        //arrange
        when(_listOfPublicationsRepo.findListsByUser(_user)).thenReturn(List.of(_listOfPublications));

        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_listOfPublicationsRepo);

        //act
        List<ListOfPublications> result = _controller.getListOfLists(_user);

        //assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFalseWhenSelectedListIsNull() {
        //SUT
        ShareListPubliclyController _controller = new ShareListPubliclyController(_listOfPublicationsRepo);

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
        ShareListPubliclyController _controller = new ShareListPubliclyController(_listOfPublicationsRepo);

        //act
        boolean result = _controller.shareListPublicly(_listOfPublications);

        //assert
        assertTrue(result);
        verify(_listOfPublications).makePublic();
    }
}
