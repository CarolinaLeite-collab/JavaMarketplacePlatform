package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        assertNotNull(_controller);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfItems listDouble = mock(ListOfItems.class);
        ListOfItemsRequestDTO dtoDouble = mock(ListOfItemsRequestDTO.class);

        when(dtoDouble.getGenreId()).thenReturn("ficiton");
        when(dtoDouble.getName()).thenReturn("Josefina");

        when(_service.save(any(UserId.class), any(Name.class), any(GenreId.class))).thenReturn(listDouble);

        // Act
        ListOfItems result = _controller.createListOfItems("user@cenas.com", dtoDouble);

        // Assert
        assertNotNull(result);
        assertEquals(listDouble, result);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        ListOfItemsRequestDTO dtoDouble = mock(ListOfItemsRequestDTO.class);

        when(dtoDouble.getName()).thenReturn("Josefina");
        when(dtoDouble.getGenreId()).thenReturn("fiction");

        when(_service.save(any(UserId.class), any(Name.class), any(GenreId.class))).thenReturn(null);

        // Act
        ListOfItems result = _controller.createListOfItems("user@cenas.com", dtoDouble);

        // Assert
        assertNull(result);
    }

}