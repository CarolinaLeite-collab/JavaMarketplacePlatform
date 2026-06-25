package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.EditionLinkProvider;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.mapper.EditionRequestDTOMapper;
import MITELOVERS.mapper.EditionResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EditionRestController.class)
class EditionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EditionService _editionService;

    @MockitoBean
    private UserService _userService;

    @MockitoBean
    private EditionResponseDTOMapper _editionResponseDTOMapper;

    @MockitoBean
    private EditionRequestDTOMapper _editionRequestDTOMapper;

    @MockitoBean
    private EditionLinkProvider _editionLinkProvider;

    // --- OPTIONS /editions ---

    @Test
    void optionsReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("pedro@aeiou.com")))).thenReturn(userDouble);
        when(_editionLinkProvider.getAllowedMethods(userDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/editions")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("POST")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/editions")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));
    }

    // --- GET /editions ---

    @Test
    void getAllEditionsReturnsOkWhenEditionsExist() throws Exception {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO dtoDouble = mock(EditionResponseDTO.class);

        when(_editionService.getAllEditions()).thenReturn(List.of(editionDouble));
        when(_editionResponseDTOMapper.toModel(editionDouble)).thenReturn(dtoDouble);

        // Act + Assert
        mockMvc.perform(get("/editions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEditionsReturnsNoContentWhenEmpty() throws Exception {
        // Arrange
        when(_editionService.getAllEditions()).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/editions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // --- POST /editions ---

    @Test
    void registerEditionReturnsCreatedWhenSuccessful() throws Exception {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        EditionResponseDTO dtoDouble = mock(EditionResponseDTO.class);

        when(_editionRequestDTOMapper.toIdentifier(any())).thenReturn(mock(Identifier.class));
        when(_editionRequestDTOMapper.toDimension(any())).thenReturn(null);
        when(_editionRequestDTOMapper.toWeight(any())).thenReturn(null);
        when(_editionRequestDTOMapper.toNumberOfPages(any())).thenReturn(null);
        when(_editionRequestDTOMapper.toEditionNumber(any())).thenReturn(null);
        when(_editionRequestDTOMapper.toBinding(any())).thenReturn(null);

        when(_editionService.registerEdition(
                any(PublicationTypeId.class), any(Identifier.class), any(PublicationId.class),
                any(PublishingCompanyId.class), any(), any(Language.class),
                any(), any(), any(), any(), any()))
                .thenReturn(editionDouble);

        when(editionDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_editionResponseDTOMapper.toModel(editionDouble)).thenReturn(dtoDouble);

        String requestBody = """
                {
                    "publicationTypeId": "BOOK",
                    "identifier": "9780471989677",
                    "identifierType": "ISBN",
                    "publishingCompanyId": "GG",
                    "publishingYear": 2005,
                    "language": "ENGLISH"
                }
                """;

        // Act + Assert
        mockMvc.perform(post("/editions")
                        .param("pubId", "PUB-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    // --- OPTIONS /editions/{editionId} ---

    @Test
    void optionsForEditionIdReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        Edition editionDouble = mock(Edition.class);

        when(_userService.getUserByEmail(new UserId(new Email("pedro@aeiou.com")))).thenReturn(userDouble);
        when(_editionService.getEditionById(any(EditionId.class))).thenReturn(editionDouble);
        when(_editionLinkProvider.getAllowedMethodsForEditionId(userDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/editions/ED-001")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForEditionIdReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/editions/ED-001")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));
    }

    // --- GET /editions/{editionId} ---

    @Test
    void getEditionByIdReturnsOkWhenFound() throws Exception {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        EditionResponseDTO dtoDouble = mock(EditionResponseDTO.class);

        when(_editionService.getEditionById(any(EditionId.class))).thenReturn(editionDouble);
        when(_editionResponseDTOMapper.toModel(editionDouble)).thenReturn(dtoDouble);

        // Act + Assert
        mockMvc.perform(get("/editions/ED-001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // --- OPTIONS /editions/by-publication ---

    @Test
    void optionsForPublicationReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail(new UserId(new Email("pedro@aeiou.com")))).thenReturn(userDouble);
        when(_editionLinkProvider.getAllowedMethodsForPublication(userDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/editions/by-publication")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .param("publicationId", "PUB-001"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForPublicationReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/editions/by-publication")
                        .header("X-User-Id", "")
                        .param("publicationId", "PUB-001"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));
    }

    // --- GET /editions/by-publication ---

    @Test
    void getAllEditionsByPublicationReturnsOkWhenEditionsExist() throws Exception {
        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionResponseDTO dtoDouble = mock(EditionResponseDTO.class);

        when(_editionService.getAllEditionsByPublication(any(PublicationId.class)))
                .thenReturn(List.of(editionDouble));
        when(_editionResponseDTOMapper.toModel(editionDouble)).thenReturn(dtoDouble);

        // Act + Assert
        mockMvc.perform(get("/editions/by-publication")
                        .param("publicationId", "PUB-001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEditionsByPublicationReturnsNoContentWhenEmpty() throws Exception {
        // Arrange
        when(_editionService.getAllEditionsByPublication(any(PublicationId.class)))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/editions/by-publication")
                        .param("publicationId", "PUB-001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}