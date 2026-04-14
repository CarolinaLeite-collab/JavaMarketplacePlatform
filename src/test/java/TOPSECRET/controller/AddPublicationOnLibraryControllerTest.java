package TOPSECRET.controller;

import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddPublicationOnLibraryControllerTest {

    @Test
    void shouldReturnLibraryOfUser() {
        //arrange
        ILibraryRepo _iLibraryRepoDouble = mock(ILibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        IItemRepo _iItemRepoDouble = mock(IItemRepo.class);
        UserId _userIdDouble = mock(UserId.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_libraryDouble);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_iLibraryRepoDouble, _libraryDouble, _iItemRepoDouble, _userIdDouble);

        //act
        Library result = controller.getMyLibrary(_userIdDouble);

        //assert
        assertEquals(_libraryDouble, result);
        verify(_iLibraryRepoDouble).findLibraryByUserId(_userIdDouble);
    }

    @Test
    void shouldReturnAllItemsFromLibrary() {
        //arrange
        ILibraryRepo _iLibraryRepoDouble = mock(ILibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        IItemRepo _iItemRepoDouble = mock(IItemRepo.class);
        UserId _userIdDouble = mock(UserId.class);

        ItemId _itemIdDouble1 = mock(ItemId.class);
        ItemId _itemIdDouble2 = mock(ItemId.class);

        List<ItemId> items = List.of(_itemIdDouble1, _itemIdDouble2);

        when(_libraryDouble.getItemsIdInLibrary()).thenReturn(items);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_iLibraryRepoDouble, _libraryDouble, _iItemRepoDouble, _userIdDouble);

        //act
        List<ItemId> result = controller.getAllItems();

        //assert
        assertEquals(items, result);
        verify(_libraryDouble).getItemsIdInLibrary();
    }

    @Test
    void shouldReturnListOfAvailableItems() {
        //arrange
        ILibraryRepo _iLibraryRepoDouble = mock(ILibraryRepo.class);
        Library _myLibraryDouble = mock(Library.class);
        Library _libraryDouble = mock(Library.class);
        IItemRepo _iItemRepoDouble = mock(IItemRepo.class);
        UserId _userIdDouble = mock(UserId.class);

        ItemId _itemIdDouble1 = mock(ItemId.class);
        ItemId _itemIdDouble2 = mock(ItemId.class);

        List<ItemId> existingItemIds = List.of(_itemIdDouble1);
        List<ItemId> availableItemIds = List.of(_itemIdDouble2);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.getItemsIdInLibrary()).thenReturn(existingItemIds);
        when(_iItemRepoDouble.getDifferentOf(existingItemIds)).thenReturn(availableItemIds);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_iLibraryRepoDouble, _libraryDouble, _iItemRepoDouble, _userIdDouble);

        //act
        List<ItemId> result = controller.getListOfAvailableItems(_userIdDouble);

        //assert
        assertEquals(availableItemIds, result);

        verify(_iLibraryRepoDouble).findLibraryByUserId(_userIdDouble);
        verify(_myLibraryDouble).getItemsIdInLibrary();
        verify(_iItemRepoDouble).getDifferentOf(existingItemIds);
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableItemsExist() {
        // arrange
        ILibraryRepo _iLibraryRepoDouble = mock(ILibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        Library _myLibraryDouble = mock(Library.class);
        IItemRepo _iItemRepoDouble = mock(IItemRepo.class);
        UserId _userIdDouble = mock(UserId.class);

        ItemId _itemIdDouble1 = mock(ItemId.class);

        List<ItemId> existingItemIds = List.of(_itemIdDouble1);
        List<ItemId> availableItemIds = List.of();

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.getItemsIdInLibrary()).thenReturn(existingItemIds);
        when(_iItemRepoDouble.getDifferentOf(existingItemIds)).thenReturn(availableItemIds);

        // SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_iLibraryRepoDouble, _libraryDouble, _iItemRepoDouble, _userIdDouble);

        // act
        List<ItemId> result = controller.getListOfAvailableItems(_userIdDouble);

        // assert
        assertTrue(result.isEmpty());
        verify(_iLibraryRepoDouble).findLibraryByUserId(_userIdDouble);
        verify(_myLibraryDouble).getItemsIdInLibrary();
        verify(_iItemRepoDouble).getDifferentOf(existingItemIds);
    }

    @Test
    void shouldAddItemToLibrary() {
        //arrange
        ILibraryRepo _iLibraryRepoDouble = mock(ILibraryRepo.class);
        Library _myLibraryDouble = mock(Library.class);
        Library _libraryDouble = mock(Library.class);
        IItemRepo _iItemRepoDouble = mock(IItemRepo.class);
        UserId _userIdDouble = mock(UserId.class);
        ItemId _itemIdDouble = mock(ItemId.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.addItemIdToLibrary(_itemIdDouble)).thenReturn(true);

        //SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_iLibraryRepoDouble, _libraryDouble, _iItemRepoDouble,
                _userIdDouble);

        //act
        boolean result = controller.addItemToLibrary(_itemIdDouble, _userIdDouble);

        //assert
        assertTrue(result);

        verify(_iLibraryRepoDouble).findLibraryByUserId(_userIdDouble);
        verify(_myLibraryDouble).addItemIdToLibrary(_itemIdDouble);
    }

    @Test
    void shouldReturnFalseWhenItemNotAddedToLibrary() {
        // arrange
        ILibraryRepo _iLibraryRepoDouble = mock(ILibraryRepo.class);
        Library _libraryDouble = mock(Library.class);
        Library _myLibraryDouble = mock(Library.class);
        IItemRepo _iItemRepoDouble = mock(IItemRepo.class);
        UserId _userIdDouble = mock(UserId.class);
        ItemId _itemIdDouble = mock(ItemId.class);

        when(_iLibraryRepoDouble.findLibraryByUserId(_userIdDouble)).thenReturn(_myLibraryDouble);
        when(_myLibraryDouble.addItemIdToLibrary(_itemIdDouble)).thenReturn(false);

        // SUT
        AddPublicationOnLibraryController controller = new AddPublicationOnLibraryController(_iLibraryRepoDouble, _libraryDouble, _iItemRepoDouble, _userIdDouble);

        // act
        boolean result = controller.addItemToLibrary(_itemIdDouble, _userIdDouble);

        // assert
        assertFalse(result);
        verify(_iLibraryRepoDouble).findLibraryByUserId(_userIdDouble);
        verify(_myLibraryDouble).addItemIdToLibrary(_itemIdDouble);
    }
}
