package MITELOVERS.applicationservices;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    static final String EDITION_NOT_FOUND = "Edition does not exist in the repository";
    static final String ITEM_NOT_FOUND    = "Item does not exist in the repository";

    IItemRepo    itemRepoDouble;
    ItemFactory  itemFactoryDouble;
    IEditionRepo editionRepoDouble;
    ItemService  itemService;

    @BeforeEach
    void setUp() {
        itemRepoDouble    = mock(IItemRepo.class);
        itemFactoryDouble = mock(ItemFactory.class);
        editionRepoDouble = mock(IEditionRepo.class);

        itemService = new ItemService(
                itemRepoDouble,
                itemFactoryDouble,
                editionRepoDouble
        );
    }

    // ----------------------------------------------------------------
    // Constructor null checks
    // ----------------------------------------------------------------

    @Test
    void constructorWithNullItemRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(null, itemFactoryDouble, editionRepoDouble));
    }

    @Test
    void constructorWithNullItemFactoryThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, null, editionRepoDouble));
    }

    @Test
    void constructorWithNullEditionRepoThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, null));
    }

    // ----------------------------------------------------------------
    // registerItem
    // ----------------------------------------------------------------

    @Test
    void registerItemEditionNotFoundThrowsNoSuchElementException() {
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(mock(EditionId.class), Condition.GOOD, new Description("copy")));

        assertEquals(EDITION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void registerItemValidRequestReturnsItem() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Item newItemDouble        = mock(Item.class);
        ItemId itemIdDouble       = mock(ItemId.class);
        Item savedItemDouble      = mock(Item.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.of(mock(Edition.class)));
        when(itemFactoryDouble.createItem(any(), any(), any())).thenReturn(newItemDouble);
        when(newItemDouble.identity()).thenReturn(itemIdDouble);
        when(itemRepoDouble.containsOfIdentity(itemIdDouble)).thenReturn(false);
        when(itemRepoDouble.save(newItemDouble)).thenReturn(savedItemDouble);

        // Act
        Item result = itemService.registerItem(
                editionIdDouble, Condition.GOOD, new Description("Nice copy"));

        // Assert
        assertSame(savedItemDouble, result);
        verify(itemRepoDouble).save(newItemDouble);
    }

    @Test
    void registerItemContainsItemButOfIdentityEmptyThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Item newItemDouble        = mock(Item.class);
        ItemId itemIdDouble       = mock(ItemId.class);

        when(itemIdDouble.toString()).thenReturn("TEST-ITEM-ID");
        when(editionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.of(mock(Edition.class)));
        when(itemFactoryDouble.createItem(any(), any(), any())).thenReturn(newItemDouble);
        when(newItemDouble.identity()).thenReturn(itemIdDouble);
        when(itemRepoDouble.containsOfIdentity(itemIdDouble)).thenReturn(true);
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("copy")));

        // Assert
        assertEquals("Item with id 'TEST-ITEM-ID' does not exist", ex.getMessage());
    }

    // ----------------------------------------------------------------
    // getAllItems
    // ----------------------------------------------------------------

    @Test
    void getAllItemsEmptyReturnsEmptyList() {
        when(itemRepoDouble.findAll()).thenReturn(List.of());

        List<Item> result = itemService.getAllItems();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllItemsReturnsListOfItems() {
        Item itemDouble = mock(Item.class);
        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));

        List<Item> result = itemService.getAllItems();

        assertEquals(1, result.size());
        assertSame(itemDouble, result.get(0));
    }

    // ----------------------------------------------------------------
    // getItemById
    // ----------------------------------------------------------------

    @Test
    void getItemByIdItemNotFoundThrowsNoSuchElementException() {
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.getItemById("3C5D126F8B"));

        assertEquals(ITEM_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getItemByIdValidIdReturnsItem() {
        Item itemDouble = mock(Item.class);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));

        Item result = itemService.getItemById("3C5D126F8B");

        assertSame(itemDouble, result);
    }

    @Test
    void registerItemAlreadyExistsReturnsExistingItem() {
        // Arrange
        EditionId editionIdDouble  = mock(EditionId.class);
        Item newItemDouble         = mock(Item.class);
        ItemId itemIdDouble        = mock(ItemId.class);
        Item existingItemDouble    = mock(Item.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble))
                .thenReturn(Optional.of(mock(Edition.class)));
        when(itemFactoryDouble.createItem(any(), any(), any())).thenReturn(newItemDouble);
        when(newItemDouble.identity()).thenReturn(itemIdDouble);
        when(itemRepoDouble.containsOfIdentity(itemIdDouble)).thenReturn(true);
        when(itemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(existingItemDouble));

        // Act
        Item result = itemService.registerItem(
                editionIdDouble, Condition.GOOD, new Description("copy"));

        // Assert
        assertSame(existingItemDouble, result);
    }
}