package TOPSECRET.controller;

import TOPSECRET.domain.ILibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationDetails;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOfItemsInMyLibraryControllerTest {
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
    void testListOfItemsInMyLibraryController(){
        // act & SUT
        new ListOfItemsInMyLibraryController(_iLibraryRepoDouble, _userDouble);
    }

    @Test
    void shouldReturnEmptyListWhenLibraryExistsButEmpty() {
        // Arrange
        when(_myLibraryDouble.getItemsInLibrary()).thenReturn(List.of());
        // SUT
        ListOfItemsInMyLibraryController controller = new ListOfItemsInMyLibraryController(_iLibraryRepoDouble, _userDouble);
        // Act
        List<PublicationDetails> result = controller.getListOfItemDetails(_userDouble);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfItemsInLibrary() {
        // Arrange
        PublicationDetails pDetailsDouble = mock(PublicationDetails.class);

        when(_iLibraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.getItemDetails()).thenReturn(List.of(pDetailsDouble));

        // SUT
        ListOfItemsInMyLibraryController controller = new ListOfItemsInMyLibraryController(_iLibraryRepoDouble, _userDouble);

        //Act
        List<PublicationDetails> result = controller.getListOfItemDetails(_userDouble);

        //Assert
        assertEquals(List.of(pDetailsDouble), result);
    }
}
