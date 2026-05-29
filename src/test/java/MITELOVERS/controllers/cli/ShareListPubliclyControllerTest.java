package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        ListOfItemsResponseDTO responseDouble = mock(ListOfItemsResponseDTO.class);
        MakeListPublicRequestDTO dtoDouble = mock(MakeListPublicRequestDTO.class);

        when(_service.makePublic("LOI-12345", dtoDouble)).thenReturn(responseDouble);

        // Act
        ListOfItemsResponseDTO result = _controller.shareListPublicly("LOI-12345", dtoDouble);

        // Assert
        assertEquals(responseDouble, result);
    }
}