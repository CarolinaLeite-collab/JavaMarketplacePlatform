package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.EditionLinkProvider;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.request.EditionRequestDTO;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.mapper.EditionRequestDTOMapper;
import MITELOVERS.mapper.EditionResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EditionRestController.class)
class EditionRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @Autowired
    private ObjectMapper _objectMapper;

    @MockitoBean
    private EditionService _editionServiceDouble;

    @MockitoBean
    private EditionLinkProvider _editionLinkProviderDouble;

    @MockitoBean
    private UserService _userServiceDouble;

    @MockitoBean
    private EditionRequestDTOMapper _editionRequestDTOMapperDouble;

    @MockitoBean
    private EditionResponseDTOMapper _editionResponseDTOMapperDouble;


    @Test
    void registerEditionReturnsCreated() throws Exception {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .identifier("9780747532743")
                .build();

        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);
        when(responseDouble.getEditionId()).thenReturn("E-ABC12345");

        when(_editionServiceDouble.registerEdition(any(), any())).thenReturn(editionDouble);
        when(_editionResponseDTOMapperDouble.toModel(editionDouble)).thenReturn(responseDouble);

        // Act & Assert
        _mockMvc.perform(post("/editions")
                        .param("pubId", "1984-Orwell-G--F43DD6(1949)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllEditionsReturnsOk() throws Exception {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);
        when(responseDouble.getEditionId()).thenReturn("E-ABC12345");

        when(_editionServiceDouble.getAllEditions()).thenReturn(List.of(editionDouble));
        when(_editionResponseDTOMapperDouble.toModel(editionDouble)).thenReturn(responseDouble);

        // Act & Assert
        _mockMvc.perform(get("/editions"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEditionsEmptyReturnsNoContent() throws Exception {
        // Arrange
        when(_editionServiceDouble.getAllEditions()).thenReturn(List.of());

        // Act & Assert
        _mockMvc.perform(get("/editions"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllEditionsServiceThrowsReturnsInternalServerError() throws Exception {
        // Arrange
        when(_editionServiceDouble.getAllEditions())
                .thenThrow(new RuntimeException("Error"));

        // Act & Assert
        _mockMvc.perform(get("/editions"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllEditionsByPublicationReturnsOk() throws Exception {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);
        when(responseDouble.getEditionId()).thenReturn("E-ABC12345");

        when(_editionServiceDouble.getAllEditionsByPublication(any()))
                .thenReturn(List.of(editionDouble));
        when(_editionResponseDTOMapperDouble.toModel(editionDouble)).thenReturn(responseDouble);

        // Act & Assert
        _mockMvc.perform(get("/editions/by-publication")
                        .param("publicationId", "1984-Orwell-G--F43DD6(1949)"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEditionsByPublicationEmptyReturnsNoContent() throws Exception {
        // Arrange
        when(_editionServiceDouble.getAllEditionsByPublication(any()))
                .thenReturn(List.of());

        // Act & Assert
        _mockMvc.perform(get("/editions/by-publication")
                        .param("publicationId", "1984-Orwell-G--F43DD6(1949)"))
                .andExpect(status().isNoContent());
    }


    @Test
    void getEditionByIdReturnsOk() throws Exception {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);
        when(responseDouble.getEditionId()).thenReturn("E-ABC12345");

        when(_editionServiceDouble.getEditionById(any())).thenReturn(editionDouble);
        when(_editionResponseDTOMapperDouble.toModel(editionDouble)).thenReturn(responseDouble);

        // Act & Assert
        _mockMvc.perform(get("/editions/E-ABC12345"))
                .andExpect(status().isOk());
    }

    @Test
    void getEditionByIdNotFoundReturnsNotFound() throws Exception {
        // Arrange
        when(_editionServiceDouble.getEditionById(any()))
                .thenThrow(new NoSuchElementException("Edition not found"));

        // Act & Assert
        _mockMvc.perform(get("/editions/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void optionsShouldReturn200WithLinksForAuthorizedUser() throws Exception {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userServiceDouble.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email("pedro@aeiou.com")))).thenReturn(_userDouble);
        when(_editionLinkProviderDouble.getLinks(_userDouble)).thenReturn(List.of(
                Link.of("/editions").withRel("editions"),
                Link.of("/editions").withRel("edition-create")
        ));

        // Act & Assert
        _mockMvc.perform(options("/editions")
                        .param("email", "pedro@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.editions").exists())
                .andExpect(jsonPath("$._links.edition-create").exists());
    }

    @Test
    void optionsShouldReturn200WithNoLinksForUnauthorizedUser() throws Exception {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userServiceDouble.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email("readonly@aeiou.com")))).thenReturn(_userDouble);
        when(_editionLinkProviderDouble.getLinks(_userDouble)).thenReturn(List.of());

        // Act & Assert
        _mockMvc.perform(options("/editions")
                        .param("email", "readonly@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void optionsShouldReturn404WhenUserNotFound() throws Exception {
        // Arrange
        when(_userServiceDouble.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email("naoexiste@aeiou.com"))))
                .thenThrow(new NoSuchElementException("User not found"));

        // Act & Assert
        _mockMvc.perform(options("/editions")
                        .param("email", "naoexiste@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());
    }

}