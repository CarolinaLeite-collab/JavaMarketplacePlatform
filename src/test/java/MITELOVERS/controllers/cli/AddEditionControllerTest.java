package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.dto.request.EditionRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddEditionControllerTest {

    private EditionService _editionServiceDouble;

    @BeforeEach
    void setUp() {

        _editionServiceDouble = mock(EditionService.class);
    }

    @Test
    void addEditionReturnsEdition() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .identifier("9780747532743")
                .build();

        Edition editionDouble = mock(Edition.class);

        when(_editionServiceDouble.registerEdition(any(), any())).thenReturn(editionDouble);

        // SUT
        AddEditionController controller = new AddEditionController(_editionServiceDouble);

        // Act
        Edition result = controller.addEdition("1984-Orwell-G--F43DD6(1949)", dto);

        // Assert
        assertNotNull(result);
        assertSame(editionDouble, result);
    }

    @Test
    void addEditionDelegatesToService() {

        // Arrange
        EditionRequestDTO dto = EditionRequestDTO.builder()
                .publicationTypeId("BOOK")
                .publishingCompanyId("Secker and Warburg")
                .publishingYear(2000)
                .language("ENGLISH")
                .identifier("9780747532743")
                .build();

        Edition editionDouble = mock(Edition.class);
        String publicationId = "1984-Orwell-G--F43DD6(1949)";

        when(_editionServiceDouble.registerEdition(any(), any())).thenReturn(editionDouble);

        // SUT
        AddEditionController controller = new AddEditionController(_editionServiceDouble);

        // Act
        controller.addEdition(publicationId, dto);

        // Assert
        verify(_editionServiceDouble).registerEdition(publicationId, dto);
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