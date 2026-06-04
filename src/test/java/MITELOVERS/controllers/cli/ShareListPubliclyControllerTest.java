package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.SharedDuration;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
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
class ShareListPubliclyControllerTest {

    @Mock
    ListOfItemsService _service;

    @InjectMocks
    ShareListPubliclyController _controller;

    @Test
    void shareListPubliclyShouldCallMakePublicAndSave() {
        // Arrange
        ListOfItems listDouble = mock(ListOfItems.class);
        MakeListPublicRequestDTO dtoDouble = mock(MakeListPublicRequestDTO.class);

        when(dtoDouble.getSharedUntil()).thenReturn(2);
        when(_service.makePublic(any(ListOfItemsId.class), any(SharedDuration.class))).thenReturn(listDouble);

        // Act
        ListOfItems result = _controller.shareListPublicly("LOI-12345", dtoDouble);

        // Assert
        assertNotNull(result);
        assertSame(listDouble, result);
    }

    @Test
    void shareListPubliclyShouldThrowWhenServiceThrows() {
        // Arrange
        MakeListPublicRequestDTO dtoDouble = mock(MakeListPublicRequestDTO.class);
        when(dtoDouble.getSharedUntil()).thenReturn(2);

        when(_service.makePublic(any(ListOfItemsId.class), any(SharedDuration.class)))
                .thenThrow(new IllegalStateException("List not found"));

        // Act
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> _controller.shareListPublicly("LOI-12345", dtoDouble)
        );

        // Assert
        assertEquals("List not found", ex.getMessage());
    }
}