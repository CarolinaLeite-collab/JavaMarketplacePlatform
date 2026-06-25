package MITELOVERS.applicationservices;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOfItemsServiceTest {

    // SUT
    @InjectMocks
    ListOfItemsService _service;
    @Mock
    IListOfItemsRepo _listRepo;
    @Mock
    ListOfItemsFactory _factory;
    @Mock
    IGenreRepo _genreRepo;
    @Mock
    IItemRepo _itemRepo;
    @Mock
    private UserId _userId;
    @Mock
    private GenreId _genreId;
    @Mock
    private GenreId _genreId2;
    @Mock
    private ListOfItemsId _listId;
    @Mock
    private ItemId _itemId;
    @Mock
    private ItemId _itemId2;
    @Mock
    private Name _listName;

    @Test
    void getUserLists_shouldReturnLists() {
        ListOfItems list = mock(ListOfItems.class);
        when(_listRepo.findListOfItemsByUserId(_userId))
                .thenReturn(List.of(list));

        List<ListOfItems> result = _service.getUserLists(_userId);

        assertEquals(1, result.size());
        assertSame(list, result.get(0));
    }

    // ------------------------------------------------------------
    // GET LIST BY ID
    // ------------------------------------------------------------

    @Test
    void getListById_shouldReturnList() {
        ListOfItems list = mock(ListOfItems.class);
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.of(list));

        ListOfItems result = _service.getListById(_listId);

        assertSame(list, result);
    }

    @Test
    void getListById_shouldThrow_whenNotFound() {
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> _service.getListById(_listId));
    }

    // ------------------------------------------------------------
    // SAVE LIST
    // ------------------------------------------------------------

    @Test
    void save_shouldCreateList_whenValid() {
        when(_genreRepo.containsOfIdentity(_genreId)).thenReturn(true);
        when(_listRepo.findListOfItemsByUserId(_userId)).thenReturn(List.of());

        ListOfItems created = mock(ListOfItems.class);
        when(_factory.createListOfItems(_userId, _listName, _genreId)).thenReturn(created);
        when(_listRepo.save(created)).thenReturn(created);

        ListOfItems result = _service.save(_userId, _listName, _genreId);

        assertSame(created, result);
    }

    @Test
    void save_shouldThrow_whenGenreDoesNotExist() {
        when(_genreRepo.containsOfIdentity(_genreId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> _service.save(_userId, _listName, _genreId));
    }

    @Test
    void save_shouldThrow_whenDuplicateName() {
        ListOfItems existing = mock(ListOfItems.class);
        when(existing.getName()).thenReturn(_listName);

        when(_genreRepo.containsOfIdentity(_genreId)).thenReturn(true);
        when(_listRepo.findListOfItemsByUserId(_userId)).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> _service.save(_userId, _listName, _genreId));
    }

    // ------------------------------------------------------------
    // ADD ITEM
    // ------------------------------------------------------------

    @Test
    void addItemToList_shouldReturnUpdatedList() {
        ListOfItems list = mock(ListOfItems.class);

        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.of(list));
        when(_itemRepo.containsOfIdentity(_itemId)).thenReturn(true);
        when(_listRepo.save(list)).thenReturn(list);

        ListOfItems result = _service.addItemToList(_listId, _itemId);

        assertSame(list, result);
    }

    @Test
    void addItemToList_shouldThrow_whenItemIdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> _service.addItemToList(_listId, null));
    }

    @Test
    void addItemToList_shouldThrow_whenListNotFound() {
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> _service.addItemToList(_listId, _itemId));
    }

    @Test
    void addItemToList_shouldThrow_whenItemDoesNotExist() {
        ListOfItems list = mock(ListOfItems.class);
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.of(list));
        when(_itemRepo.containsOfIdentity(_itemId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> _service.addItemToList(_listId, _itemId));
    }

    // ------------------------------------------------------------
    // MAKE PUBLIC
    // ------------------------------------------------------------

    @Test
    void makePublic_shouldReturnList() {
        ListOfItems list = mock(ListOfItems.class);
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.of(list));
        when(_listRepo.save(list)).thenReturn(list);

        SharedDuration duration = new SharedDuration(5);

        ListOfItems result = _service.makePublic(_listId, duration);

        assertSame(list, result);
    }

    @Test
    void makePublic_shouldThrow_whenListNotFound() {
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> _service.makePublic(_listId, new SharedDuration(5)));
    }

    // ------------------------------------------------------------
    // MAKE PRIVATE
    // ------------------------------------------------------------

    @Test
    void makePrivate_shouldReturnList() {
        ListOfItems list = mock(ListOfItems.class);
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.of(list));
        when(_listRepo.save(list)).thenReturn(list);

        ListOfItems result = _service.makePrivate(_listId);

        assertSame(list, result);
    }

    @Test
    void makePrivate_shouldThrow_whenListNotFound() {
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> _service.makePrivate(_listId));
    }

    // ------------------------------------------------------------
    // FIND BY GENRE
    // ------------------------------------------------------------

    @Test
    void findByGenre_shouldReturnOnlyPublicListsOfGenre() {
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        when(list1.getGenreId()).thenReturn(_genreId);
        when(list2.getGenreId()).thenReturn(_genreId2);

        when(_listRepo.findAll()).thenReturn(List.of(list1, list2));

        List<ListOfItems> result = _service.findByGenre(_genreId);

        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
    }

    // ------------------------------------------------------------
    // DELETE LIST
    // ------------------------------------------------------------

    @Test
    void deleteList_shouldNotThrow() {
        assertDoesNotThrow(() -> _service.deleteList(_listId));
    }

    // ------------------------------------------------------------
    // GET PUBLIC LISTS
    // ------------------------------------------------------------

    @Test
    void getPublicLists_shouldReturnOnlyPublicLists() {
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        when(list1.isPrivate()).thenReturn(false);
        when(list2.isPrivate()).thenReturn(true);

        when(_listRepo.findAll()).thenReturn(List.of(list1, list2));

        List<ListOfItems> result = _service.getPublicLists();

        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
    }

    // ------------------------------------------------------------
    // GET ITEMS IN PUBLIC LIST
    // ------------------------------------------------------------

    @Test
    void getItemsInPublicList_shouldReturnItems() {
        ListOfItems list = mock(ListOfItems.class);
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.of(list));
        when(list.isPrivate()).thenReturn(false);

        List<ItemId> items = List.of(_itemId, _itemId2);
        when(list.getItemIds()).thenReturn(items);

        List<ItemId> result = _service.getItemsInPublicList(_listId);

        assertEquals(items, result);
    }

    @Test
    void getItemsInPublicList_shouldThrow_whenListIsPrivate() {
        ListOfItems list = mock(ListOfItems.class);
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.of(list));
        when(list.isPrivate()).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> _service.getItemsInPublicList(_listId));
    }

    @Test
    void getItemsInPublicList_shouldThrow_whenListNotFound() {
        when(_listRepo.ofIdentity(_listId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> _service.getItemsInPublicList(_listId));
    }

}
