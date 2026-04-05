
package TOPSECRET.controller;

import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfItemsInMyLibraryControllerTest {
    private UserId _userIdDouble;
    private ILibraryRepo _iLibraryRepoDouble;
    private Library _myLibraryDouble;

    @BeforeEach
    void setUp() {
        _userIdDouble = mock(UserId.class);

        _myLibraryDouble = mock(Library.class);

        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_myLibraryDouble);
    }

    @Test
    void testListOfItemsInMyLibraryController(){
        // act & SUT
        new ListOfItemsInMyLibraryController(_iLibraryRepoDouble, _userIdDouble);
    }

    @Test
    void shouldReturnEmptyListWhenLibraryExistsButEmpty() {
        // Arrange
        when(_myLibraryDouble.getItemsInLibrary()).thenReturn(List.of());
        // SUT
        ListOfItemsInMyLibraryController controller = new ListOfItemsInMyLibraryController(_iLibraryRepoDouble, _userIdDouble);
        // Act
        List<PublicationDetails> result = controller.getListOfItemDetails(_userIdDouble);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfItemsInLibrary() {
        // Arrange
        PublicationDetails pDetailsDouble = mock(PublicationDetails.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.getItemDetails()).thenReturn(List.of(pDetailsDouble));

        // SUT
        ListOfItemsInMyLibraryController controller = new ListOfItemsInMyLibraryController(_iLibraryRepoDouble, _userIdDouble);

        //Act
        List<PublicationDetails> result = controller.getListOfItemDetails(_userIdDouble);

        //Assert
        assertEquals(List.of(pDetailsDouble), result);
    }
}