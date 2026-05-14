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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class AddItemToLibraryControllerTest {

    @Mock
    private IRepository<LibraryId, Library> _libraryRepo;

    @Mock
    private IRepository<ItemId, Item> _itemRepo;

    @Mock
    private UserId _userId;

    @Mock
    private ItemId _itemId1;

    @Mock
    private ItemId _itemId2;

    @Mock
    private Library _library;

    @InjectMocks
    private AddItemToLibraryController _controller;

    @BeforeEach
    void setUp() {
        _controller = new AddItemToLibraryController(_libraryRepo, _itemRepo);
    }

    // ---------------------------------------------------------
    // getListOfAvailableItemIds()
    // ---------------------------------------------------------

    @Test
    void shouldReturnAllAvailableItems() {

        when(_itemRepo.findAllKeys()).thenReturn(List.of(_itemId1, _itemId2));
        when(_libraryRepo.findAll()).thenReturn(List.of(_library));
        when(_library.getItemsIdInLibrary()).thenReturn(List.of(_itemId1));

        List<ItemId> result = _controller.getListOfAvailableItemIds();

        assertEquals(1, result.size());
        assertEquals(_itemId2, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableItemsExist() {

        when(_itemRepo.findAllKeys()).thenReturn(List.of(_itemId1, _itemId2));
        when(_libraryRepo.findAll()).thenReturn(List.of(_library));
        when(_library.getItemsIdInLibrary()).thenReturn(List.of(_itemId1, _itemId2));

        List<ItemId> result = _controller.getListOfAvailableItemIds();

        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAllItemsWhenNoLibrariesExist() {

        when(_itemRepo.findAllKeys()).thenReturn(List.of(_itemId1, _itemId2));
        when(_libraryRepo.findAll()).thenReturn(List.of());

        List<ItemId> result = _controller.getListOfAvailableItemIds();

        assertEquals(2, result.size());
    }

    // ---------------------------------------------------------
    // addItemIdToLibrary()
    // ---------------------------------------------------------

    @Test
    void shouldSuccessfullyAddPublicationOnLibrary() {

        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.findAll()).thenReturn(List.of(_library));
            when(_library.getItemsIdInLibrary()).thenReturn(List.of());
            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.of(_library));
            when(_library.addItemIdToLibrary(_itemId1)).thenReturn(true);

            boolean result = _controller.addItemIdToLibrary(_itemId1, _userId);

            assertTrue(result);
        }
    }

    @Test
    void shouldThrowWhenLibraryDoesNotExist() {

        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_libraryRepo.ofIdentity(libraryId)).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class,
                    () -> _controller.addItemIdToLibrary(_itemId1, _userId));
        }
    }

    @Test
    void shouldReturnFalseWhenItemAlreadyInLibrary() {

        LibraryId libraryId = mock(LibraryId.class);

        try (MockedStatic<LibraryId> mocked = mockStatic(LibraryId.class)) {

            mocked.when(() -> LibraryId.fromUserId(_userId)).thenReturn(libraryId);

            when(_library.getItemsIdInLibrary()).thenReturn(List.of(_itemId1));
            when(_libraryRepo.findAll()).thenReturn(List.of(_library));

            boolean result = _controller.addItemIdToLibrary(_itemId1, _userId);

            assertFalse(result);
        }
    }

}
