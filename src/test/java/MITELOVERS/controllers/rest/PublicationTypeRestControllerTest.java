package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicationTypeRestControllerTest {

    @InjectMocks
    PublicationTypeRestController _controller;

    @Mock
    PublicationTypeService _publicationTypeServiceDouble;

    @Test
    void getAllPublicationTypesReturnsOkResponse() {
        //Arrange
        PublicationTypeResponseDTO publicationType1 =
                mock(PublicationTypeResponseDTO.class);
        PublicationTypeResponseDTO publicationType2 =
                mock(PublicationTypeResponseDTO.class);

        when(publicationType1.getPublicationTypeId()).thenReturn("BOOK");
        when(publicationType2.getPublicationTypeId()).thenReturn("MAGAZINE");

        List<PublicationTypeResponseDTO> publicationTypes =
                List.of(publicationType1, publicationType2);

        when(_publicationTypeServiceDouble.getAllPublicationTypes())
                .thenReturn(publicationTypes);

        //Act
        ResponseEntity<List<PublicationTypeResponseDTO>> response =
                _controller.getAllPublicationTypes();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(publicationTypes, response.getBody());
    }

    @Test
    void getAllPublicationTypesReturnsNoContentWhenListIsEmpty() {
        //Arrange
        when(_publicationTypeServiceDouble.getAllPublicationTypes())
                .thenReturn(List.of());

        //Act
        ResponseEntity<List<PublicationTypeResponseDTO>> response =
                _controller.getAllPublicationTypes();

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getPublicationTypeByIdReturnsOkResponse() {
        //Arrange
        PublicationTypeResponseDTO responseDTODouble =
                mock(PublicationTypeResponseDTO.class);

        when(_publicationTypeServiceDouble.getPublicationTypeById("BOOK"))
                .thenReturn(responseDTODouble);

        //Act
        ResponseEntity<PublicationTypeResponseDTO> response =
                _controller.getPublicationTypeById("BOOK");

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(responseDTODouble, response.getBody());
    }
}