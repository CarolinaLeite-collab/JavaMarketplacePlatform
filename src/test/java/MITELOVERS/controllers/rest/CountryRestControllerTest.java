package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.CountryService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.CountryLinkProvider;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.request.CountryRequestDTO;
import MITELOVERS.dto.response.CountryResponseDTO;
import MITELOVERS.mapper.CountryResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryRestControllerTest {

    @Mock
    private CountryService _serviceDouble;

    @Mock
    private CountryResponseDTOMapper _mapperDouble;

    @Mock
    private UserService _userServiceDouble;

    @Mock
    private CountryLinkProvider _countryLinkProviderDouble;

    @InjectMocks
    private CountryRestController _controller;

    @Test
    void options_returnsSelfLinkAndProviderLinks() {
        // Arrange
        String email = "maria@example.com";

        User userDouble = mock(User.class);
        when(_userServiceDouble.getUserByEmail(email))
                .thenReturn(userDouble);

        Link link1 = Link.of("/countries").withRel("countries");
        Link link2 = Link.of("/countries/create").withRel("create-country");

        when(_countryLinkProviderDouble.getLinks(userDouble))
                .thenReturn(List.of(link1, link2));

        // Act
        ResponseEntity<RepresentationModel<?>> response =
                _controller.options(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        RepresentationModel<?> model = response.getBody();
        assertNotNull(model);

        // 1) Self link added by controller
        assertTrue(model.getLinks().hasLink("self"));

        // 2) Provider links
        assertTrue(model.getLinks().hasLink("countries"));
        assertTrue(model.getLinks().hasLink("create-country"));

        // 3) Optional but strong mutation killer: assert order
        List<Link> links = model.getLinks().toList();

        assertEquals("self", links.get(0).getRel().value());
        assertEquals("countries", links.get(1).getRel().value());
        assertEquals("create-country", links.get(2).getRel().value());
    }

    // ───────────────────────────────────────────────────────────────
    // POST /countries
    // ───────────────────────────────────────────────────────────────

    @Test
    void create_returnsCreatedDTOWithLinks() {
        // Arrange
        CountryRequestDTO request = new CountryRequestDTO("Portugal");

        Country countryDouble = mock(Country.class);
        when(_serviceDouble.createCountry("Portugal"))
                .thenReturn(countryDouble);

        CountryResponseDTO dtoDouble = new CountryResponseDTO("123", "Portugal");
        when(_mapperDouble.toModel(countryDouble))
                .thenReturn(dtoDouble);

        // Act
        ResponseEntity<CountryResponseDTO> response = _controller.create(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CountryResponseDTO result = response.getBody();
        assertNotNull(result);

        // DTO fields
        assertEquals("123", result.getCountryId());
        assertEquals("Portugal", result.getName());

        // Links added by controller
        assertTrue(result.getLinks().hasSize(2));
        assertNotNull(result.getLink("self"));
        assertNotNull(result.getLink("list"));
    }

    @Test
    void listAll_returnsCollectionModelWithLinks() {
        // Arrange
        Country c1 = mock(Country.class);
        Country c2 = mock(Country.class);

        when(_serviceDouble.listAllCountries())
                .thenReturn(List.of(c1, c2));

        CountryResponseDTO dto1 = new CountryResponseDTO("1", "Portugal");
        CountryResponseDTO dto2 = new CountryResponseDTO("2", "Spain");

        when(_mapperDouble.toModel(c1)).thenReturn(dto1);
        when(_mapperDouble.toModel(c2)).thenReturn(dto2);

        // Act
        ResponseEntity<CollectionModel<CountryResponseDTO>> response =
                _controller.listAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CollectionModel<CountryResponseDTO> model = response.getBody();
        assertNotNull(model);

        List<CountryResponseDTO> items = model.getContent().stream().toList();
        assertEquals(2, items.size());
        assertTrue(items.contains(dto1));
        assertTrue(items.contains(dto2));

        // Links added by controller
        assertNotNull(model.getLink("self"));
        assertNotNull(model.getLink("create"));
    }

    @Test
    void findById_returnsDTOWithLinks() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_serviceDouble.findById("123"))
                .thenReturn(countryDouble);

        CountryResponseDTO dtoDouble = new CountryResponseDTO("123", "Portugal");
        when(_mapperDouble.toModel(countryDouble))
                .thenReturn(dtoDouble);

        // Act
        ResponseEntity<CountryResponseDTO> response =
                _controller.findById("123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CountryResponseDTO result = response.getBody();
        assertNotNull(result);

        assertEquals("123", result.getCountryId());
        assertEquals("Portugal", result.getName());

        // Links added by controller
        assertNotNull(result.getLink("self"));
        assertNotNull(result.getLink("list"));
    }

    @Test
    void options_whenNoLinks_returnsOnlySelfLink() {
        // Arrange
        User userDouble = mock(User.class);
        when(_userServiceDouble.getUserByEmail("maria@example.com"))
                .thenReturn(userDouble);

        when(_countryLinkProviderDouble.getLinks(userDouble))
                .thenReturn(List.of());

        // Act
        ResponseEntity<RepresentationModel<?>> response =
                _controller.options("maria@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        RepresentationModel<?> model = response.getBody();
        assertNotNull(model);

        assertEquals(1, model.getLinks().toList().size());
        assertTrue(model.getLinks().hasLink("self"));
    }

    @Test
    void create_returnsMapperOutputUnchanged() {
        // Arrange
        CountryRequestDTO request = new CountryRequestDTO("Portugal");

        Country countryDouble = mock(Country.class);
        when(_serviceDouble.createCountry("Portugal"))
                .thenReturn(countryDouble);

        CountryResponseDTO dtoDouble = new CountryResponseDTO("999", "X");
        when(_mapperDouble.toModel(countryDouble))
                .thenReturn(dtoDouble);

        // Act
        ResponseEntity<CountryResponseDTO> response = _controller.create(request);

        // Assert
        assertSame(dtoDouble, response.getBody());
    }

    @Test
    void listAll_whenNoCountries_returnsEmptyCollectionModel() {
        // Arrange
        when(_serviceDouble.listAllCountries())
                .thenReturn(List.of());

        // Act
        ResponseEntity<CollectionModel<CountryResponseDTO>> response =
                _controller.listAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getContent().isEmpty());

        // Links still must exist
        assertNotNull(response.getBody().getLink("self"));
        assertNotNull(response.getBody().getLink("create"));
    }

    @Test
    void findById_returnsMapperOutputUnchanged() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_serviceDouble.findById("123"))
                .thenReturn(countryDouble);

        CountryResponseDTO dtoDouble = new CountryResponseDTO("123", "Portugal");
        when(_mapperDouble.toModel(countryDouble))
                .thenReturn(dtoDouble);

        // Act
        ResponseEntity<CountryResponseDTO> response =
                _controller.findById("123");

        // Assert
        assertSame(dtoDouble, response.getBody());
    }

    @Test
    void findById_whenDtoAlreadyHasLinks_preservesExistingLinks() {
        // Arrange
        Country countryDouble = mock(Country.class);
        when(_serviceDouble.findById("123"))
                .thenReturn(countryDouble);

        CountryResponseDTO dtoDouble = new CountryResponseDTO("123", "Portugal");
        dtoDouble.add(Link.of("/existing").withRel("existing"));

        when(_mapperDouble.toModel(countryDouble))
                .thenReturn(dtoDouble);

        // Act
        ResponseEntity<CountryResponseDTO> response =
                _controller.findById("123");

        // Assert
        CountryResponseDTO result = response.getBody();
        assertNotNull(result);

        assertNotNull(result.getLink("existing"));
        assertNotNull(result.getLink("self"));
        assertNotNull(result.getLink("list"));
    }

}