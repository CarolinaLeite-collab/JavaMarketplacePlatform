package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.PublicationTypeLinkProvider;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import MITELOVERS.mapper.PublicationTypeResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicationTypeRestController.class)
@Import(CustomRestExceptionHandler.class)
class PublicationTypeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PublicationTypeService publicationTypeService;

    @MockitoBean
    private PublicationTypeResponseDTOMapper publicationTypeResponseDTOMapper;

    @MockitoBean
    private PublicationTypeLinkProvider publicationTypeLinkProvider;

    @MockitoBean
    private UserService userService;

    // --- getAllPublicationTypes ---

    @Test
    void getAllPublicationTypesReturnsOkResponse() throws Exception {
        // Arrange
        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeResponseDTO dto = new PublicationTypeResponseDTO("BOOK");

        when(publicationTypeService.getAllPublicationTypes()).thenReturn(List.of(publicationType));
        when(publicationTypeResponseDTOMapper.toModel(publicationType)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/publicationTypes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAllPublicationTypesReturnsNoContentWhenEmpty() throws Exception {
        // Arrange
        when(publicationTypeService.getAllPublicationTypes()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/publicationTypes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // --- getPublicationTypeById ---

    @Test
    void getPublicationTypeByIdReturnsOkResponse() throws Exception {
        // Arrange
        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeResponseDTO dto = new PublicationTypeResponseDTO("BOOK");

        when(publicationTypeService.getPublicationTypeById("BOOK")).thenReturn(publicationType);
        when(publicationTypeResponseDTOMapper.toModel(publicationType)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/publicationTypes/BOOK")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getPublicationTypeByIdReturnsNotFoundWhenNotExists() throws Exception {
        // Arrange
        when(publicationTypeService.getPublicationTypeById("UNKNOWN"))
                .thenThrow(new NoSuchElementException("PublicationType with id 'UNKNOWN' does not exist"));

        // Act & Assert
        mockMvc.perform(get("/publicationTypes/UNKNOWN")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // --- options ---

    @Test
    void optionsShouldReturn200WithLinksForAuthorizedUser() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(publicationTypeLinkProvider.getLinks(userDouble)).thenReturn(List.of(
                Link.of("/publicationTypes").withRel("publication-types")
        ));

        // Act & Assert
        mockMvc.perform(options("/publicationTypes")
                        .param("email", "pedro@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.publication-types").exists());
    }

    @Test
    void optionsShouldReturn200WithNoLinksForUnauthorizedUser() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userService.getUserByEmail("readonly@aeiou.com")).thenReturn(userDouble);
        when(publicationTypeLinkProvider.getLinks(userDouble)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(options("/publicationTypes")
                        .param("email", "readonly@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void optionsShouldReturn404WhenUserNotFound() throws Exception {
        // Arrange
        when(userService.getUserByEmail("naoexiste@aeiou.com"))
                .thenThrow(new NoSuchElementException("User not found"));

        // Act & Assert
        mockMvc.perform(options("/publicationTypes")
                        .param("email", "naoexiste@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());
    }
}