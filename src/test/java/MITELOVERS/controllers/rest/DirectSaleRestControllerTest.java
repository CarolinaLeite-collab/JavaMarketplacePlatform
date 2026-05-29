package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.dto.DirectSaleRequestDTO;
import MITELOVERS.dto.DirectSaleResponseDTO;
import MITELOVERS.dto.DSFilteredItemsResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectSaleRestControllerTest {

    @Mock
    private DirectSaleService _service;

    @InjectMocks
    private DirectSaleRestController _controller;

    @Test
    void createDirectSale_shouldReturnCreated() {
        // Arrange
        DirectSaleRequestDTO request = mock(DirectSaleRequestDTO.class);
        DirectSaleResponseDTO response = mock(DirectSaleResponseDTO.class);

        when(_service.createDirectSale(request)).thenReturn(response);

        // Act (SUT)
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.createDirectSale(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getAllDirectSales_shouldReturnOk() {
        // Arrange
        List<DirectSaleResponseDTO> list =
                List.of(mock(DirectSaleResponseDTO.class));

        when(_service.getAllDirectSales()).thenReturn(list);

        // Act (SUT)
        ResponseEntity<List<DirectSaleResponseDTO>> result =
                _controller.getAllDirectSales();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(list, result.getBody());
    }

    @Test
    void getDirectSaleById_shouldReturnOk() {
        // Arrange
        DirectSaleResponseDTO dto = mock(DirectSaleResponseDTO.class);

        when(_service.getDirectSaleById("DS1")).thenReturn(dto);

        // Act (SUT)
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("DS1");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void getDirectSaleItemsByGenre_shouldReturnOk() {
        // Arrange
        String genreId = "GEN-12345";

        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(
                        List.of("ABCDEF1234", "A1B2C3D4E5")
                );

        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenReturn(dto);

        // Act (SUT)
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void getDirectSaleItemsByGenre_shouldThrowWhenNoMatches() {
        // Arrange
        String genreId = "GEN-12345";

        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenThrow(new IllegalStateException("No matching DirectSales"));

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenre(genreId)
        );
    }

    @Test
    void getAllDirectSales_shouldReturnNoContentWhenListEmpty() {
        // Arrange
        when(_service.getAllDirectSales()).thenReturn(List.of());

        // Act (SUT)
        ResponseEntity<List<DirectSaleResponseDTO>> result =
                _controller.getAllDirectSales();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

}