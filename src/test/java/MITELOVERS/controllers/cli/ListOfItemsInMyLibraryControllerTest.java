package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.dto.ItemDetailsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOfItemsInMyLibraryControllerTest {

    @Mock
    private LibraryService _libraryService;

    @InjectMocks
    private ListOfItemsInMyLibraryController _controller;

    @Test
    void shouldReturnItemListFromService() {
        // Arrange
        String userId = "pedro@aeiou.com";
        ItemDetailsDTO dto = mock(ItemDetailsDTO.class);
        when(_libraryService.getListOfItemInfoInMyLibraryFull(userId)).thenReturn(List.of(dto));

        // Act
        List<ItemDetailsDTO> result = _controller.getListOfItemInfoInMyLibrary(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenLibraryIsEmpty() {
        // Arrange
        String userId = "pedro@aeiou.com";
        when(_libraryService.getListOfItemInfoInMyLibraryFull(userId)).thenReturn(List.of());

        // Act
        List<ItemDetailsDTO> result = _controller.getListOfItemInfoInMyLibrary(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldPropagateExceptionFromService() {
        // Arrange
        String userId = "pedro@aeiou.com";
        when(_libraryService.getListOfItemInfoInMyLibraryFull(userId))
                .thenThrow(new IllegalStateException("Library not found for user!"));

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getListOfItemInfoInMyLibrary(userId));
    }
}