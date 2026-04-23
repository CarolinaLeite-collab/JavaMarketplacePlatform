package MITELOVERS.controller;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterPublishingCompanyControllerTest {

    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;
    private PublishingCompanyFactory _publishingCompanyFactoryDouble;

    @BeforeEach
    void setUp() {

        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);
        _publishingCompanyFactoryDouble = mock(PublishingCompanyFactory.class);

    }

    @Test
    void constructorShouldInitializeController() {

        // Act & SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _publishingCompanyFactoryDouble);

        //Assert
        assertNotNull(controller);

    }

    @Test
    void shouldRegisterNewPublishingCompany() {

        //Arrange
        String publishingCompanyName = "Bertrand Editora";
        PublishingCompany pcDouble = mock(PublishingCompany.class);
        PublishingCompanyId pcIdDouble = mock(PublishingCompanyId.class);

        when(_publishingCompanyFactoryDouble.createPublishingCompany(publishingCompanyName)).thenReturn(pcDouble);
        when(pcDouble.identity()).thenReturn(pcIdDouble);

        when(_iPublishingCompanyRepoDouble.containsOfIdentity(pcIdDouble)).thenReturn(false);

        when(_iPublishingCompanyRepoDouble.save(pcDouble)).thenReturn(pcDouble);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _publishingCompanyFactoryDouble);

        //Act
        PublishingCompany publishingCompanyResult = controller.registerPublishingCompany(publishingCompanyName);

        //Assert
        assertEquals(pcDouble, publishingCompanyResult);
        verify(_publishingCompanyFactoryDouble).createPublishingCompany(publishingCompanyName);
        verify(pcDouble).identity();
        verify(_iPublishingCompanyRepoDouble).containsOfIdentity(pcIdDouble);
        verify(_iPublishingCompanyRepoDouble).save(pcDouble);
    }

    @Test
    void shouldNotRegisterExistingPublishingCompany() {

        //Arrange
        String publishingCompanyName = "Pendant Publishing";
        PublishingCompany pcDouble = mock(PublishingCompany.class);
        PublishingCompanyId pcIdDouble = mock(PublishingCompanyId.class);

        when(_publishingCompanyFactoryDouble.createPublishingCompany(publishingCompanyName)).thenReturn(pcDouble);
        when(pcDouble.identity()).thenReturn(pcIdDouble);

        when(_iPublishingCompanyRepoDouble.containsOfIdentity(pcIdDouble)).thenReturn(true);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _publishingCompanyFactoryDouble);

        //Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.registerPublishingCompany(publishingCompanyName));

        //Assert
        assertEquals(exception.getMessage(), "Publishing Company with name " + publishingCompanyName + " already exists");
    }

    @Test
    void shouldNotSaveWhenPublishingCompanyAlreadyExists() {
        String publishingCompanyName = "Pendant Publishing";
        PublishingCompany pcDouble = mock(PublishingCompany.class);
        PublishingCompanyId pcIdDouble = mock(PublishingCompanyId.class);

        when(_publishingCompanyFactoryDouble.createPublishingCompany(publishingCompanyName)).thenReturn(pcDouble);
        when(pcDouble.identity()).thenReturn(pcIdDouble);

        when(_iPublishingCompanyRepoDouble.containsOfIdentity(pcIdDouble)).thenReturn(true);

        //SUT
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_iPublishingCompanyRepoDouble, _publishingCompanyFactoryDouble);

        //Act
        assertThrows(IllegalArgumentException.class, () -> controller.registerPublishingCompany(publishingCompanyName));

        //Assert
        verify(_iPublishingCompanyRepoDouble, never()).save(pcDouble);
    }

}
