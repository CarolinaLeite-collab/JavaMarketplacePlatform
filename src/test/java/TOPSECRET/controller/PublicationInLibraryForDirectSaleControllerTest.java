package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationInLibraryForDirectSaleControllerTest {

    private PublicationInLibraryForDirectSaleController controller;
    private LibraryRepo libraryRepo;
    private DirectSaleRepo directSaleRepo;
    private User testUser;
    private Library testLibrary;
    private Item testItem;
    private Price testPrice;
    private Period timeLimit;

    @BeforeEach
    void setUp() {
        libraryRepo = mock(LibraryRepo.class);
        directSaleRepo = mock(DirectSaleRepo.class);
        testUser = mock(User.class);
        testLibrary = mock(Library.class);
        testItem = mock(Item.class);
        testPrice = mock(Price.class);
        timeLimit = Period.ofDays(30);

        controller = new PublicationInLibraryForDirectSaleController(libraryRepo, directSaleRepo);
    }

    @Test
    void testConstructorPublicationInLibraryForDirectSaleController() {
        assertDoesNotThrow(() ->
                new PublicationInLibraryForDirectSaleController(libraryRepo, directSaleRepo));
    }

    @Test
    void testGetItemsInLibraryNullUser() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.getItemsInLibrary(null));
    }

    @Test
    void testGetItemsInLibraryForUserWithoutLibrary() {
        when(libraryRepo.findLibraryByUser(testUser))
                .thenThrow(new IllegalStateException("Library not found for user"));

        assertThrows(IllegalStateException.class, () ->
                controller.getItemsInLibrary(testUser));
    }

    @Test
    void testGetItemsInLibraryForUserWithEmptyLibrary() {
        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(testLibrary);
        when(testLibrary.getItemsInLibrary()).thenReturn(List.of());

        List<Item> result = controller.getItemsInLibrary(testUser);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetItemsInLibraryForUserWithItemsInLibrary() {
        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(testLibrary);
        when(testLibrary.getItemsInLibrary()).thenReturn(List.of(testItem));

        List<Item> result = controller.getItemsInLibrary(testUser);

        assertEquals(1, result.size());
        assertEquals(testItem, result.get(0));
    }

    @Test
    void testGetItemsInLibraryListIsImmutable() {
        when(libraryRepo.findLibraryByUser(testUser)).thenReturn(testLibrary);
        when(testLibrary.getItemsInLibrary()).thenReturn(List.of(testItem));

        List<Item> result = controller.getItemsInLibrary(testUser);

        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Item.class)));
    }

    @Test
    void testAddItemForDirectSaleWithNullItem() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.addItemForDirectSale(null, testPrice, timeLimit));
    }

    @Test
    void testAddItemForDirectSaleWithNullPrice() {
        assertThrows(IllegalArgumentException.class, () ->
                controller.addItemForDirectSale(testItem, null, timeLimit));
    }

    @Test
    void testAddItemForDirectSaleSuccess() {
        DirectSale directSale = mock(DirectSale.class);

        when(directSaleRepo.addDirectSale(testItem, testPrice, timeLimit))
                .thenReturn(directSale);

        DirectSale result = controller.addItemForDirectSale(testItem, testPrice, timeLimit);

        assertNotNull(result);
        assertEquals(directSale, result);
        verify(directSaleRepo).addDirectSale(testItem, testPrice, timeLimit);
        verify(testItem).setDirectSale(directSale);
    }

    @Test
    void testAddItemForDirectSaleWhenItemAlreadyInDirectSale() {
        when(directSaleRepo.addDirectSale(testItem, testPrice, timeLimit))
                .thenReturn(mock(DirectSale.class));

        doThrow(new IllegalStateException("Item is already in a direct sale."))
                .when(testItem).setDirectSale(any(DirectSale.class));

        assertThrows(IllegalStateException.class, () ->
                controller.addItemForDirectSale(testItem, testPrice, timeLimit));
    }
}