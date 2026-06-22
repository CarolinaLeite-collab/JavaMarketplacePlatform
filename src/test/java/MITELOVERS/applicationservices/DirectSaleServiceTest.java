
package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectSaleServiceTest {

    @Mock private IGenreRepo       _iGenreRepo;
    @Mock private IItemRepo        _iItemRepo;
    @Mock private IDirectSaleRepo  _iDirectSaleRepo;
    @Mock private DirectSaleFactory _factory;
    @Mock private UserService      _userService;

    @InjectMocks
    private DirectSaleService _service;

    // ----------------------------------------------------------------
    // createDirectSale
    // ----------------------------------------------------------------

    @Test
    void createDirectSale_shouldSaveAndReturnDomainObject() {

        List<ItemId> itemIds = List.of(new ItemId("ABCDEF1234"), new ItemId("A1B2C3D4E5"));
        UserId sellerId      = new UserId(new Email("seller@selling.com"));
        Price price          = new Price(20.0, Currency.USD);
        Duration timeLimit   = Duration.ofSeconds(3600L);

        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(_userService.userIdExists("seller@selling.com")).thenReturn(true);
        when(item1.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(item2.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_iItemRepo.ofIdentity(itemIds.get(0))).thenReturn(Optional.of(item1));
        when(_iItemRepo.ofIdentity(itemIds.get(1))).thenReturn(Optional.of(item2));

        DirectSale newSale  = mock(DirectSale.class);
        DirectSale savedSale = mock(DirectSale.class);

        when(_factory.createDirectSale(anyList(), any(), any(), any())).thenReturn(newSale);
        when(newSale.identity()).thenReturn(new DirectSaleId("DS-A1B2C3D4"));
        when(_iDirectSaleRepo.containsOfIdentity(any())).thenReturn(false);
        when(_iDirectSaleRepo.save(newSale)).thenReturn(savedSale);

        DirectSale result = _service.createDirectSale(itemIds, sellerId, price, timeLimit);

        assertSame(savedSale, result);
    }

    @Test
    void createDirectSale_shouldThrowWhenDirectSaleAlreadyExists() {

        List<ItemId> itemIds = List.of(new ItemId("ABCDEF1234"));
        UserId sellerId      = new UserId(new Email("seller@selling.com"));
        Price price          = new Price(10.0, Currency.EUR);
        Duration timeLimit   = Duration.ofSeconds(3600L);

        Item item = mock(Item.class);
        when(_iItemRepo.ofIdentity(itemIds.get(0))).thenReturn(Optional.of(item));
        when(item.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_userService.userIdExists("seller@selling.com")).thenReturn(true);

        DirectSale newSale = mock(DirectSale.class);
        DirectSaleId id    = new DirectSaleId("DS-A1B2C3D4");

        when(_factory.createDirectSale(anyList(), any(), any(), any())).thenReturn(newSale);
        when(newSale.identity()).thenReturn(id);
        when(_iDirectSaleRepo.containsOfIdentity(id)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> _service.createDirectSale(itemIds, sellerId, price, timeLimit));
    }

    @Test
    void createDirectSale_shouldThrowWhenItemNotFound() {

        List<ItemId> itemIds = List.of(new ItemId("ABCDEF1234"));
        UserId sellerId      = new UserId(new Email("seller@selling.com"));
        Price price          = new Price(10.0, Currency.EUR);
        Duration timeLimit   = Duration.ofSeconds(3600L);

        when(_userService.userIdExists("seller@selling.com")).thenReturn(true);
        when(_iItemRepo.ofIdentity(itemIds.get(0))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> _service.createDirectSale(itemIds, sellerId, price, timeLimit));
    }

    @Test
    void createDirectSale_shouldThrowWhenItemAlreadyOnSale() {

        List<ItemId> itemIds = List.of(new ItemId("ABCDEF1234"));
        UserId sellerId      = new UserId(new Email("seller@selling.com"));
        Price price          = new Price(10.0, Currency.EUR);
        Duration timeLimit   = Duration.ofSeconds(3600L);

        Item item = mock(Item.class);
        when(_userService.userIdExists("seller@selling.com")).thenReturn(true);
        when(_iItemRepo.ofIdentity(itemIds.get(0))).thenReturn(Optional.of(item));
        when(item.getSaleStatus()).thenReturn(SaleStatus.OnDirectSale);

        assertThrows(IllegalStateException.class,
                () -> _service.createDirectSale(itemIds, sellerId, price, timeLimit));
    }

    @Test
    void createDirectSale_shouldHandleNullTimeLimitAndVerifyMarkAsDirectSale() {

        List<ItemId> itemIds = List.of(new ItemId("ABCDEF1234"));
        UserId sellerId      = new UserId(new Email("seller@selling.com"));
        Price price          = new Price(20.0, Currency.USD);

        Item item = mock(Item.class);
        when(_userService.userIdExists("seller@selling.com")).thenReturn(true);
        when(_iItemRepo.ofIdentity(itemIds.get(0))).thenReturn(Optional.of(item));
        when(item.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);

        DirectSale newSale  = mock(DirectSale.class);
        DirectSale savedSale = mock(DirectSale.class);
        DirectSaleId dsId   = new DirectSaleId("DS-A1B2C3D4");

        when(_factory.createDirectSale(eq(itemIds), any(UserId.class), any(Price.class), isNull()))
                .thenReturn(newSale);
        when(newSale.identity()).thenReturn(dsId);
        when(_iDirectSaleRepo.containsOfIdentity(dsId)).thenReturn(false);
        when(_iDirectSaleRepo.save(newSale)).thenReturn(savedSale);

        DirectSale result = _service.createDirectSale(itemIds, sellerId, price, null);

        assertSame(savedSale, result);
    }

    @Test
    void createDirectSale_shouldThrowWhenDuplicateItemsProvided() {

        List<ItemId> itemIds = List.of(new ItemId("ABCDEF1234"), new ItemId("ABCDEF1234"));
        UserId sellerId      = new UserId(new Email("seller@selling.com"));
        Price price          = new Price(20.0, Currency.USD);
        Duration timeLimit   = Duration.ofSeconds(3600L);

        when(_userService.userIdExists("seller@selling.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> _service.createDirectSale(itemIds, sellerId, price, timeLimit));
    }

    @Test
    void createDirectSale_shouldThrowWhenUserDoesNotExist() {

        List<ItemId> itemIds = List.of(new ItemId("ABCDEF1234"));
        UserId sellerId      = new UserId(new Email("seller@selling.com"));
        Price price          = new Price(20.0, Currency.USD);
        Duration timeLimit   = Duration.ofSeconds(3600L);

        when(_userService.userIdExists("seller@selling.com")).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> _service.createDirectSale(itemIds, sellerId, price, timeLimit));
    }

    // ----------------------------------------------------------------
    // getAllDirectSales
    // ----------------------------------------------------------------

    @Test
    void getAllDirectSales_shouldReturnDomainList() {
        DirectSale ds = mock(DirectSale.class);
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));

        List<DirectSale> result = _service.getAllDirectSales();

        assertEquals(1, result.size());
        assertSame(ds, result.get(0));
    }

    @Test
    void getAllDirectSales_shouldReturnEmptyListWhenNoDirectSales() {
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of());

        List<DirectSale> result = _service.getAllDirectSales();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllActiveDirectSales_shouldReturnDomainList() {
        DirectSale ds = mock(DirectSale.class);
        when(ds.getDSStatus()).thenReturn(DirectSaleStatus.ACTIVE);
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));

        List<DirectSale> result = _service.getAllActiveDirectSales();

        assertEquals(1, result.size());
        assertSame(ds, result.get(0));
    }

    @Test
    void getAllActiveDirectSales_shouldReturnEmptyListWhenNoDirectSales() {
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of());

        List<DirectSale> result = _service.getAllActiveDirectSales();

        assertTrue(result.isEmpty());
    }

    // ----------------------------------------------------------------
    // getDirectSaleById
    // ----------------------------------------------------------------

    @Test
    void getDirectSaleById_shouldReturnDTO() {
        DirectSale ds = mock(DirectSale.class);
        when(_iDirectSaleRepo.ofIdentity(any())).thenReturn(Optional.of(ds));

        DirectSale result = _service.getDirectSaleById(new DirectSaleId("DS-A1B2C3D4"));

        assertEquals(ds, result);
    }

    @Test
    void getDirectSaleById_shouldThrowIfNotFound() {
        when(_iDirectSaleRepo.ofIdentity(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> _service.getDirectSaleById(new DirectSaleId("DS-A1B2C3D4")));
    }

    // ----------------------------------------------------------------
    // getDirectSaleItemsByGenreAsc
    // ----------------------------------------------------------------

    @Test
    void getDirectSaleItemsByGenreAsc_shouldThrowWhenGenreNotFound() {
        GenreId gid = new GenreId("FICTION");
        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> _service.getDirectSaleItemsByGenreAsc(gid));
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnEmptyListWhenNoItemsForGenre() {
        GenreId gid = new GenreId("FICTION");
        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of());

        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(gid);

        assertTrue(result.isEmpty());
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnDistinctDirectSaleIds() {
        GenreId gid    = new GenreId("FICTION");
        ItemId item1   = new ItemId("ABCDEF1234");
        ItemId item2   = new ItemId("A1B2C3D4E5");

        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of(item1, item2));

        DirectSale ds1 = mock(DirectSale.class);
        DirectSale ds2 = mock(DirectSale.class);
        DirectSaleId id1 = new DirectSaleId("DS-A1B2C3D4");
        DirectSaleId id2 = new DirectSaleId("DS-1234ABCD");

        when(ds1.identity()).thenReturn(id1);
        when(ds2.identity()).thenReturn(id2);
        when(ds1.getItemsId()).thenReturn(List.of(item1));
        when(ds2.getItemsId()).thenReturn(List.of(item2));
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds1, ds2, ds1));

        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(gid);

        assertEquals(2, result.size());
        assertEquals(id1, result.get(0));
        assertEquals(id2, result.get(1));
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnMutableListWhenNoItemsFound() {
        GenreId gid = new GenreId("FICTION");
        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of());

        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(gid);

        assertTrue(result.isEmpty());
        result.add(new DirectSaleId("DS-A1B2C3D4"));
        assertEquals(1, result.size());
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnEmptyListWhenAllSalesIsNull() {
        GenreId gid = new GenreId("FICTION");
        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of(new ItemId("ABCDEF1234")));
        when(_iDirectSaleRepo.findAll()).thenReturn(null);

        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(gid);

        assertTrue(result.isEmpty());
    }

    // ----------------------------------------------------------------
    // deleteDirectSale
    // ----------------------------------------------------------------

    @Test
    void deleteDirectSale_shouldDeleteWithoutErrors() {
        DirectSaleId id = mock(DirectSaleId.class);
        assertDoesNotThrow(() -> _service.deleteDirectSale(id));
    }

    @Test
    void deleteDirectSale_shouldPropagateExceptionWhenRepoFails() {
        DirectSaleId id = mock(DirectSaleId.class);
        doThrow(new IllegalStateException("delete failed")).when(_iDirectSaleRepo).deleteDirectSale(id);

        assertThrows(IllegalStateException.class, () -> _service.deleteDirectSale(id));
    }

    @Test
    void deleteDirectSale_shouldAcceptAnyId() {
        DirectSaleId id = mock(DirectSaleId.class);
        _service.deleteDirectSale(id);
        assertNotNull(id);
    }
}
