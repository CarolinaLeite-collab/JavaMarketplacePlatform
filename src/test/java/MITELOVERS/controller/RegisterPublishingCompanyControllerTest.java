package MITELOVERS.controller;

import MITELOVERS.controllers.cli.RegisterPublishingCompanyController;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")

class RegisterPublishingCompanyControllerTest {

    @Mock
    IPublishingCompanyRepo _iPublishingCompanyRepoDouble;

    @Mock
    PublishingCompanyFactory _publishingCompanyFactoryDouble;

    @InjectMocks
    RegisterPublishingCompanyController _registerPublishingCompanyController;

    private PublishingCompany _publishingCompanyDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp() {

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
