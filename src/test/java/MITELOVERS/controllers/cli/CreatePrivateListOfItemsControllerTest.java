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

    @Mock
    ListOfItemsResponseDTOMapper _mapper;

    @InjectMocks
    CreatePrivateListOfItemsController _controller;

    @Test
    void testCreatePrivateListOfItemsController() {
        // SUT
        _controller = new CreatePrivateListOfItemsController(_service, _mapper);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfItems listDouble = mock(ListOfItems.class);
        ListOfItemsRequestDTO dtoDouble = mock(ListOfItemsRequestDTO.class);
        ListOfItemsResponseDTO responseDouble = mock(ListOfItemsResponseDTO.class);

        when(_service.save(any(UserId.class), any(Name.class), any(GenreId.class))).thenReturn(listDouble);
        when(dtoDouble.getGenreId()).thenReturn("ficiton");
        when(dtoDouble.getName()).thenReturn("Josefina");
        when(_mapper.toModel(any(ListOfItems.class))).thenReturn(responseDouble);

        // Act
        ListOfItemsResponseDTO result = _controller.createListOfItems("user@cenas.com", dtoDouble);

        // Assert
        assertEquals(responseDouble, result);
        assertNotNull(result);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        ListOfItemsRequestDTO dtoDouble = mock(ListOfItemsRequestDTO.class);
        when(_service.save(any(UserId.class), any(Name.class), any(GenreId.class))).thenReturn(null);
        when(dtoDouble.getName()).thenReturn("Josefina");
        when(dtoDouble.getGenreId()).thenReturn("fiction");

        // Act
        ListOfItemsResponseDTO result = _controller.createListOfItems("user@cenas.com", dtoDouble);

        // Assert
        assertNull(result);
    }


}