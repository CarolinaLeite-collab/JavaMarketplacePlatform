package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.SharedDuration;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class ShareListPubliclyControllerTest {

    @Mock
    ListOfItemsService _service;

    @Mock
    ListOfItemsResponseDTOMapper _mapper;

    @InjectMocks
    ShareListPubliclyController _controller;

    @Test
    void shareListPubliclyShouldCallMakePublicAndSave() {
        // Arrange
        ListOfItems listDouble = mock(ListOfItems.class);
        MakeListPublicRequestDTO dtoDouble = mock(MakeListPublicRequestDTO.class);
        ListOfItemsResponseDTO responseDouble = mock(ListOfItemsResponseDTO.class);

        when(_service.makePublic(any(ListOfItemsId.class), any(SharedDuration.class))).thenReturn(listDouble);
        when(dtoDouble.getSharedUntil()).thenReturn(2);
        when(_mapper.toModel(any(ListOfItems.class))).thenReturn(responseDouble);

        // Act
        ListOfItemsResponseDTO result = _controller.shareListPublicly("LOI-12345", dtoDouble);

        // Assert
        assertEquals(responseDouble, result);
    }
}