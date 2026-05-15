package MITELOVERS.controller;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class AddPublicationTypeControllerTest {

    @Mock
    private IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @Mock
    private PublicationTypeFactory _publicationTypeFactoryDouble;

    @Mock
    private PublicationType _pubTypeDouble;

    @Mock
    private PublicationTypeId _pubTypeIdDouble;

    //SUT
    @InjectMocks
    AddPublicationTypeController _controller;


    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        String publicationTypeName = "book";

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(_pubTypeDouble);
        when(_pubTypeDouble.identity()).thenReturn(_pubTypeIdDouble);
        when(_iPublicationTypeRepoDouble.containsOfIdentity(_pubTypeIdDouble)).thenReturn(false);
        when(_iPublicationTypeRepoDouble.save(_pubTypeDouble)).thenReturn(_pubTypeDouble);

        //Act
        PublicationType pubTypeResult = _controller.addPublicationType(publicationTypeName);

        //Assert
        assertEquals(_pubTypeDouble, pubTypeResult);

    }

    @Test
    void shouldNotAddExistingPublicationType() {

        //Arrange
        String publicationTypeName = "book";

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(_pubTypeDouble);
        when(_pubTypeDouble.identity()).thenReturn(_pubTypeIdDouble);

        when(_iPublicationTypeRepoDouble.containsOfIdentity(_pubTypeIdDouble)).thenReturn(true);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> _controller.addPublicationType(publicationTypeName));

        //Assert
        assertEquals("The publication type "
                + publicationTypeName + " already exists.", exception.getMessage());

    }

    @Test
    void shouldNotSaveWhenPublicationTypeAlreadyExists() {

        //Arrange
        String publicationTypeName = "book";

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(_pubTypeDouble);
        when(_pubTypeDouble.identity()).thenReturn(_pubTypeIdDouble);

        when(_iPublicationTypeRepoDouble.containsOfIdentity(_pubTypeIdDouble)).thenReturn(true);

        //Act
        Exception result = assertThrows(IllegalArgumentException.class,
                () -> _controller.addPublicationType(publicationTypeName));

        //Assert
        assertEquals("The publication type " +
                publicationTypeName + " already exists.", result.getMessage());

    }

}
