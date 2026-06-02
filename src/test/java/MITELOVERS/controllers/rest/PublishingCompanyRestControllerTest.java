package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(PublishingCompanyRestController.class)
class PublishingCompanyRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @MockitoBean
    private PublishingCompanyService _publishingCompanyServiceDouble;

    @Autowired
    private ObjectMapper _objectMapper;

    @Test
    void registerPublishingCompanyReturnsOk() throws Exception {
        // Arrange
        PublishingCompanyResponseDTO responseDouble = mock(PublishingCompanyResponseDTO.class);
        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenReturn(responseDouble);

        // Act & Assert
        _mockMvc.perform(post("/publishingCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"publishingCompanyId\":\"Secker and Warburg\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void registerPublishingCompanyServiceThrowsReturnsInternalServerError() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenThrow(new IllegalStateException("Publishing company already exists"));

        // Act & Assert
        _mockMvc.perform(post("/publishingCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"publishingCompanyId\":\"Secker and Warburg\"}"))
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
        _mockMvc.perform(get("/publishingCompanies/SECKER AND WARBURG")
                        .param("publishingCompanyId", "SECKER AND WARBURG"))
                .andExpect(status().isOk());
    }

    @Test
    void getPublishingCompanyByIdNotFoundReturnsInternalServerError() throws Exception {
        // Arrange
        when(_publishingCompanyServiceDouble.getPublishingCompanyById(any()))
                .thenThrow(new NoSuchElementException("Publishing company not found"));

        // Act & Assert
        _mockMvc.perform(get("/publishingCompanies/UNKNOWN")
                        .param("publishingCompanyId", "UNKNOWN"))
                .andExpect(status().isInternalServerError());
    }
}