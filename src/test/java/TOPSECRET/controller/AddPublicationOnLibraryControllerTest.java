package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddPublicationOnLibraryControllerTest {

    @Test
    void shouldReturnLibraryOfUser() {

        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        ItemRepo _itemRepoDouble = mock(ItemRepo.class);
        User _userDouble = mock(User.class);

        when(_libraryRepoDouble.findByUser(_userDouble)).thenReturn(_libraryDouble);

        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        Library result = controller.getMyLibrary(_userDouble);

        assertEquals(_libraryDouble, result);
        verify(_libraryRepoDouble).findByUser(_userDouble);
    }

    @Test
    void shouldReturnAllItemsFromLibrary() {

        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        ItemRepo _itemRepoDouble = mock(ItemRepo.class);

        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        List<Item> items = List.of(_itemDouble1, _itemDouble2);

        when(_libraryDouble.getAllItems()).thenReturn(items);

        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        List<Item> result = controller.getAllItems();

        assertEquals(items, result);
        verify(_libraryDouble).getAllItems();
    }

    @Test
    void shouldReturnListOfAvailableItems() {

        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        ItemRepo _itemRepoDouble = mock(ItemRepo.class);
        User _userDouble = mock(User.class);

        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);

        List<Item> existingItems = List.of(_itemDouble1);
        List<Item> availableItems = List.of(_itemDouble2);

        when(_libraryRepoDouble.findByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.getAllItems()).thenReturn(existingItems);
        when(_itemRepoDouble.getDifferentOf(existingItems)).thenReturn(availableItems);

        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        List<Item> result = controller.getListOfAvailableItems(_userDouble);

        assertEquals(availableItems, result);

        verify(_libraryRepoDouble).findByUser(_userDouble);
        verify(_libraryDouble).getAllItems();
        verify(_itemRepoDouble).getDifferentOf(existingItems);
    }

    @Test
    void shouldAddItemToLibrary() {
        LibraryRepo _libraryRepoDouble = mock(LibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        ItemRepo _itemRepoDouble = mock(ItemRepo.class);
        User _userDouble = mock(User.class);
        Item _idemDouble = mock(Item.class);

        when(_libraryRepoDouble.findByUser(_userDouble)).thenReturn(_libraryDouble);
        when(_libraryDouble.addItemToLibrary(_idemDouble, _userDouble)).thenReturn(true);

        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_libraryRepoDouble, _libraryDouble, _itemRepoDouble);

        boolean result = controller.addItemToLibrary(_idemDouble, _userDouble);

        assertTrue(result);

        verify(_libraryRepoDouble).findByUser(_userDouble);
        verify(_libraryDouble).addItemToLibrary(_idemDouble, _userDouble);
    }
}