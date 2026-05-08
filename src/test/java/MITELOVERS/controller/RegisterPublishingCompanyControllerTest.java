package MITELOVERS.controller;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(RegisterPublishingCompanyController.class)
@ActiveProfiles("jpa")
class RegisterPublishingCompanyControllerTest {

    @MockBean
    IPublishingCompanyRepo _iPublishingCompanyRepoDouble;

    @MockBean
    PublishingCompanyFactory _publishingCompanyFactoryDouble;

    @Autowired
    RegisterPublishingCompanyController _registerPublishingCompanyController;

    private PublishingCompany _publishingCompanyDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp() throws InstantiationException {

        MockitoAnnotations.openMocks(this);

        _publishingCompanyDouble = mock(PublishingCompany.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);

    }

    @Test
    void constructorShouldInitializeController() {
        assertNotNull(_registerPublishingCompanyController);

    }

    @Test
    void shouldRegisterNewPublishingCompany() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";

        when(_publishingCompanyFactoryDouble.createPublishingCompany(publishingCompanyName)).thenReturn(_publishingCompanyDouble);
        when(_publishingCompanyDouble.identity()).thenReturn(_publishingCompanyIdDouble);

        when(_iPublishingCompanyRepoDouble.containsOfIdentity(_publishingCompanyIdDouble)).thenReturn(false);

        when(_iPublishingCompanyRepoDouble.save(_publishingCompanyDouble)).thenReturn(_publishingCompanyDouble);

        //Act
        PublishingCompany publishingCompanyResult =
                _registerPublishingCompanyController.registerPublishingCompany(publishingCompanyName);

        //Assert
        assertEquals(_publishingCompanyDouble, publishingCompanyResult);

    }

    @Test
    void shouldNotRegisterExistingPublishingCompany() {

        //Arrange
        String publishingCompanyName = "Pendant Publishing";

        when(_publishingCompanyFactoryDouble.createPublishingCompany(publishingCompanyName)).thenReturn(_publishingCompanyDouble);
        when(_publishingCompanyDouble.identity()).thenReturn(_publishingCompanyIdDouble);

        when(_iPublishingCompanyRepoDouble.containsOfIdentity(_publishingCompanyIdDouble)).thenReturn(true);

        //Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> _registerPublishingCompanyController.registerPublishingCompany(publishingCompanyName));

        //Assert
        assertEquals("Publishing Company with name " + publishingCompanyName + " already exists", exception.getMessage());
    }

    @Test
    void shouldNotSaveWhenPublishingCompanyAlreadyExists() {

        //Arrange
        String publishingCompanyName = "Pendant Publishing";

        when(_publishingCompanyFactoryDouble.createPublishingCompany(publishingCompanyName)).thenReturn(_publishingCompanyDouble);
        when(_publishingCompanyDouble.identity()).thenReturn(_publishingCompanyIdDouble);

        when(_iPublishingCompanyRepoDouble.containsOfIdentity(_publishingCompanyIdDouble)).thenReturn(true);

        //Act
        assertThrows(IllegalArgumentException.class,
                () -> _registerPublishingCompanyController.registerPublishingCompany(publishingCompanyName));

        //Assert
        verify(_iPublishingCompanyRepoDouble, never()).save(_publishingCompanyDouble);
    }

}
