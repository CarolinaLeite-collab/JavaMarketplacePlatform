package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfPublicationsInMyLibraryControllerTest {
    private User _user;
    private LibraryRepo _libraryRepo;
    private Library _myLibrary;

    @BeforeEach
    void setUp() {
        _user = mock(User.class);
        _myLibrary = mock(Library.class);
        _libraryRepo = mock(LibraryRepo.class);
        when(_libraryRepo.findByUser(_user)).thenReturn(_myLibrary);
    }

    @Test
    void shouldReturnEmptyListWhenLibraryExistsButEmpty() {
        //Arrange
        when(_myLibrary.getPublicationsInLibrary()).thenReturn(List.of());
        //SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_libraryRepo);
        //Act
        List<PublicationDetails> result = controller.getListOfPublications(_user);
        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfPublicationsInLibrary() {
        //Arrange
        PublicationDetails details = mock(PublicationDetails.class);
        when(_myLibrary.getPublicationsInLibrary()).thenReturn(List.of(details));
        //SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_libraryRepo);
        //Act
        List<PublicationDetails> result = controller.getListOfPublications(_user);
        //Assert
        assertEquals(List.of(details), result);
    }
}
