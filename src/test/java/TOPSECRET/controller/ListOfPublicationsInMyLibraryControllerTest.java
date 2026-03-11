package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfPublicationsInMyLibraryControllerTest {
    private User _userDouble;
    private LibraryRepo _libraryRepoDouble;
    private Library _myLibraryDouble;

    @BeforeEach
    void setUp() {
        _userDouble = mock(User.class);

        _myLibraryDouble = mock(Library.class);

        _libraryRepoDouble = mock(LibraryRepo.class);
        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
    }

    @Test
    void testListOfPublicationsInMyLibraryController(){
        // act & SUT
        new ListOfPublicationsInMyLibraryController(_libraryRepoDouble);
    }

    @Test
    void shouldReturnEmptyListWhenLibraryExistsButEmpty() {
        // Arrange
        when(_myLibraryDouble.getPublicationsInLibrary()).thenReturn(List.of());
        // SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_libraryRepoDouble);
        // Act
        List<PublicationDetails> result = controller.getListOfPublications(_userDouble);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfPublicationsInLibrary() {
        // Arrange
        PublicationDetails _detailsDouble = mock(PublicationDetails.class);
        when(_myLibraryDouble.getPublicationsInLibrary()).thenReturn(List.of(_detailsDouble));
        // SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_libraryRepoDouble);
        //Act
        List<PublicationDetails> result = controller.getListOfPublications(_userDouble);
        //Assert
        assertEquals(List.of(_detailsDouble), result);
    }

//    @Test
//    void shouldThrowException_whenLibraryNotFound() {
//        IllegalStateException exception = assertThrows(IllegalStateException.class,
//                () -> _libraryRepo.findLibraryByUser(_user)
//        );
//        assertEquals("Library not found for user: Zé Isep", exception.getMessage());
//    }
}
