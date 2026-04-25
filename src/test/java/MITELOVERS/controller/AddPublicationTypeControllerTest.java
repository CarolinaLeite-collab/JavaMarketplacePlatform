package MITELOVERS.controller;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class AddPublicationTypeControllerTest {

    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
    private PublicationTypeFactory _publicationTypeFactoryDouble;
    private UserId _adminDoubleId;

    @BeforeEach
    void setUp() {

        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        _publicationTypeFactoryDouble = mock(PublicationTypeFactory.class);
        _adminDoubleId = mock(UserId.class);

    }

    @Test
    void constructorAddPublicationTypeControllerShouldCreateController() {

        //SUT
        new AddPublicationTypeController(_iPublicationTypeRepoDouble, _publicationTypeFactoryDouble, _adminDoubleId);

    }

    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);
        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(false);
        when(_iPublicationTypeRepoDouble.save(pubTypeDouble)).thenReturn(pubTypeDouble);

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_iPublicationTypeRepoDouble, _publicationTypeFactoryDouble, _adminDoubleId);

        //Act
        PublicationType pubTypeResult = controller.addPublicationType(publicationTypeName);

        //Assert
        assertEquals(pubTypeDouble, pubTypeResult);
        verify(_publicationTypeFactoryDouble).createPublicationType(publicationTypeName);
        verify(pubTypeDouble).identity();
        verify(_iPublicationTypeRepoDouble).containsOfIdentity(pubTypeIdDouble);
        verify(_iPublicationTypeRepoDouble).save(pubTypeDouble);
    }

    @Test
    void shouldNotAddExistingPublicationType() {

        //Arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);

        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(true);

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_iPublicationTypeRepoDouble, _publicationTypeFactoryDouble, _adminDoubleId);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.addPublicationType(publicationTypeName));

        //Assert
        assertEquals("The publication type " + publicationTypeName + " already exists.", exception.getMessage());
    }

    @Test
    void shouldNotSaveWhenPublicationTypeAlreadyExists() {

        //Arrange
        String publicationTypeName = "book";
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);

        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(true);

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_iPublicationTypeRepoDouble, _publicationTypeFactoryDouble, _adminDoubleId);

        //Act
        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationType(publicationTypeName));

        //Assert
        verify(_iPublicationTypeRepoDouble, never()).save(pubTypeDouble);
    }

}
