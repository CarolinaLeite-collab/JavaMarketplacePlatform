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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
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
        // Arrange
        User userDouble = mock(User.class);
        Link linkDouble = Link.of("/publishingCompanies").withRel("publishingCompanies");

        when(_userServiceDouble.getUserByEmail(any())).thenReturn(userDouble);
        when(_linkProviderDouble.getLinks(userDouble)).thenReturn(List.of(linkDouble));

        // Act & Assert
        _mockMvc.perform(options("/publishingCompanies")
                        .param("email", "pedro@aeiou.com"))
                .andExpect(status().isOk());
    }

    @Test
    void optionsUserNotFoundReturnsNotFound() throws Exception {
        // Arrange
        when(_userServiceDouble.getUserByEmail(any()))
                .thenThrow(new NoSuchElementException("User not found"));

        // Act & Assert
        _mockMvc.perform(options("/publishingCompanies")
                        .param("email", "unknown@aeiou.com"))
                .andExpect(status().isNotFound());
    }


    @Test
    void registerPublishingCompanyReturnsOk() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenReturn(mock(PublishingCompanyResponseDTO.class));

        // Act & Assert
        _mockMvc.perform(post("/publishingCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"publishingCompanyName\":\"Porto Editora\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void registerPublishingCompanyServiceThrowsReturnsInternalServerError() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenThrow(new IllegalStateException("Already exists"));

        // Act & Assert
        _mockMvc.perform(post("/publishingCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"publishingCompanyName\":\"Porto Editora\"}"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void getAllPublishingCompaniesReturnsOk() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.getAllPublishingCompanies())
                .thenReturn(List.of(mock(PublishingCompanyResponseDTO.class)));

        // Act & Assert
        _mockMvc.perform(get("/publishingCompanies"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllPublishingCompaniesServiceThrowsReturnsInternalServerError() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.getAllPublishingCompanies())
                .thenThrow(new RuntimeException("Error"));

        // Act & Assert
        _mockMvc.perform(get("/publishingCompanies"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void getPublishingCompanyByIdReturnsOk() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.getPublishingCompanyById(any()))
                .thenReturn(mock(PublishingCompanyResponseDTO.class));

        // Act & Assert
        _mockMvc.perform(get("/publishingCompanies/PORTO-EDITORA"))
                .andExpect(status().isOk());
    }

    @Test
    void getPublishingCompanyByIdNotFoundReturnsInternalServerError() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.getPublishingCompanyById(any()))
                .thenThrow(new NoSuchElementException("Not found"));

        // Act & Assert
        _mockMvc.perform(get("/publishingCompanies/UNKNOWN"))
                .andExpect(status().isInternalServerError());
    }
}