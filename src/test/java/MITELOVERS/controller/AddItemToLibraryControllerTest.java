package MITELOVERS.controller;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddItemToLibraryControllerTest {

    @Mock
    private IRepository<LibraryId, Library> libraryRepo;

    @Mock
    private IRepository<ItemId, Item> itemRepo;

    @Mock
    private UserId userId;

    @Mock
    private ItemId itemId1;

    @Mock
    private ItemId itemId2;

    @Mock
    private Library library;

    private AddItemToLibraryController controller;

    @BeforeEach
    void setUp() {
        controller = new AddItemToLibraryController(libraryRepo, itemRepo);
    }

    // ---------------------------------------------------------
    // getListOfAvailableItemIds()
    // ---------------------------------------------------------

    @Test
    void shouldReturnAllAvailableItems() {

        when(itemRepo.findAllKeys()).thenReturn(List.of(itemId1, itemId2));
        when(libraryRepo.findAll()).thenReturn(List.of(library));
        when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId1));

        List<ItemId> result = controller.getListOfAvailableItemIds();

        assertEquals(1, result.size());
        assertEquals(itemId2, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableItemsExist() {

        when(itemRepo.findAllKeys()).thenReturn(List.of(itemId1, itemId2));
        when(libraryRepo.findAll()).thenReturn(List.of(library));
        when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId1, itemId2));

        List<ItemId> result = controller.getListOfAvailableItemIds();

        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllItemsWhenNoLibrariesExist() {

        when(itemRepo.findAllKeys()).thenReturn(List.of(itemId1, itemId2));
        when(libraryRepo.findAll()).thenReturn(List.of());

        List<ItemId> result = controller.getListOfAvailableItemIds();

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // addItemIdToLibrary()
    // ---------------------------------------------------------

    @Test
    void shouldSuccessfullyAddPublicationOnLibrary() {

        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.findAll()).thenReturn(List.of(library));
            when(library.getItemsIdInLibrary()).thenReturn(List.of());
            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(library));
            when(library.addItemIdToLibrary(itemId1)).thenReturn(true);

            boolean result = controller.addItemIdToLibrary(itemId1, userId);

            assertTrue(result);
        }
    }

    @Test
    void shouldThrowWhenLibraryDoesNotExist() {

        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> controller.addItemIdToLibrary(itemId1, userId));
        }
    }

    @Test
    void shouldReturnFalseWhenItemAlreadyInLibrary() {

        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(userId)).thenReturn(libraryId);

            when(library.getItemsIdInLibrary()).thenReturn(List.of(itemId1));
            when(libraryRepo.findAll()).thenReturn(List.of(library));

            boolean result = controller.addItemIdToLibrary(itemId1, userId);

            assertFalse(result);
        }
    }

}
