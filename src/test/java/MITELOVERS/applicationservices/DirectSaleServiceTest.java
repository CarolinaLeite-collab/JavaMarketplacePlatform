package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectSaleServiceTest {

    @Mock
    private IGenreRepo _iGenreRepo;

    @Mock
    private IItemRepo _iItemRepo;

    @Mock
    private IDirectSaleRepo _iDirectSaleRepo;

    @Mock
    private DirectSaleFactory _factory;

    @Mock
    private UserService _userService;

    @InjectMocks
    private DirectSaleService _service;

    @Test
    void createDirectSale_shouldSaveAndReturnDomainObject() {

        // Arrange
        DirectSaleRequestDTO request = new DirectSaleRequestDTO(
                List.of("ABCDEF1234", "A1B2C3D4E5"),
                20.0,
                "USD",
                3600L
        );

        String email = "seller@selling.com";

        List<ItemId> itemIds = List.of(
                new ItemId("ABCDEF1234"),
                new ItemId("A1B2C3D4E5")
        );

        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(_userService.userIdExists(email)).thenReturn(true);
        when(item1.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(item2.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_iItemRepo.ofIdentity(itemIds.get(0))).thenReturn(Optional.of(item1));
        when(_iItemRepo.ofIdentity(itemIds.get(1))).thenReturn(Optional.of(item2));

        DirectSale newSale = mock(DirectSale.class);
        DirectSale savedSale = mock(DirectSale.class);

        when(_factory.createDirectSale(anyList(), any(), any(), any())).thenReturn(newSale);
        when(newSale.identity()).thenReturn(new DirectSaleId("DS-A1B2C3D4"));
        when(_iDirectSaleRepo.containsOfIdentity(any())).thenReturn(false);
        when(_iDirectSaleRepo.save(newSale)).thenReturn(savedSale);

        // Act
        DirectSale result = _service.createDirectSale(request, email);

        // Assert
        assertSame(savedSale, result);
    }

    @Test
    void createDirectSale_shouldThrowWhenDirectSaleAlreadyExists() {

        // Arrange
        DirectSaleRequestDTO request = new DirectSaleRequestDTO(
                List.of("ABCDEF1234"),
                10.0,
                "EUR",
                3600L
        );

        String email = "seller@selling.com";

        ItemId itemId = new ItemId("ABCDEF1234");
        Item item = mock(Item.class);

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_userService.userIdExists(email)).thenReturn(true);

        DirectSale newSale = mock(DirectSale.class);
        DirectSaleId id = new DirectSaleId("DS-A1B2C3D4");

        when(_factory.createDirectSale(anyList(), any(), any(), any())).thenReturn(newSale);
        when(newSale.identity()).thenReturn(id);
        when(_iDirectSaleRepo.containsOfIdentity(id)).thenReturn(true);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _service.createDirectSale(request, email)
        );
    }

    @Test
    void createDirectSale_shouldThrowWhenItemNotFound() {

        // Arrange
        DirectSaleRequestDTO request = new DirectSaleRequestDTO(
                List.of("ABCDEF1234"),
                10.0,
                "EUR",
                3600L
        );

        String email = "seller@selling.com";

        ItemId itemId = new ItemId("ABCDEF1234");

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> _service.createDirectSale(request, email)
        );
    }

    @Test
    void createDirectSale_shouldThrowWhenItemAlreadyOnSale() {

        // Arrange
        DirectSaleRequestDTO request = new DirectSaleRequestDTO(
                List.of("ABCDEF1234"),
                10.0,
                "EUR",
                3600L
        );

        String email = "seller@selling.com";

        ItemId itemId = new ItemId("ABCDEF1234");
        Item item = mock(Item.class);

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getSaleStatus()).thenReturn(SaleStatus.OnDirectSale); // already on sale

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _service.createDirectSale(request, email)
        );
    }

    @Test
    void createDirectSale_shouldHandleNullTimeLimitAndVerifyMarkAsDirectSale() {

        // Arrange
        DirectSaleRequestDTO request = new DirectSaleRequestDTO(
                List.of("ABCDEF1234"),
                20.0,
                "USD",
                null
        );

        String email = "seller@selling.com";

        ItemId itemId = new ItemId("ABCDEF1234");
        Item item = mock(Item.class);

        when(_userService.userIdExists(email)).thenReturn(true);
        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);

        DirectSale newSale = mock(DirectSale.class);
        DirectSale savedSale = mock(DirectSale.class);
        DirectSaleId dsId = new DirectSaleId("DS-A1B2C3D4");

        when(_factory.createDirectSale(eq(List.of(itemId)), any(UserId.class), any(Price.class), isNull()))
                .thenReturn(newSale);

        when(newSale.identity()).thenReturn(dsId);
        when(_iDirectSaleRepo.containsOfIdentity(dsId)).thenReturn(false);
        when(_iDirectSaleRepo.save(newSale)).thenReturn(savedSale);

        // Act
        DirectSale result = _service.createDirectSale(request,email);

        // Assert
        assertSame(savedSale, result);
    }

    @Test
    void createDirectSale_shouldThrowWhenDuplicateItemsProvided() {

        // Arrange
        DirectSaleRequestDTO request = new DirectSaleRequestDTO(
                List.of("ABCDEF1234", "ABCDEF1234"), // duplicate
                20.0,
                "USD",
                3600L
        );

        String email = "seller@selling.com";

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> _service.createDirectSale(request, email)
        );
    }

    // ------------------------------------------------------------
    // getAllDirectSales
    // ------------------------------------------------------------

    @Test
    void getAllDirectSales_shouldReturnDomainList() {

        // Arrange
        DirectSale ds = mock(DirectSale.class);

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));

        // Act (SUT)
        List<DirectSale> result = _service.getAllDirectSales();

        // Assert
        assertEquals(1, result.size());
        assertSame(ds, result.get(0));
    }

    @Test
    void getAllDirectSales_shouldReturnEmptyListWhenNoDirectSales() {

        // Arrange
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of());

        // Act
        List<DirectSale> result = _service.getAllDirectSales();

        // Assert
        assertTrue(result.isEmpty());
    }

    // ------------------------------------------------------------
    // getDirectSaleById
    // ------------------------------------------------------------

    @Test
    void getDirectSaleById_shouldReturnDTO() {

        // Arrange
        DirectSale ds = mock(DirectSale.class);

        when(_iDirectSaleRepo.ofIdentity(any())).thenReturn(Optional.of(ds));

        // Act (SUT)
        DirectSale result = _service.getDirectSaleById("DS-A1B2C3D4");

        // Assert
        assertEquals(ds, result);
    }

    @Test
    void getDirectSaleById_shouldThrowIfNotFound() {

        // Arrange
        when(_iDirectSaleRepo.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                NoSuchElementException.class,
                () -> _service.getDirectSaleById("DS-A1B2C3D4")
        );
    }

    // ------------------------------------------------------------
    // getDirectSaleItemsByGenreAsc
    // ------------------------------------------------------------

    @Test
    void getDirectSaleItemsByGenreAsc_shouldThrowWhenGenreNotFound() {

        // Arrange
        String genreId = "FICTION";
        GenreId gid = new GenreId(genreId);

        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(false);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> _service.getDirectSaleItemsByGenreAsc(genreId)
        );
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnEmptyListWhenNoItemsForGenre() {

        // Arrange
        String genreId = "FICTION";
        GenreId gid = new GenreId(genreId);

        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of());

        // Act
        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnDistinctDirectSaleIds() {

        // Arrange
        String genreId = "FICTION";
        GenreId gid = new GenreId(genreId);

        ItemId item1 = new ItemId("ABCDEF1234");
        ItemId item2 = new ItemId("A1B2C3D4E5");

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

        // Act
        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(id1, result.get(0));
        assertEquals(id2, result.get(1));
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnEmptyListWhenGenreIdIsNullOrEmpty() {

        // Arrange
        String nullGenre = null;
        String emptyGenre = "";
        String blankGenre = "   ";

        // Act
        List<DirectSaleId> resultNull = _service.getDirectSaleItemsByGenreAsc(nullGenre);
        List<DirectSaleId> resultEmpty = _service.getDirectSaleItemsByGenreAsc(emptyGenre);
        List<DirectSaleId> resultBlank = _service.getDirectSaleItemsByGenreAsc(blankGenre);

        // Assert
        assertNotNull(resultNull);
        assertNotNull(resultEmpty);
        assertNotNull(resultBlank);

        assertDoesNotThrow(() -> resultNull.add(new DirectSaleId("DS-A1B2C3D4")));
        assertDoesNotThrow(() -> resultEmpty.add(new DirectSaleId("DS-1234ABCD")));
        assertDoesNotThrow(() -> resultBlank.add(new DirectSaleId("DS-E5F6G7H8")));
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnMutableListWhenNoItemsFound() {

        // Arrange
        String genreId = "FICTION";
        GenreId gid = new GenreId(genreId);
        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of());

        // Act
        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertTrue(result.isEmpty());
        int initialSize = result.size();
        result.add(new DirectSaleId("DS-A1B2C3D4"));
        assertEquals(initialSize + 1, result.size());
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnEmptyListWhenAllSalesIsNull() {

        // Arrange
        String genreId = "FICTION";
        GenreId gid = new GenreId(genreId);
        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of(new ItemId("ABCDEF1234")));
        when(_iDirectSaleRepo.findAll()).thenReturn(null);

        // Act
        List<DirectSaleId> result = _service.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertTrue(result.isEmpty());
        int initialSize = result.size();
        result.add(new DirectSaleId("DS-A1B2C3D4"));
        assertEquals(initialSize + 1, result.size());
    }

    // -------------
    // DELETE tests
    // -------------
    @Test
    void deleteDirectSale_shouldDeleteWithoutErrors() {

        // Arrange
        DirectSaleId id = mock(DirectSaleId.class);

        // Act
        assertDoesNotThrow(() -> _service.deleteDirectSale(id));

        // Assert
        assertTrue(true); // state-based: no exception means success
    }


    @Test
    void deleteDirectSale_shouldPropagateExceptionWhenRepoFails() {

        // Arrange
        DirectSaleId id = mock(DirectSaleId.class);

        doThrow(new IllegalStateException("delete failed"))
                .when(_iDirectSaleRepo)
                .deleteDirectSale(id);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _service.deleteDirectSale(id));
    }

    @Test
    void deleteDirectSale_shouldAcceptAnyId() {

        // Arrange
        DirectSaleId id = mock(DirectSaleId.class);

        // Act
        _service.deleteDirectSale(id);

        // Assert
        assertNotNull(id);
    }

}