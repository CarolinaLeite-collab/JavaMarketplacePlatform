package TOPSECRET.controller;

import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddPublicationOnLibraryControllerTest {

    private IItemRepo _itemRepoDouble;
    private ILibraryRepo _libraryRepoDouble;

    @BeforeEach
    void setUp() {

        _libraryRepoDouble = mock(ILibraryRepo.class);
        _itemRepoDouble = mock(IItemRepo.class);

    }

    @Test
    void shouldReturnAllAvailableItems() {

        //Arrange
        ItemId itemId1Double = mock(ItemId.class);
        ItemId itemId2Double = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);

        when(_itemRepoDouble.findAllKeys()).thenReturn(List.of(itemId1Double, itemId2Double));
        when(_libraryRepoDouble.existsItemIdInAnyLibrary(itemId1Double)).thenReturn(true);
        when(_libraryRepoDouble.existsItemIdInAnyLibrary(itemId2Double)).thenReturn(false);

        //SUT
        AddPublicationOnLibraryController ctl = new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userIdDouble);

        //Act
        List<ItemId> result = ctl.getListOfAvailableItemIds();

        //Assert
        assertEquals(1, result.size());

    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableItemsExist() {

        //Arrange
        ItemId itemId1Double = mock(ItemId.class);
        ItemId itemId2Double = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        UserId userId2Double = mock(UserId.class);

        when(_itemRepoDouble.findAllKeys()).thenReturn(List.of(itemId1Double, itemId2Double));
        when(_libraryRepoDouble.existsItemIdInAnyLibrary(itemId1Double)).thenReturn(true);
        when(_libraryRepoDouble.existsItemIdInAnyLibrary(itemId2Double)).thenReturn(true);

        //SUT
        AddPublicationOnLibraryController ctl = new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userIdDouble);

        //Act
        List<ItemId> result = ctl.getListOfAvailableItemIds();

        //Assert
        assertEquals(0, result.size());

    }

    @Test
    void shouldReturnTrueWhenItemAddedToLibrary() {

        //Arrange
        ItemId itemId1Double = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Library libraryDouble = mock(Library.class);

        when(_itemRepoDouble.findAllKeys()).thenReturn(List.of(itemId1Double));
        when(_libraryRepoDouble.existsItemIdInAnyLibrary(itemId1Double)).thenReturn(false);
        when(_libraryRepoDouble.addLibrary(userIdDouble)).thenReturn(libraryDouble);
        when(_libraryRepoDouble.findLibraryByUserId(userIdDouble)).thenReturn(libraryDouble);
        when(libraryDouble.addItemIdToLibrary(itemId1Double)).thenReturn(true);

        //SUT
        AddPublicationOnLibraryController ctl = new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userIdDouble);

        //Act
        boolean result = ctl.addItemIdToLibrary(itemId1Double, userIdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenItemNotAddedToLibrary() {

        //Arrange
        ItemId itemId1Double = mock(ItemId.class);
        UserId userIdDouble = mock(UserId.class);
        Library libraryDouble = mock(Library.class);

        when(_itemRepoDouble.findAllKeys()).thenReturn(List.of(itemId1Double));
        when(_libraryRepoDouble.existsItemIdInAnyLibrary(itemId1Double)).thenReturn(true);
        when(_libraryRepoDouble.addLibrary(userIdDouble)).thenReturn(libraryDouble);
        when(_libraryRepoDouble.findLibraryByUserId(userIdDouble)).thenReturn(libraryDouble);
        when(libraryDouble.addItemIdToLibrary(itemId1Double)).thenReturn(false);

        //SUT
        AddPublicationOnLibraryController ctl = new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userIdDouble);

        //Act
        boolean result = ctl.addItemIdToLibrary(itemId1Double, userIdDouble);

        //Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnFalseWhenItemAlreadyInLibrary() {
        // Arrange
        ItemId itemId = mock(ItemId.class);
        UserId userId = mock(UserId.class);
        Library library = mock(Library.class);

        when(_libraryRepoDouble.existsItemIdInAnyLibrary(itemId)).thenReturn(false);
        when(_libraryRepoDouble.findLibraryByUserId(userId)).thenReturn(library);
        when(library.addItemIdToLibrary(itemId)).thenReturn(false);

        AddPublicationOnLibraryController ctl =
                new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userId);

        // Act
        boolean result = ctl.addItemIdToLibrary(itemId, userId);

        // Assert
        assertFalse(result);
        verify(library).addItemIdToLibrary(itemId);
    }
}
