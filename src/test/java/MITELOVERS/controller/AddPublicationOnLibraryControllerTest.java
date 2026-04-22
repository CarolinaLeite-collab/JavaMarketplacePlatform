package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;

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

        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ItemId itemId1Double = mock(ItemId.class);
        ItemId itemId2Double = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);

        when(_itemRepoDouble.findAllKeys())
                .thenReturn(List.of(itemId1Double, itemId2Double));

        when(_libraryRepoDouble.findAll())
                .thenReturn(List.of(libraryDouble));

        when(libraryDouble.getItemsIdInLibrary())
                .thenReturn(List.of(itemId1Double));

        // SUT
        AddPublicationOnLibraryController ctl =
                new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userIdDouble);

        // Act
        List<ItemId> result = ctl.getListOfAvailableItemIds();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableItemsExist() {

        // Arrange
        ItemId itemId1Double = mock(ItemId.class);
        ItemId itemId2Double = mock(ItemId.class);
        Library libraryDouble = mock(Library.class);

        when(_itemRepoDouble.findAllKeys())
                .thenReturn(List.of(itemId1Double, itemId2Double));

        when(_libraryRepoDouble.findAll())
                .thenReturn(List.of(libraryDouble));

        when(libraryDouble.getItemsIdInLibrary())
                .thenReturn(List.of(itemId1Double, itemId2Double));

        // SUT
        AddPublicationOnLibraryController ctl =
                new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, mock(UserId.class));

        // Act
        List<ItemId> result = ctl.getListOfAvailableItemIds();

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllItemsWhenNoLibrariesExist() {

        // Arrange
        ItemId itemId1 = mock(ItemId.class);
        ItemId itemId2 = mock(ItemId.class);

        when(_itemRepoDouble.findAllKeys())
                .thenReturn(List.of(itemId1, itemId2));

        when(_libraryRepoDouble.findAll())
                .thenReturn(List.of());

        // SUT
        AddPublicationOnLibraryController ctl =
                new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, mock(UserId.class));

        // Act
        List<ItemId> result = ctl.getListOfAvailableItemIds();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldSuccessfullyAddPublicationOnLibrary() {

        // Arrange
        ItemId itemId = mock(ItemId.class);
        UserId userId = mock(UserId.class);
        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(userId))
                    .thenReturn(libraryId);

            when(_libraryRepoDouble.findAll())
                    .thenReturn(List.of(library));

            when(library.getItemsIdInLibrary())
                    .thenReturn(List.of());

            when(_libraryRepoDouble.ofIdentity(libraryId))
                    .thenReturn(Optional.of(library));

            when(library.addItemIdToLibrary(itemId))
                    .thenReturn(true);

            // SUT
            AddPublicationOnLibraryController ctl =
                    new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userId);

            // Act
            boolean result = ctl.addItemIdToLibrary(itemId, userId);

            // Assert
            assertTrue(result);
        }
    }

    @Test
    void shouldThrowWhenLibraryDoesNotExist() {

        // Arrange
        ItemId itemId = mock(ItemId.class);
        UserId userId = mock(UserId.class);
        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(userId))
                    .thenReturn(libraryId);

            when(_libraryRepoDouble.ofIdentity(libraryId))
                    .thenReturn(Optional.empty());

            // SUT
            AddPublicationOnLibraryController ctl =
                    new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userId);

            // Act + Assert
            assertThrows(IllegalStateException.class,
                    () -> ctl.addItemIdToLibrary(itemId, userId));
        }
    }

    @Test
    void shouldReturnFalseWhenItemAlreadyInLibrary() {

        // Arrange
        ItemId itemId = mock(ItemId.class);
        UserId userId = mock(UserId.class);
        LibraryId libraryId = mock(LibraryId.class);
        Library library = mock(Library.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(userId))
                    .thenReturn(libraryId);

            when(library.getItemsIdInLibrary())
                    .thenReturn(List.of(itemId));

            when(_libraryRepoDouble.findAll())
                    .thenReturn(List.of(library));

            // SUT
            AddPublicationOnLibraryController ctl =
                    new AddPublicationOnLibraryController(_libraryRepoDouble, _itemRepoDouble, userId);

            // Act
            boolean result = ctl.addItemIdToLibrary(itemId, userId);

            // Assert
            assertFalse(result);
        }
    }
}
