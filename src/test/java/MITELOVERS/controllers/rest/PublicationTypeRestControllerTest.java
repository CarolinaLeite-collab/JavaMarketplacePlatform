package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.PublicationTypeLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicationTypeRestController.class)
@Import(CustomRestExceptionHandler.class)
class PublicationTypeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PublicationTypeService publicationTypeService;

    @MockitoBean
    private PublicationTypeLinkProvider publicationTypeLinkProvider;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllPublicationTypesReturnsOkResponse() throws Exception {
        PublicationTypeResponseDTO dto = new PublicationTypeResponseDTO("BOOK");
        when(publicationTypeService.getAllPublicationTypes()).thenReturn(List.of(dto));

        mockMvc.perform(get("/publicationTypes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAllPublicationTypesReturnsNoContentWhenEmpty() throws Exception {
        when(publicationTypeService.getAllPublicationTypes()).thenReturn(List.of());

        mockMvc.perform(get("/publicationTypes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPublicationTypeByIdReturnsOkResponse() throws Exception {
        PublicationTypeResponseDTO dto = new PublicationTypeResponseDTO("BOOK");
        when(publicationTypeService.getPublicationTypeById("BOOK")).thenReturn(dto);

        mockMvc.perform(get("/publicationTypes/BOOK")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void optionsShouldReturn200WithLinksForAuthorizedUser() throws Exception {
        User userDouble = mock(User.class);
        when(userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(publicationTypeLinkProvider.getLinks(userDouble)).thenReturn(List.of(
                Link.of("/publicationTypes").withRel("publication-types")
        ));

        mockMvc.perform(options("/publicationTypes")
                        .param("email", "pedro@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.publication-types").exists());
    }

    @Test
    void optionsShouldReturn200WithNoLinksForUnauthorizedUser() throws Exception {
        User userDouble = mock(User.class);
        when(userService.getUserByEmail("readonly@aeiou.com")).thenReturn(userDouble);
        when(publicationTypeLinkProvider.getLinks(userDouble)).thenReturn(List.of());

        mockMvc.perform(options("/publicationTypes")
                        .param("email", "readonly@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void optionsShouldReturn404WhenUserNotFound() throws Exception {
        when(userService.getUserByEmail("naoexiste@aeiou.com"))
                .thenThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(options("/publicationTypes")
                        .param("email", "naoexiste@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());
    }
}