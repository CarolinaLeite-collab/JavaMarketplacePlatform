package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
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

    // ------------------------------------------------------------
    // POST /direct-sales
    // ------------------------------------------------------------

    @Test
    void createDirectSale_shouldReturnCreated() {

        // Arrange
        DirectSaleRequestDTO request = mock(DirectSaleRequestDTO.class);

        DirectSaleResponseDTO response =
                new DirectSaleResponseDTO(
                        "DS-A1B2C3D4",
                        List.of("ITEM-1"),
                        10.0,
                        "EUR",
                        3600L,
                        Instant.now()
                );

        when(_service.createDirectSale(request)).thenReturn(response);

        // Act (SUT)
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.createDirectSale(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(response, result.getBody());
        assertTrue(result.getBody().getLinks().hasLink("self"));
    }

    // ------------------------------------------------------------
    // GET /direct-sales
    // ------------------------------------------------------------

    @Test
    void getAllDirectSales_shouldReturnOk() {

        // Arrange
        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
                        "DS-A1B2C3D4",
                        List.of("ITEM-1"),
                        10.0,
                        "EUR",
                        3600L,
                        Instant.now()
                );

        when(_service.getAllDirectSales()).thenReturn(List.of(dto));

        // Act
        ResponseEntity<List<DirectSaleResponseDTO>> result =
                _controller.getAllDirectSales();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertTrue(result.getBody().get(0).getLinks().hasLink("self"));
    }

    @Test
    void getAllDirectSales_shouldReturnNoContentWhenEmpty() {

        // Arrange
        when(_service.getAllDirectSales()).thenReturn(List.of());

        // Act
        ResponseEntity<List<DirectSaleResponseDTO>> result =
                _controller.getAllDirectSales();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    // ------------------------------------------------------------
    // GET /direct-sales/{id}
    // ------------------------------------------------------------

    @Test
    void getDirectSaleById_shouldReturnOk() {

        // Arrange
        DirectSaleResponseDTO dto =
                new DirectSaleResponseDTO(
                        "DS-A1B2C3D4",
                        List.of("ITEM-1"),
                        10.0,
                        "EUR",
                        3600L,
                        Instant.now()
                );

        when(_service.getDirectSaleById("DS-A1B2C3D4")).thenReturn(dto);

        // Act
        ResponseEntity<DirectSaleResponseDTO> result =
                _controller.getDirectSaleById("DS-A1B2C3D4");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(dto, result.getBody());
        assertTrue(result.getBody().getLinks().hasLink("self"));
    }

    // ------------------------------------------------------------
    // GET /direct-sales/genre/{genreId}
    // ------------------------------------------------------------

    @Test
    void getDirectSaleItemsByGenre_shouldReturnOk() {

        // Arrange
        String genreId = "FICTION";

        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(
                        List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4"),
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-1234ABCD")
                ));

        when(_service.getDirectSaleItemsByGenreAsc(genreId)).thenReturn(dto);

        // Act
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        DSFilteredItemsResponseDTO body = result.getBody();
        assertNotNull(body);

        assertEquals(2, body.getDirectSales().size());
        assertEquals("DS-A1B2C3D4", body.getDirectSales().get(0).getDirectSaleId());
        assertEquals("DS-1234ABCD", body.getDirectSales().get(1).getDirectSaleId());

        // Each entry has self link
        assertTrue(body.getDirectSales().get(0).getLinks().hasLink("self"));
        assertTrue(body.getDirectSales().get(1).getLinks().hasLink("self"));

        // Collection has self link
        assertTrue(body.getLinks().hasLink("self"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldThrowWhenServiceThrows() {

        // Arrange
        String genreId = "ROMANCE";
        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenThrow(new IllegalStateException("No matches"));

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenre(genreId)
        );
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToEachEntry() {

        // Arrange
        String genreId = "FICTION";

        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4")
                ));

        when(_service.getDirectSaleItemsByGenreAsc(genreId)).thenReturn(dto);

        // Act
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        var entry = result.getBody().getDirectSales().get(0);
        assertTrue(entry.getLinks().hasLink("self"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToCollection() {

        // Arrange
        String genreId = "FICTION";

        DSFilteredItemsResponseDTO dto =
                new DSFilteredItemsResponseDTO(
                        List.of(
                        new DSFilteredItemsResponseDTO.DirectSaleEntry("DS-A1B2C3D4")
                ));

        when(_service.getDirectSaleItemsByGenreAsc(genreId)).thenReturn(dto);

        // Act
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        assertTrue(result.getBody().getLinks().hasLink("self"));
    }

}