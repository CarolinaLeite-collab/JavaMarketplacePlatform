package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddPublicationOnLibraryControllerTest {

    @Test
    void shouldReturnLibraryOfUser() {
        //arrange
        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        MemoItemRepo _itemRepoDouble = mock(MemoItemRepo.class);
        User _userDouble = mock(User.class);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_libraryDouble);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        //act
        Library result = controller.getMyLibrary(_userDouble);

        //assert
        assertEquals(_libraryDouble, result);
        verify(_libraryRepoDouble).findLibraryByUser(_userDouble);
    }

    @Test
    void shouldReturnAllItemsFromLibrary() {
        //arrange
        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        MemoItemRepo _itemRepoDouble = mock(MemoItemRepo.class);

        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        List<Item> items = List.of(_itemDouble1, _itemDouble2);

        when(_libraryDouble.getItemsInLibrary()).thenReturn(items);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        //act
        List<Item> result = controller.getAllItems();

        //assert
        assertEquals(items, result);
        verify(_libraryDouble).getItemsInLibrary();
    }

    @Test
    void shouldReturnListOfAvailableItems() {
        //arrange
        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _myLibraryDouble = mock(Library.class);
        Library _libraryDouble = mock(Library.class);
        MemoItemRepo _itemRepoDouble = mock(MemoItemRepo.class);
        User _userDouble = mock(User.class);

        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        List<Item> existingItems = List.of(_itemDouble1);
        List<Item> availableItems = List.of(_itemDouble2);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.getItemsInLibrary()).thenReturn(existingItems);
        when(_itemRepoDouble.getDifferentOf(existingItems)).thenReturn(availableItems);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        //act
        List<Item> result = controller.getListOfAvailableItems(_userDouble);

        //assert
        assertEquals(availableItems, result);

        verify(_libraryRepoDouble).findLibraryByUser(_userDouble);
        verify(_myLibraryDouble).getItemsInLibrary();
        verify(_itemRepoDouble).getDifferentOf(existingItems);
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableItemsExist() {
        // arrange
        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        Library _myLibraryDouble = mock(Library.class);
        MemoItemRepo _itemRepoDouble = mock(MemoItemRepo.class);
        User _userDouble = mock(User.class);

        Item _itemDouble1 = mock(Item.class);

        List<Item> existingItems = List.of(_itemDouble1);
        List<Item> availableItems = List.of();

        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.getItemsInLibrary()).thenReturn(existingItems);
        when(_itemRepoDouble.getDifferentOf(existingItems)).thenReturn(availableItems);

        // SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        // act
        List<Item> result = controller.getListOfAvailableItems(_userDouble);

        // assert
        assertTrue(result.isEmpty());
        verify(_libraryRepoDouble).findLibraryByUser(_userDouble);
        verify(_myLibraryDouble).getItemsInLibrary();
        verify(_itemRepoDouble).getDifferentOf(existingItems);
    }

    @Test
    void shouldAddItemToLibrary() {
        //arrange
        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _myLibraryDouble = mock(Library.class);
        Library _libraryDouble = mock(Library.class);
        MemoItemRepo _itemRepoDouble = mock(MemoItemRepo.class);
        User _userDouble = mock(User.class);
        Item _idemDouble = mock(Item.class);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.addItemToLibrary(_idemDouble)).thenReturn(true);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        //act
        boolean result = controller.addItemToLibrary(_idemDouble, _userDouble);

        //assert
        assertTrue(result);

        verify(_libraryRepoDouble).findLibraryByUser(_userDouble);
        verify(_myLibraryDouble).addItemToLibrary(_idemDouble);
    }
    @Test
    void shouldReturnFalseWhenItemNotAddedToLibrary() {
        // arrange
        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        Library _myLibraryDouble = mock(Library.class);
        MemoItemRepo _itemRepoDouble = mock(MemoItemRepo.class);
        User _userDouble = mock(User.class);
        Item _itemDouble = mock(Item.class);

        when(_libraryRepoDouble.findLibraryByUser(_userDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.addItemToLibrary(_itemDouble)).thenReturn(false);

        // SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        // act
        boolean result = controller.addItemToLibrary(_itemDouble, _userDouble);

        // assert
        assertFalse(result);
        verify(_libraryRepoDouble).findLibraryByUser(_userDouble);
        verify(_myLibraryDouble).addItemToLibrary(_itemDouble);
    }
}