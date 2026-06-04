package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterPublishingCompanyControllerTest {

    private PublishingCompanyService _publishingCompanyServiceDouble;

    @BeforeEach
    void setUp() {
        _publishingCompanyServiceDouble = mock(PublishingCompanyService.class);
    }

    @Test
    void registerPublishingCompanyReturnsDTO() {
        // Arrange
        PublishingCompanyRequestDTO dtoDouble = mock(PublishingCompanyRequestDTO.class);
        PublishingCompanyResponseDTO responseDouble = mock(PublishingCompanyResponseDTO.class);

        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenReturn(responseDouble);

        // SUT
        RegisterPublishingCompanyController controller =
                new RegisterPublishingCompanyController(_publishingCompanyServiceDouble);

        // Act
        PublishingCompanyResponseDTO result = controller.registerPublishingCompany(dtoDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void registerPublishingCompanyServiceThrowsExceptionPropagates() {
        // Arrange
        PublishingCompanyRequestDTO dtoDouble = mock(PublishingCompanyRequestDTO.class);

        when(_publishingCompanyServiceDouble.registerPublishingCompany(any()))
                .thenThrow(new IllegalStateException("Publishing company already exists"));

        // SUT
        RegisterPublishingCompanyController controller =
                new RegisterPublishingCompanyController(_publishingCompanyServiceDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                controller.registerPublishingCompany(dtoDouble));
    }
}


