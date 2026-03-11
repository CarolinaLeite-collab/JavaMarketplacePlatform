package TOPSECRET.controller;

import TOPSECRET.domain.PublishingCompany;
import TOPSECRET.domain.PublishingCompanyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterPublishingCompanyControllerTest {

    private PublishingCompanyRepo _publishingCompanyRepoDouble;

    @BeforeEach
    void setUp() {

        _publishingCompanyRepoDouble = mock(PublishingCompanyRepo.class);

    }

    @Test
    void constructorShouldInitializeController() {

        // Arrange & Act
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_publishingCompanyRepoDouble);

    }

    @Test
    void shouldRegisterNewPublishingCompany() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";
        PublishingCompany pcDouble = mock(PublishingCompany.class);

        when(_publishingCompanyRepoDouble.registerPublishingCompany(publishingCompanyName)).thenReturn(pcDouble);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_publishingCompanyRepoDouble);

        //Act
        PublishingCompany publishingCompanyResult = controller.registerPublishingCompany(publishingCompanyName);

        //Assert
        assertEquals(pcDouble, publishingCompanyResult);
        verify(_publishingCompanyRepoDouble).registerPublishingCompany(publishingCompanyName);

    }

    @Test
    void addingExistingPublishingCompanyThrowsWhenAlreadyExists() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";

        when(_publishingCompanyRepoDouble.registerPublishingCompany(publishingCompanyName)).thenThrow(
                new IllegalArgumentException("Publishing Company with name " + publishingCompanyName + " already exists."));

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_publishingCompanyRepoDouble);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPublishingCompany(publishingCompanyName));

    }

}