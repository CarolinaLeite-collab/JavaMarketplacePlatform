package MITELOVERS.controllers.rest;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.PublicationRequestDTO;
import MITELOVERS.dto.PublicationResponseDTO;
import MITELOVERS.applicationservices.PublicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicationRestControllerTest {

    @InjectMocks
    PublicationRestController _controller;

    @Mock
    PublicationService _publicationServiceDouble;

    @Test
    void registerPublicationAndReturnDTOReturnsCreatedResponse() {
        // Arrange
        PublicationRequestDTO requestDTO = new PublicationRequestDTO(
                "Photomaton & Vox",
                "HERBERTO_HELDER",
                1979,
                "p4"
        );

        PublicationResponseDTO responseDTODouble =
                mock(PublicationResponseDTO.class);

        when(_publicationServiceDouble.registerPublication(
                any(Title.class),
                any(AuthorId.class),
                any(Year.class),
                any(GenreId.class)
        )).thenReturn(responseDTODouble);

        // Act
        ResponseEntity<PublicationResponseDTO> response =
                _controller.registerPublicationAndReturnDTO(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(responseDTODouble, response.getBody());
    }

    @Test
    void getAllPublicationsReturnsOkResponse() {
        // Arrange
        PublicationResponseDTO publication1 = mock(PublicationResponseDTO.class);
        PublicationResponseDTO publication2 = mock(PublicationResponseDTO.class);

        List<PublicationResponseDTO> publications =
                List.of(publication1, publication2);

        when(_publicationServiceDouble.getAllPublications())
                .thenReturn(publications);

        // Act
        ResponseEntity<List<PublicationResponseDTO>> response =
                _controller.getAllPublications();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(publications, response.getBody());
    }

    @Test
    void getPublicationByIdReturnsOkResponse() {
        //Act
        ResponseEntity<PublicationResponseDTO> response =
                _controller.getPublicationById("PUB-001");

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllPublicationsReturnsNoContentWhenListIsEmpty() {
        //Arrange
        when(_publicationServiceDouble.getAllPublications())
                .thenReturn(List.of());

        //Act
        ResponseEntity<List<PublicationResponseDTO>> response =
                _controller.getAllPublications();

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}