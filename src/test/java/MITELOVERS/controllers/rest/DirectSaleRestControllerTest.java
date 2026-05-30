package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
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

        List<String> itemIds = List.of("ABCDEF1234", "A1B2C3D4E5");

        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenReturn(itemIds);

        // Act
        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        DSFilteredItemsResponseDTO body = result.getBody();
        assertNotNull(body);

        // Items exist
        assertEquals(2, body.getItems().size());

        // Item IDs match
        assertEquals("ABCDEF1234", body.getItems().get(0).getItemId());
        assertEquals("A1B2C3D4E5", body.getItems().get(1).getItemId());

        // Each item has a self link
        assertTrue(body.getItems().get(0).getLinks().hasLink("self"));
        assertTrue(body.getItems().get(1).getLinks().hasLink("self"));

        // The DTO has a self link
        assertTrue(body.getLinks().hasLink("self"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldThrowWhenNoMatches() {
        String genreId = "GEN-12345";

        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenThrow(new IllegalStateException("No matching DirectSales"));

        assertThrows(
                IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenre(genreId)
        );
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToEachItem() {
        String genreId = "GEN-12345";
        List<String> itemIds = List.of("ITEM-1");

        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenReturn(itemIds);

        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        var item = result.getBody().getItems().get(0);

        assertTrue(item.getLinks().hasLink("self"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldAddSelfLinkToCollection() {
        String genreId = "GEN-12345";
        List<String> itemIds = List.of("ITEM-1");

        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenReturn(itemIds);

        ResponseEntity<DSFilteredItemsResponseDTO> result =
                _controller.getDirectSaleItemsByGenre(genreId);

        assertTrue(result.getBody().getLinks().hasLink("self"));
    }

    @Test
    void getDirectSaleItemsByGenre_shouldWrapItemsInItemEntry() {
        String genreId = "GEN-12345";
        List<String> itemIds = List.of("ITEM-1");

        when(_service.getDirectSaleItemsByGenreAsc(genreId))
                .thenReturn(itemIds);

        var result = _controller.getDirectSaleItemsByGenre(genreId);

        assertEquals("ITEM-1", result.getBody().getItems().get(0).getItemId());
    }

}