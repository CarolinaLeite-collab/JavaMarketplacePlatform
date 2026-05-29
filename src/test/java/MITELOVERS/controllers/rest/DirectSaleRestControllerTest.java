package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.dto.DirectSaleRequestDTO;
import MITELOVERS.dto.DirectSaleResponseDTO;
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
    private DirectSaleService service;

    @InjectMocks
    private DirectSaleRestController controller;

    @Test
    void createDirectSale_shouldReturnCreated() {
        // Arrange
        DirectSaleRequestDTO request = mock(DirectSaleRequestDTO.class);
        DirectSaleResponseDTO response = mock(DirectSaleResponseDTO.class);

        when(service.createDirectSale(request)).thenReturn(response);

        // Act (SUT)
        ResponseEntity<DirectSaleResponseDTO> result =
                controller.createDirectSale(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getAllDirectSales_shouldReturnOk() {
        // Arrange
        List<DirectSaleResponseDTO> list =
                List.of(mock(DirectSaleResponseDTO.class));

        when(service.getAllDirectSales()).thenReturn(list);

        // Act (SUT)
        ResponseEntity<List<DirectSaleResponseDTO>> result =
                controller.getAllDirectSales();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(list, result.getBody());
    }

    @Test
    void getDirectSaleById_shouldReturnOk() {
        // Arrange
        DirectSaleResponseDTO dto = mock(DirectSaleResponseDTO.class);

        when(service.getDirectSaleById("DS1")).thenReturn(dto);

        // Act (SUT)
        ResponseEntity<DirectSaleResponseDTO> result =
                controller.getDirectSaleById("DS1");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

}