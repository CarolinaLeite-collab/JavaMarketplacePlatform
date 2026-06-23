package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublicationLinkProvider;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.user.User;
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
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
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
        //Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationResponseDTO dtoDouble = mock(PublicationResponseDTO.class);

        when(dtoDouble.getPublicationId()).thenReturn("publication-id");

        when(_publicationServiceDouble.getPublicationById(any()))
                .thenReturn(publicationDouble);

        when(_mapperDouble.toModel(publicationDouble))
                .thenReturn(dtoDouble);

        //Act
        ResponseEntity<PublicationResponseDTO> response =
                _controller.getPublicationById("publication-id");

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dtoDouble, response.getBody());
    }

    @Test
    void optionsReturnsOkWithLinks() {
        // Arrange
        User userDouble = mock(User.class);
        Link linkDouble = Link.of("/publications").withRel("publications");

        when(_userServiceDouble.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email("pedro@aeiou.com"))))
                .thenReturn(userDouble);

        when(_publicationLinkProviderDouble.getLinks(userDouble))
                .thenReturn(List.of(linkDouble));

        //Act
        ResponseEntity<RepresentationModel<?>> response =
                _controller.options("pedro@aeiou.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getLinks().toList().size());
    }

    @Test
    void optionsReturnsOkWithNoLinks() {
        //Arrange
        User userDouble = mock(User.class);

        when(_userServiceDouble.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email("readonly@aeiou.com"))))
                .thenReturn(userDouble);

        when(_publicationLinkProviderDouble.getLinks(userDouble))
                .thenReturn(List.of());

        //Act
        ResponseEntity<RepresentationModel<?>> response =
                _controller.options("readonly@aeiou.com");

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}