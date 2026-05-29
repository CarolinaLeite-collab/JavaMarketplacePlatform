package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.dto.EditionRequestDTO;
import MITELOVERS.dto.EditionResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EditionRestController.class)
class EditionRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @MockBean
    private EditionService _editionServiceDouble;

    @Autowired
    private ObjectMapper _objectMapper;

    // ── POST ────────────────────────────────────────────────────────────────

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

        when(_editionServiceDouble.registerEdition(any(), any()))
                .thenReturn(mock(EditionResponseDTO.class));

        // Act & Assert
        _mockMvc.perform(post("/editions")
                        .header("pubId", "1984-Orwell-G--F43DD6(1949)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerEditionServiceThrowsReturnsInternalServerError() throws Exception {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .build();

        when(_editionServiceDouble.registerEdition(any(), any()))
                .thenThrow(new IllegalStateException("Edition already exists"));

        // Act & Assert
        _mockMvc.perform(post("/editions")
                        .header("pubId", "1984-Orwell-G--F43DD6(1949)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void registerEditionPublicationNotFoundReturnsInternalServerError() throws Exception {
        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .build();

        when(_editionServiceDouble.registerEdition(any(), any()))
                .thenThrow(new NoSuchElementException("Publication not found"));

        // Act & Assert
        _mockMvc.perform(post("/editions")
                        .header("pubId", "1984-Orwell-G--F43DD6(1949)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    // ── GET all ─────────────────────────────────────────────────────────────

    @Test
    void getAllEditionsByPublicationReturnsOk() throws Exception {
        // Arrange
        when(_editionServiceDouble.getAllEditionsByPublication(any()))
                .thenReturn(List.of());

        // Act & Assert
        _mockMvc.perform(get("/editions")
                        .header("publicationId", "1984-Orwell-G--F43DD6(1949)"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEditionsByPublicationServiceThrowsReturnsInternalServerError() throws Exception {
        // Arrange
        when(_editionServiceDouble.getAllEditionsByPublication(any()))
                .thenThrow(new NoSuchElementException("Publication not found"));

        // Act & Assert
        _mockMvc.perform(get("/editions")
                        .header("publicationId", "1984-Orwell-G--F43DD6(1949)"))
                .andExpect(status().isInternalServerError());
    }

    // ── GET by id ───────────────────────────────────────────────────────────

    @Test
    void getEditionByIdReturnsOk() throws Exception {
        // Arrange
        when(_editionServiceDouble.getEditionById(any()))
                .thenReturn(mock(EditionResponseDTO.class));

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
        _mockMvc.perform(get("/editions/E-ABC12345"))
                .andExpect(status().isNotFound());
    }
}