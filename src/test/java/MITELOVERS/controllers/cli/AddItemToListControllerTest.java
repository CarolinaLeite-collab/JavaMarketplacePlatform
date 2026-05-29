
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

//    @Test
//    void findListsByUserIdShouldThrowWhenUserIdIsNull() {
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> _controller.getMyLists(null));
//    }

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
    void addItemToListShouldThrowWhenItemIdIsNull() {
        //arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);
        when(_service.addItemToList("LOI-12345", dto)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _controller.addItemToList("LOI-12345", dto));
    }

    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);

        doThrow(new IllegalStateException("Item already in list"))
                .when(_service).addItemToList("user@cenas.com", dto);

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList("user@cenas.com", dto));
        assertEquals("Item already in list", ex.getMessage());
    }

    @Test
    void addItemToListShouldThrowWhenListDoesNotExist() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);

        when(_service.addItemToList("user@cenas.com", dto))
                .thenThrow(new IllegalStateException("List does not exist"));

        // Act & assert
        assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList("user@cenas.com", dto));
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