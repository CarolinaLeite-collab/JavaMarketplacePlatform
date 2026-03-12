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
    private Item _itemDouble;

    @BeforeEach
    void setUp() {
        _userDouble = mock(User.class);

        _myLibraryDouble = mock(Library.class);

        _itemDouble = mock(Item.class);

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
        when(_myLibraryDouble.getItemsInLibrary()).thenReturn(List.of());
        // SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_libraryRepoDouble);
        // Act
        List<Item> result = controller.getListOfItems(_userDouble);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfItemsInLibrary() {
        // Arrange
        when(_myLibraryDouble.getItemsInLibrary()).thenReturn(List.of(_itemDouble));
        // SUT
        ListOfPublicationsInMyLibraryController controller = new ListOfPublicationsInMyLibraryController(_libraryRepoDouble);
        //Act
        List<Item> result = controller.getListOfItems(_userDouble);
        //Assert
        assertEquals(List.of(_itemDouble), result);
    }
}
