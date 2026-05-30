package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.SaleStatus;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectSaleServiceTest {

    @Mock
    private ILibraryRepo _iLibraryRepo;

    @Mock
    private IItemRepo _iItemRepo;

    @Mock
    private IEditionRepo _iEditionRepo;

    @Mock
    private IPublicationRepo _iPublicationRepo;

    @Mock
    private IDirectSaleRepo _iDirectSaleRepo;

    @Mock
    private DirectSaleFactory _factory;

    @Mock
    private DirectSaleResponseDTOMapper _responseMapper;

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

        DirectSale newSale = mock(DirectSale.class);
        DirectSale savedSale = mock(DirectSale.class);
        DirectSaleResponseDTO expectedDTO = mock(DirectSaleResponseDTO.class);

        when(_factory.createDirectSale(anyList(), any(), any())).thenReturn(newSale);
        when(_iDirectSaleRepo.containsOfIdentity(any())).thenReturn(false);

        Item mockItem = mock(Item.class);
        when(mockItem.getSaleStatus()).thenReturn(SaleStatus.NotOnSale);
        when(_iItemRepo.ofIdentity(any())).thenReturn(Optional.of(mockItem));

        when(_iDirectSaleRepo.save(newSale)).thenReturn(savedSale);
        when(_responseMapper.toResponseDTO(savedSale)).thenReturn(expectedDTO);

        // Act (SUT)
        DirectSaleResponseDTO result = _service.createDirectSale(request);

        // Assert
        assertEquals(expectedDTO, result);
    }

    @Test
    void getAllDirectSales_shouldReturnMappedList() {
        // Arrange
        DirectSale ds = mock(DirectSale.class);
        DirectSaleResponseDTO dto = mock(DirectSaleResponseDTO.class);

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(_responseMapper.toResponseDTO(ds)).thenReturn(dto);

        // Act (SUT)
        List<DirectSaleResponseDTO> result = _service.getAllDirectSales();

        // Assert
        assertEquals(List.of(dto), result);
    }

    @Test
    void getDirectSaleById_shouldReturnDTO() {
        // Arrange
        DirectSale ds = mock(DirectSale.class);
        DirectSaleResponseDTO dto = mock(DirectSaleResponseDTO.class);

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
        assertThrows(NoSuchElementException.class,
                () -> _service.getDirectSaleById(validId));
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldThrowWhenItemNotFound() {
        // Arrange
        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = new ItemId("ABCDEF1234");

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _service.getDirectSaleItemsByGenreAsc("GEN-12345")
        );
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldThrowWhenEditionNotFound() {
        // Arrange
        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = new ItemId("ABCDEF1234");
        Item item = mock(Item.class);

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(_iEditionRepo.ofIdentity(item.getEditionId())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _service.getDirectSaleItemsByGenreAsc("GEN-12345")
        );
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldThrowWhenPublicationNotFound() {
        // Arrange
        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = new ItemId("ABCDEF1234");
        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(_iEditionRepo.ofIdentity(item.getEditionId())).thenReturn(Optional.of(edition));
        when(_iPublicationRepo.ofIdentity(edition.getPublicationId())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _service.getDirectSaleItemsByGenreAsc("GEN-12345")
        );
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnSortedList() {
        // Arrange
        String genreId = "GEN-12345";
        GenreId gid = new GenreId(genreId);

        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = new ItemId("ABCDEF1234");

        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(_iEditionRepo.ofIdentity(item.getEditionId())).thenReturn(Optional.of(edition));
        when(_iPublicationRepo.ofIdentity(edition.getPublicationId())).thenReturn(Optional.of(publication));

        when(publication.isByGenreId(gid)).thenReturn(true);

        when(_iDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(List.of(itemId)))
                .thenReturn(List.of(itemId));

        // Act
        List<String> result = _service.getDirectSaleItemsByGenreAsc(genreId);

        // Assert
        assertEquals(List.of("ABCDEF1234"), result);
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldThrowWhenNoMatches() {
        when(_iDirectSaleRepo.findAll()).thenReturn(List.of());

        assertThrows(
                IllegalStateException.class,
                () -> _service.getDirectSaleItemsByGenreAsc("GEN-12345")
        );
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldReturnItemsInSortedOrder() {
        String genreId = "GEN-1";

        ItemId id1 = new ItemId("A1B2C3D4E5");
        ItemId id2 = new ItemId("ABCDEF1234");

        DirectSale ds = mock(DirectSale.class);

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(id1, id2));

        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);

        when(_iItemRepo.ofIdentity(any())).thenReturn(Optional.of(item));
        when(_iEditionRepo.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(_iPublicationRepo.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.isByGenreId(any())).thenReturn(true);

        when(_iDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(List.of(id1, id2)))
                .thenReturn(List.of(id2, id1)); // sorted order

        List<String> result = _service.getDirectSaleItemsByGenreAsc(genreId);

        assertEquals(List.of("ABCDEF1234", "A1B2C3D4E5"), result);
    }

    @Test
    void getDirectSaleItemsByGenreAsc_shouldFilterOutNonMatchingGenres() {
        String genreId = "GEN-1";
        GenreId gid = new GenreId(genreId);

        DirectSale ds = mock(DirectSale.class);
        ItemId id1 = new ItemId("A1B2C3D4E5");
        ItemId id2 = new ItemId("ABCDEF1234");

        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication pubMatch = mock(Publication.class);
        Publication pubNoMatch = mock(Publication.class);

        when(_iDirectSaleRepo.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(id1, id2));

        when(_iItemRepo.ofIdentity(any())).thenReturn(Optional.of(item));
        when(_iEditionRepo.ofIdentity(any())).thenReturn(Optional.of(edition));

        when(_iPublicationRepo.ofIdentity(edition.getPublicationId()))
                .thenReturn(Optional.of(pubMatch))
                .thenReturn(Optional.of(pubNoMatch));

        when(pubMatch.isByGenreId(gid)).thenReturn(true);
        when(pubNoMatch.isByGenreId(gid)).thenReturn(false);

        when(_iDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(List.of(id1)))
                .thenReturn(List.of(id1));

        List<String> result = _service.getDirectSaleItemsByGenreAsc(genreId);

        assertEquals(List.of("A1B2C3D4E5"), result);
    }

}