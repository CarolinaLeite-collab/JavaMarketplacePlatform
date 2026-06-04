package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublicationLinkProvider;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.request.PublicationRequestDTO;
import MITELOVERS.dto.response.PublicationResponseDTO;
import MITELOVERS.mapper.PublicationResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicationRestControllerTest {

    // SUT
    @InjectMocks
    PublicationRestController _controller;

    @Mock
    PublicationService _publicationServiceDouble;

    @Mock
    PublicationResponseDTOMapper _mapperDouble;

    @Mock
    PublicationLinkProvider _publicationLinkProviderDouble;

    @Mock
    UserService _userServiceDouble;

    // --- registerPublication tests ---

    @Test
    void registerPublicationReturnsCreatedWithDTO() {
        // Arrange
        PublicationRequestDTO requestDTO = new PublicationRequestDTO(
                "Photomaton & Vox",
                "HERBERTO_HELDER",
                1979,
                "PROSE"
        );

        Publication publicationDouble = mock(Publication.class);
        PublicationResponseDTO responseDTODouble = mock(PublicationResponseDTO.class);

        when(_publicationServiceDouble.registerPublication(
                any(Title.class),
                any(AuthorId.class),
                any(Year.class),
                any(GenreId.class)
        )).thenReturn(publicationDouble);

        when(_mapperDouble.toModel(publicationDouble)).thenReturn(responseDTODouble);
        when(responseDTODouble.getPublicationId()).thenReturn("PUB-001");

        // Act
        ResponseEntity<PublicationResponseDTO> response =
                _controller.registerPublicationAndReturnDTO(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(responseDTODouble, response.getBody());
    }

    // --- getAllPublications tests ---

    @Test
    void getAllPublicationsReturnsOkWithDTOList() {
        // Arrange
        Publication publication1 = mock(Publication.class);
        Publication publication2 = mock(Publication.class);

        PublicationResponseDTO dto1 = mock(PublicationResponseDTO.class);
        PublicationResponseDTO dto2 = mock(PublicationResponseDTO.class);

        when(_publicationServiceDouble.getAllPublications())
                .thenReturn(List.of(publication1, publication2));

        when(_mapperDouble.toModel(publication1)).thenReturn(dto1);
        when(_mapperDouble.toModel(publication2)).thenReturn(dto2);

        when(dto1.getPublicationId()).thenReturn("PUB-001");
        when(dto2.getPublicationId()).thenReturn("PUB-002");

        // Act
        ResponseEntity<List<PublicationResponseDTO>> response =
                _controller.getAllPublications();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertSame(dto1, response.getBody().get(0));
        assertSame(dto2, response.getBody().get(1));
    }

    @Test
    void getAllPublicationsReturnsNoContentWhenListIsEmpty() {
        // Arrange
        when(_publicationServiceDouble.getAllPublications()).thenReturn(List.of());

        // Act
        ResponseEntity<List<PublicationResponseDTO>> response =
                _controller.getAllPublications();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getPublicationByIdReturnsOkWithDTO() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationResponseDTO responseDTODouble = mock(PublicationResponseDTO.class);

        when(_publicationServiceDouble.getPublicationById("PUB-001"))
                .thenReturn(publicationDouble);

        when(_mapperDouble.toModel(publicationDouble)).thenReturn(responseDTODouble);
        when(responseDTODouble.getPublicationId()).thenReturn("PUB-001");

        // Act
        ResponseEntity<PublicationResponseDTO> response =
                _controller.getPublicationById("PUB-001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(responseDTODouble, response.getBody());
    }

}