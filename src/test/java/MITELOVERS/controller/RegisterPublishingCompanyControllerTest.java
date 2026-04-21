package MITELOVERS.controller;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterPublishingCompanyControllerTest {

    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;

    @BeforeEach
    void setUp() {

        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);

    }

    @Test
    void constructorShouldInitializeControllerAndBeNotNull() {

        // Act & SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble);

        //Assert
        assertNotNull(controller);

    }

    @Test
    void shouldRegisterNewPublishingCompany() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";
        PublishingCompany pcDouble = mock(PublishingCompany.class);

        when(_iPublishingCompanyRepoDouble.registerPublishingCompany(publishingCompanyName)).thenReturn(pcDouble);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble);

        //Act
        PublishingCompany publishingCompanyResult = controller.registerPublishingCompany(publishingCompanyName);

        //Assert
        assertEquals(pcDouble, publishingCompanyResult);
        verify(_iPublishingCompanyRepoDouble).registerPublishingCompany(publishingCompanyName);

    }

    @Test
    void addingExistingPublishingCompanyThrowsWhenAlreadyExists() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";

        when(_iPublishingCompanyRepoDouble.registerPublishingCompany(publishingCompanyName)).thenThrow(
                new IllegalArgumentException("This publishing company is already registered."));

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPublishingCompany(publishingCompanyName));

    }

}
