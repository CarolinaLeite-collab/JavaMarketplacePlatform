package TOPSECRET.controller;

import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.repository.IPublishingCompanyRepo;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegisterPublishingCompanyControllerTest {

    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;
    private User _adminDouble;
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() {

        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);
        _adminDouble = mock(User.class);
        _adminIdDouble = mock(UserId.class);

    }

    @Test
    void constructorShouldInitializeController() {

        // Act & SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _adminIdDouble);

    }

    @Test
    void shouldRegisterNewPublishingCompany() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";
        PublishingCompany pcDouble = mock(PublishingCompany.class);

        when(_iPublishingCompanyRepoDouble.registerPublishingCompany(publishingCompanyName)).thenReturn(pcDouble);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _adminIdDouble);

        //Act
        PublishingCompany publishingCompanyResult = controller.registerPublishingCompany(_adminDouble, publishingCompanyName);

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
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _adminIdDouble);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerPublishingCompany(_adminDouble, publishingCompanyName));

    }

    @Test
    void shouldNotRegisterPublishingCompanySuccessfullyIfUserNotAdmin() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";
        PublishingCompany pcDouble = mock(PublishingCompany.class);

        when(_iPublishingCompanyRepoDouble.registerPublishingCompany(publishingCompanyName)).thenReturn(pcDouble);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _adminIdDouble);

        //Act
        SecurityException exception = assertThrows(
                SecurityException.class, () -> controller.registerPublishingCompany(_adminDouble, publishingCompanyName));

        //Assert
        assertEquals("User is not authorized to register publishing companies", exception.getMessage());

    }

}