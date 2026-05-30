package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class CreatePrivateListOfItemsControllerTest {

    @Mock
    ListOfItemsService _service;

    @InjectMocks
    CreatePrivateListOfItemsController _controller;

    @Test
    void testCreatePrivateListOfItemsController() {
        // SUT
        _controller = new CreatePrivateListOfItemsController(_service);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfItemsResponseDTO listDouble = mock(ListOfItemsResponseDTO.class);
        ListOfItemsRequestDTO dtoDouble = mock(ListOfItemsRequestDTO.class);
        when(_service.save("user@cenas.com", dtoDouble)).thenReturn(listDouble);

        // Act
        ListOfItemsResponseDTO result = _controller.createListOfItems("user@cenas.com", dtoDouble);

        // Assert
        assertEquals(listDouble, result);
        assertNotNull(result);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        ListOfItemsRequestDTO dtoDouble = mock(ListOfItemsRequestDTO.class);
        when(_service.save("user@cenas.com", dtoDouble)).thenReturn(null);

        // Act
        ListOfItemsResponseDTO result = _controller.createListOfItems("user@cenas.com", dtoDouble);

        // Assert
        assertNull(result);
    }


}