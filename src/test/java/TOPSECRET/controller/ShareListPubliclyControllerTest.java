package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShareListPubliclyControllerTest {

    private ShareListPubliclyController _controller;
    private ListOfPublications _listOfPublications;
    private ListOfPublicationsRepo _listOfPublicationsRepo;
    private User _user;


    @BeforeEach
    void setUp() {
        _listOfPublications = mock(ListOfPublications.class);
        _listOfPublicationsRepo = mock(ListOfPublicationsRepo.class);
        _user = mock(User.class);
        _controller = new ShareListPubliclyController(_listOfPublicationsRepo);
    }

    @Test
    void returnListFromRepo() {
        //arrange
        when(_listOfPublicationsRepo.findListsByUser(_user)).thenReturn(List.of(_listOfPublications));

        //act
        List<ListOfPublications> result = _controller.getListOfLists(_user);

        //assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFalseWhenSelectedListIsNull() {
        // shareListPublicly() – returns false when selected list is null

        //act
        boolean result = _controller.shareListPublicly(null);

        //assert
        assertFalse(result);
    }

    @Test
    void makesListPublicWhenInitiallyPrivate() {
        // shareListPublicly() – changes list visibility from private to public
        doNothing().when(_listOfPublications).makePublic();

        boolean result = _controller.shareListPublicly(_listOfPublications);

        assertTrue(result);
        verify(_listOfPublications).makePublic();
    }
}
