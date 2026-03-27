package TOPSECRET.controller;

import TOPSECRET.domain.IPublishingCompanyRepo;
import TOPSECRET.domain.MemoPublishingCompanyRepo;
import TOPSECRET.domain.PublishingCompany;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegisterPublishingCompanyControllerTest {

    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;

    @BeforeEach
    void setUp() {

        _iPublishingCompanyRepoDouble = mock(MemoPublishingCompanyRepo.class);

    }

    @Test
    void constructorShouldInitializeController() {

        // Arrange & Act
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble);

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
                new IllegalArgumentException("Publishing Company with name " + publishingCompanyName + " already exists."));

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPublishingCompany(publishingCompanyName));

    }

}