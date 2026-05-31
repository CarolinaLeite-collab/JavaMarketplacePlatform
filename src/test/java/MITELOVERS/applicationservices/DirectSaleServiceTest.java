package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.SaleStatus;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.mapper.DSFilteredItemsResponseMapper;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private DirectSaleResponseDTOMapper _responseMapper;

    @Mock
    private DSFilteredItemsResponseMapper _filteredResponseMapper;

    @InjectMocks
    private DirectSaleService _service;

    @Test
    void createDirectSale_shouldSaveAndReturnDTO() {

        // Arrange
        DirectSaleRequestDTO request = new DirectSaleRequestDTO(
                List.of("ABCDEF1234", "A1B2C3D4E5"),
                20.0,
                "USD",
                3600L
        );

        List<ItemId> itemIds = List.of(
                new ItemId("ABCDEF1234"),
                new ItemId("A1B2C3D4E5")
        );

        DirectSale newSale = mock(DirectSale.class);
        DirectSale savedSale = mock(DirectSale.class);
        DirectSaleResponseDTO expectedDTO =
                new DirectSaleResponseDTO(
                        "DS-A1B2C3D4",
                        List.of("ABCDEF1234", "A1B2C3D4E5"),
                        20.0,
                        "USD",
                        3600L,
                        Instant.now()
                );

        when(_factory.createDirectSale(anyList(), any(), any())).thenReturn(newSale);
        when(_iDirectSaleRepo.containsOfIdentity(newSale.identity())).thenReturn(false);

        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(item1.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(item2.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);

        when(_iItemRepo.ofIdentity(itemIds.get(0))).thenReturn(Optional.of(item1));
        when(_iItemRepo.ofIdentity(itemIds.get(1))).thenReturn(Optional.of(item2));

        when(_iDirectSaleRepo.save(newSale)).thenReturn(savedSale);
        when(_responseMapper.toResponseDTO(savedSale)).thenReturn(expectedDTO);

        // Act (SUT)
        DirectSaleResponseDTO result = _service.createDirectSale(request);

        // Assert
        assertEquals(expectedDTO, result);
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

        DirectSale newSale = mock(DirectSale.class);

        when(_factory.createDirectSale(anyList(), any(), any())).thenReturn(newSale);
        when(_iDirectSaleRepo.containsOfIdentity(newSale.identity())).thenReturn(true);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _service.createDirectSale(request)
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

        DirectSale newSale = mock(DirectSale.class);

        when(_factory.createDirectSale(anyList(), any(), any())).thenReturn(newSale);
        when(_iDirectSaleRepo.containsOfIdentity(newSale.identity())).thenReturn(false);

        ItemId itemId = new ItemId("ABCDEF1234");
        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> _service.createDirectSale(request)
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

        DirectSale newSale = mock(DirectSale.class);
        DirectSaleId dsId = new DirectSaleId("DS-A1B2C3D4");

        when(newSale.identity()).thenReturn(dsId);
        when(_factory.createDirectSale(anyList(), any(), any())).thenReturn(newSale);
        when(_iDirectSaleRepo.containsOfIdentity(dsId)).thenReturn(false);

        ItemId itemId = new ItemId("ABCDEF1234");
        Item item = mock(Item.class);

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getSaleStatus()).thenReturn(SaleStatus.OnDirectSale); // already on sale

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _service.createDirectSale(request)
        );
    }

    // ------------------------------------------------------------
    // getAllDirectSales
    // ------------------------------------------------------------

    @Test
    void getAllDirectSales_shouldReturnMappedList() {

        // Arrange
        DirectSale ds = mock(DirectSale.class);
        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
                        "DS-A1B2C3D4",
                        List.of("ABCDEF1234"),
                        10.0,
                        "EUR",
                        3600L,
                        Instant.now()
                );

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(_responseMapper.toResponseDTO(ds)).thenReturn(dto);

        // Act (SUT)
        List<DirectSaleResponseDTO> result = _service.getAllDirectSales();

        // Assert
        assertEquals(List.of(dto), result);
    }

    @Test
    void getAllDirectSales_shouldReturnEmptyListWhenNoDirectSales() {

        // Arrange
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of());

        // Act
        List<DirectSaleResponseDTO> result = _service.getAllDirectSales();

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
        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
                        "DS-A1B2C3D4",
                        List.of("ABCDEF1234"),
                        10.0,
                        "EUR",
                        3600L,
                        Instant.now()
                );

        String validId = "DS-A1B2C3D4";

        when(_iDirectSaleRepo.ofIdentity(any())).thenReturn(Optional.of(ds));
        when(_responseMapper.toResponseDTO(ds)).thenReturn(dto);

        // Act (SUT)
        DirectSaleResponseDTO result = _service.getDirectSaleById(validId);

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void getDirectSaleById_shouldThrowIfNotFound() {

        // Arrange
        when(_iDirectSaleRepo.ofIdentity(any())).thenReturn(Optional.empty());
        String validId = "DS-A1B2C3D4";

        // Act + Assert
        assertThrows(
                NoSuchElementException.class,
                () -> _service.getDirectSaleById(validId)
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
    void getDirectSaleItemsByGenreAsc_shouldReturnEmptyDTOWhenNoItemsForGenre() {

        // Arrange
        String genreId = "FICTION";
        GenreId gid = new GenreId(genreId);

        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of());

        // Act
        DSFilteredItemsResponseDTO result =
                _service.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertTrue(result.getDirectSales().isEmpty());
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnMappedDTOWithDistinctDirectSaleIds() {

        // Arrange
        String genreId = "FICTION";
        GenreId gid = new GenreId(genreId);

        ItemId item1 = new ItemId("ABCDEF1234");
        ItemId item2 = new ItemId("A1B2C3D4E5");

        when(_iGenreRepo.containsOfIdentity(gid)).thenReturn(true);
        when(_iItemRepo.findByGenreId(gid)).thenReturn(List.of(item1, item2));

        DirectSale ds1 = mock(DirectSale.class);
        DirectSale ds2 = mock(DirectSale.class);

        DirectSaleId dsId1 = new DirectSaleId("DS-A1B2C3D4");
        DirectSaleId dsId2 = new DirectSaleId("DS-1234ABCD");

        when(ds1.identity()).thenReturn(dsId1);
        when(ds2.identity()).thenReturn(dsId2);

        when(_iDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(List.of(item1, item2)))
                .thenReturn(List.of(ds1, ds2, ds1)); // duplicate ds1 to test distinct()

        List<String> mappedIds = List.of("DS-A1B2C3D4", "DS-1234ABCD");
        DSFilteredItemsResponseDTO expected =
                new DSFilteredItemsResponseDTO(
                        List.of(
                                new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4"),
                                new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-1234ABCD")
                        )
                );

        when(_filteredResponseMapper.toDTO(mappedIds)).thenReturn(expected);

        // Act
        DSFilteredItemsResponseDTO result =
                _service.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertEquals(2, result.getDirectSales().size());
        assertEquals("DS-A1B2C3D4", result.getDirectSales().get(0).getDirectSaleId());
        assertEquals("DS-1234ABCD", result.getDirectSales().get(1).getDirectSaleId());
    }

}