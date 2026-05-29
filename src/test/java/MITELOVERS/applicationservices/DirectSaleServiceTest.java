package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.dto.DirectSaleRequestDTO;
import MITELOVERS.dto.DirectSaleResponseDTO;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectSaleServiceTest {

    @Mock
    private IDirectSaleRepo repo;

    @Mock
    private DirectSaleFactory factory;

    @Mock
    private DirectSaleResponseDTOMapper mapper;

    @InjectMocks
    private DirectSaleService service;

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

        when(factory.createDirectSale(
                anyList(),
                any(Price.class),
                any(Duration.class)
        )).thenReturn(newSale);

        when(repo.containsOfIdentity(any())).thenReturn(false);
        when(repo.save(newSale)).thenReturn(savedSale);
        when(mapper.toResponseDTO(savedSale)).thenReturn(expectedDTO);

        // Act
        DirectSaleResponseDTO result = service.createDirectSale(request);

        // Assert
        assertEquals(expectedDTO, result);
    }

    @Test
    void getAllDirectSales_shouldReturnMappedList() {
        DirectSale ds = mock(DirectSale.class);
        DirectSaleResponseDTO dto = mock(DirectSaleResponseDTO.class);

        when(repo.findAll()).thenReturn(List.of(ds));
        when(mapper.toResponseDTO(ds)).thenReturn(dto);

        List<DirectSaleResponseDTO> result = service.getAllDirectSales();

        assertEquals(List.of(dto), result);
    }

    @Test
    void getDirectSaleById_shouldReturnDTO() {
        // Arrange
        DirectSale ds = mock(DirectSale.class);
        DirectSaleResponseDTO dto = mock(DirectSaleResponseDTO.class);

        // Use a valid DirectSaleId format
        String validId = "DS-A1B2C3D4";

        when(repo.ofIdentity(any())).thenReturn(Optional.of(ds));
        when(mapper.toResponseDTO(ds)).thenReturn(dto);

        // Act
        DirectSaleResponseDTO result = service.getDirectSaleById(validId);

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void getDirectSaleById_shouldThrowIfNotFound() {
        // Arrange
        when(repo.ofIdentity(any())).thenReturn(Optional.empty());

        // Use a valid DirectSaleId format
        String validId = "DS-A1B2C3D4";

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> service.getDirectSaleById(validId));
    }
}