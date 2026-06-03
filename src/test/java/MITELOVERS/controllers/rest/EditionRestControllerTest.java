package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.EditionLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.dto.request.EditionRequestDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;

@Import({JacksonAutoConfiguration.class, CustomRestExceptionHandler.class})
@WebMvcTest(EditionRestController.class)
class EditionRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @MockitoBean
    private EditionService _editionServiceDouble;

    @Autowired
    private ObjectMapper _objectMapper;

    @MockitoBean
    private EditionLinkProvider _editionLinkProviderDouble;

    @MockitoBean
    private UserService _userServiceDouble;

    @Test
    void registerEditionReturnsCreated() throws Exception {
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .identifier("9780747532743")
                .build();

        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);
        when(responseDouble.getEditionId()).thenReturn("E-ABC12345");
        when(_editionServiceDouble.registerEdition(any(), any())).thenReturn(responseDouble);

        _mockMvc.perform(post("/editions")
                        .param("pubId", "1984-Orwell-G--F43DD6(1949)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void registerEditionServiceThrowsReturnsInternalServerError() throws Exception {
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .build();

        when(_editionServiceDouble.registerEdition(any(), any()))
                .thenThrow(new IllegalStateException("Edition already exists"));

        _mockMvc.perform(post("/editions")
                        .param("pubId", "1984-Orwell-G--F43DD6(1949)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllEditionsReturnsOk() throws Exception {
        EditionResponseDTO dtoDouble = mock(EditionResponseDTO.class);
        when(_editionServiceDouble.getAllEditions()).thenReturn(List.of(dtoDouble));

        _mockMvc.perform(get("/editions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.editionResponseDTOList", hasSize(1)));

        verify(dtoDouble, atLeastOnce()).add((Link) any());
    }

    @Test
    void getAllEditionsEmptyReturnsNoContent() throws Exception {
        when(_editionServiceDouble.getAllEditions()).thenReturn(List.of());

        _mockMvc.perform(get("/editions"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllEditionsByPublicationReturnsOk() throws Exception {
        EditionResponseDTO dtoDouble = mock(EditionResponseDTO.class);
        when(_editionServiceDouble.getAllEditionsByPublication(any()))
                .thenReturn(List.of(dtoDouble));

        _mockMvc.perform(get("/editions/by-publication")
                        .param("publicationId", "1984-Orwell-G--F43DD6(1949)"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(dtoDouble, atLeastOnce()).add((Link) any());
    }

    @Test
    void getAllEditionsByPublicationEmptyReturnsNoContent() throws Exception {
        when(_editionServiceDouble.getAllEditionsByPublication(any()))
                .thenReturn(List.of());

        _mockMvc.perform(get("/editions/by-publication")
                        .param("publicationId", "1984-Orwell-G--F43DD6(1949)"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllEditionsByPublicationServiceThrowsReturnsInternalServerError() throws Exception {
        when(_editionServiceDouble.getAllEditionsByPublication(any()))
                .thenThrow(new NoSuchElementException("Publication not found"));

        _mockMvc.perform(get("/editions/by-publication")
                        .param("publicationId", "1984-Orwell-G--F43DD6(1949)"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getEditionByIdReturnsOk() throws Exception {
        when(_editionServiceDouble.getEditionById(any()))
                .thenReturn(mock(EditionResponseDTO.class));

        _mockMvc.perform(get("/editions/E-ABC12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void optionsShouldReturn200WithLinksForAuthorizedUser() throws Exception {
        User _userDouble = mock(User.class);
        when(_userServiceDouble.getUserByEmail("pedro@aeiou.com")).thenReturn(_userDouble);
        when(_editionLinkProviderDouble.getLinks(_userDouble)).thenReturn(List.of(
                Link.of("/editions").withRel("editions"),
                Link.of("/editions").withRel("edition-create")
        ));

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
        User _userDouble = mock(User.class);
        when(_userServiceDouble.getUserByEmail("readonly@aeiou.com")).thenReturn(_userDouble);
        when(_editionLinkProviderDouble.getLinks(_userDouble)).thenReturn(List.of());

        _mockMvc.perform(options("/editions")
                        .param("email", "readonly@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void optionsShouldReturn404WhenUserNotFound() throws Exception {
        when(_userServiceDouble.getUserByEmail("naoexiste@aeiou.com"))
                .thenThrow(new NoSuchElementException("User not found"));

        _mockMvc.perform(options("/editions")
                        .param("email", "naoexiste@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());
    }
}