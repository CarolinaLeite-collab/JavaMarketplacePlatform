package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublishingCompanyLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublishingCompanyRestController.class)
class PublishingCompanyRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @MockitoBean
    private PublishingCompanyService _publishingCompanyServiceDouble;

    @MockitoBean
    private PublishingCompanyLinkProvider _linkProviderDouble;

    @MockitoBean
    private UserService _userServiceDouble;

    @Test
    void optionsReturnsOkWithLinks() throws Exception {
        User userDouble = mock(User.class);
        Link linkDouble = Link.of("/publishingCompanies").withRel("publishingCompanies");
        when(_userServiceDouble.getUserByEmail(any())).thenReturn(userDouble);
        when(_linkProviderDouble.getLinks(userDouble)).thenReturn(List.of(linkDouble));

        _mockMvc.perform(options("/publishingCompanies")
                        .param("email", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void optionsReturnsOkWithNoLinks() throws Exception {
        User userDouble = mock(User.class);
        when(_userServiceDouble.getUserByEmail(any())).thenReturn(userDouble);
        when(_linkProviderDouble.getLinks(userDouble)).thenReturn(List.of());

        _mockMvc.perform(options("/publishingCompanies")
                        .param("email", "readonly@aeiou.com"))
                .andExpect(status().isOk());
    }

    @Test
    void optionsUserNotFoundReturnsNotFound() throws Exception {
        when(_userServiceDouble.getUserByEmail(any()))
                .thenThrow(new NoSuchElementException("User not found"));

        _mockMvc.perform(options("/publishingCompanies")
                        .param("email", "unknown@aeiou.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registerPublishingCompanyReturnsOk() throws Exception {
        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenReturn(mock(PublishingCompanyResponseDTO.class));

        _mockMvc.perform(post("/publishingCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"publishingCompanyName\":\"Porto Editora\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void registerPublishingCompanyServiceThrowsReturnsInternalServerError() throws Exception {
        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenThrow(new IllegalStateException("Already exists"));

        _mockMvc.perform(post("/publishingCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"publishingCompanyName\":\"Porto Editora\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllPublishingCompaniesReturnsOk() throws Exception {
        when(_publishingCompanyServiceDouble.getAllPublishingCompanies())
                .thenReturn(List.of(mock(PublishingCompanyResponseDTO.class)));

        _mockMvc.perform(get("/publishingCompanies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAllPublishingCompaniesServiceThrowsReturnsInternalServerError() throws Exception {
        when(_publishingCompanyServiceDouble.getAllPublishingCompanies())
                .thenThrow(new RuntimeException("Error"));

        _mockMvc.perform(get("/publishingCompanies"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getPublishingCompanyByIdReturnsOk() throws Exception {
        when(_publishingCompanyServiceDouble.getPublishingCompanyById(any()))
                .thenReturn(mock(PublishingCompanyResponseDTO.class));

        _mockMvc.perform(get("/publishingCompanies/PORTO-EDITORA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void getPublishingCompanyByIdNotFoundReturnsInternalServerError() throws Exception {
        when(_publishingCompanyServiceDouble.getPublishingCompanyById(any()))
                .thenThrow(new NoSuchElementException("Not found"));

        _mockMvc.perform(get("/publishingCompanies/UNKNOWN"))
                .andExpect(status().isInternalServerError());
    }
}