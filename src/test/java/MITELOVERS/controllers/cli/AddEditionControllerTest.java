package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.dto.request.EditionRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddEditionControllerTest {

    private EditionService _editionServiceDouble;

    @BeforeEach
    void setUp() {

        _editionServiceDouble = mock(EditionService.class);
    }

    @Test
    void addEditionReturnsEditionResponseDTO() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .identifier("9780747532743")
                .build();

        EditionResponseDTO responseDouble = mock(EditionResponseDTO.class);

        when(_editionServiceDouble.registerEdition(any(), any())).thenReturn(responseDouble);

        // SUT
        AddEditionController controller = new AddEditionController(_editionServiceDouble);

        // Act
        EditionResponseDTO result = controller.addEdition("1984-Orwell-G--F43DD6(1949)", dto);

        // Assert
        assertNotNull(result);

    }

    @Test
    void addEditionServiceThrowsExceptionPropagates() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .build();

        when(_editionServiceDouble.registerEdition(any(), any()))
                .thenThrow(new NoSuchElementException("Publication not found"));

        // SUT
        AddEditionController controller = new AddEditionController(_editionServiceDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                controller.addEdition("1984-Orwell-G--F43DD6(1949)", dto));

    }

}