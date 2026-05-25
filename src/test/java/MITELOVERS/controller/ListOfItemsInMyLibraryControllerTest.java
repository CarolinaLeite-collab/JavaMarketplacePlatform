package MITELOVERS.controller;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.controllers.cli.ListOfItemsInMyLibraryController;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.ItemDetailsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListOfItemsInMyLibraryControllerTest {

    @Mock
    private LibraryService _libraryService;

    @InjectMocks
    private ListOfItemsInMyLibraryController _controller;

    @Test
    void shouldReturnItemListFromService() {
        // Arrange
        UserId userId = mock(UserId.class);
        ItemDetailsDTO dto = mock(ItemDetailsDTO.class);
        when(_libraryService.getListOfItemInfoInMyLibrary(userId)).thenReturn(List.of(dto));

        // Act
        List<ItemDetailsDTO> result = _controller.getListOfItemInfoInMyLibrary(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenLibraryIsEmpty() {
        // Arrange
        UserId userId = mock(UserId.class);
        when(_libraryService.getListOfItemInfoInMyLibrary(userId)).thenReturn(List.of());

        // Act
        List<ItemDetailsDTO> result = _controller.getListOfItemInfoInMyLibrary(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldPropagateExceptionFromService() {
        // Arrange
        UserId userId = mock(UserId.class);
        when(_libraryService.getListOfItemInfoInMyLibrary(userId))
                .thenThrow(new IllegalStateException("Library not found for user!"));

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getListOfItemInfoInMyLibrary(userId));
    }
}