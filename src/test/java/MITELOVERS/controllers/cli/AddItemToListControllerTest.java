package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class AddItemToListControllerTest {

    static final String ITEM_ALREADY_IN_LIST = "Item already in list";
    static final String LIST_DOES_NOT_EXIST  = "List does not exist";

    @Mock
    ListOfItemsService _service;

    @InjectMocks
    AddItemToListController _controller;

    @Test
    void getMyListsShouldReturnOnlyListsBelongingToUser() {
        // Arrange
        ListOfItemsResponseDTO list1 = mock(ListOfItemsResponseDTO.class);
        ListOfItemsResponseDTO list2 = mock(ListOfItemsResponseDTO.class);
        when(_service.getUserLists("user@cenas.com")).thenReturn(List.of(list1, list2));

        // Act
        List<ListOfItemsResponseDTO> result = _controller.getMyLists("user@cenas.com");

        // Assert
        assertEquals(2, result.size());
        assertSame(list1, result.get(0));
        assertSame(list2, result.get(1));
    }

    @Test
    void getMyListsShouldReturnEmptyListWhenUserHasNoLists() {
        // Arrange
        when(_service.getUserLists("user@cenas.com")).thenReturn(List.of());

        // Act
        List<ListOfItemsResponseDTO> result = _controller.getMyLists("user@cenas.com");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addItemToListShouldSucceedWithValidArguments() {
        // Arrange
        AddItemRequestDTO dto              = mock(AddItemRequestDTO.class);
        ListOfItemsResponseDTO expected    = mock(ListOfItemsResponseDTO.class);
        when(_service.addItemToList("LOI-12345", dto)).thenReturn(expected);

        // Act
        assertDoesNotThrow(() -> _controller.addItemToList("LOI-12345", dto));

        // Assert
        verify(_service).addItemToList("LOI-12345", dto);
    }

    @Test
    void addItemToListShouldThrowWhenItemIdIsNull() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);
        when(_service.addItemToList("LOI-12345", dto)).thenThrow(new IllegalArgumentException());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> _controller.addItemToList("LOI-12345", dto));
    }

    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);
        doThrow(new IllegalStateException(ITEM_ALREADY_IN_LIST))
                .when(_service).addItemToList("user@cenas.com", dto);

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList("user@cenas.com", dto));

        // Assert
        assertEquals(ITEM_ALREADY_IN_LIST, ex.getMessage());
    }

    @Test
    void addItemToListShouldThrowWhenListDoesNotExist() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);
        when(_service.addItemToList("user@cenas.com", dto))
                .thenThrow(new IllegalStateException(LIST_DOES_NOT_EXIST));

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList("user@cenas.com", dto));

        // Assert
        assertEquals(LIST_DOES_NOT_EXIST, ex.getMessage());
    }

    @Test
    void findByGenreShouldReturnListsWhenGenreHasResults() {
        // Arrange
        String genreId                  = "FICTION";
        ListOfItemsResponseDTO list1    = mock(ListOfItemsResponseDTO.class);
        when(_service.findByGenre(genreId)).thenReturn(List.of(list1));

        // Act
        List<ListOfItemsResponseDTO> result = _controller.findByGenre(genreId);

        // Assert
        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
    }

    @Test
    void findByGenreShouldReturnEmptyWhenThereIsNoListByGenreId() {
        // Arrange
        String genreId = "NON-FICTION";
        when(_service.findByGenre(genreId)).thenReturn(List.of());

        // Act
        List<ListOfItemsResponseDTO> result = _controller.findByGenre(genreId);

        // Assert
        assertTrue(result.isEmpty());
    }
}