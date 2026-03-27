package TOPSECRET.controller;

import TOPSECRET.domain.ILibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfPublicationsInMyLibraryControllerTest {
    private User _userDouble;
    private ILibraryRepo _iLibraryRepoDouble;
    private Library _myLibraryDouble;

    @BeforeEach
    void setUp() {
        _userDouble = mock(User.class);

        _myLibraryDouble = mock(Library.class);

        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
    }

    @Test
    void testListOfPublicationsInMyLibraryController(){
        // act & SUT
        new ListOfPublicationsInMyLibraryController(_iLibraryRepoDouble, _userDouble);
    }

    @Test
    void shouldReturnEmptyListWhenLibraryExistsButEmpty() {
        // Arrange
        when(_myLibraryDouble.getItemsInLibrary()).thenReturn(List.of());
        // SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_iLibraryRepoDouble, _userDouble);
        // Act
        List<PublicationDetails> result = controller.getListOfPublicationDetails(_userDouble);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfItemsInLibrary() {
        // Arrange
        PublicationDetails publicationDetailsDouble = mock(PublicationDetails.class);

        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.getPublicationDetails()).thenReturn(List.of(publicationDetailsDouble));

        // SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_iLibraryRepoDouble, _userDouble);

        //Act
        List<PublicationDetails> result = controller.getListOfPublicationDetails(_userDouble);

        //Assert
        assertEquals(List.of(publicationDetailsDouble), result);
    }
}
