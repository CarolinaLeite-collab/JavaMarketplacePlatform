package MITELOVERS.controller;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@WebMvcTest(AddPublicationTypeController.class)
@ActiveProfiles("jpa")
class AddPublicationTypeControllerTest {

    @MockBean
    private IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @MockBean
    private PublicationTypeFactory _publicationTypeFactoryDouble;

    @Autowired
    AddPublicationTypeController _controller;


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

        //Act
        PublicationType pubTypeResult = _controller.addPublicationType(publicationTypeName);

        //Assert
        assertEquals(pubTypeDouble, pubTypeResult);

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
        PublicationType pubTypeDouble = mock(PublicationType.class);
        PublicationTypeId pubTypeIdDouble = mock(PublicationTypeId.class);

        when(_publicationTypeFactoryDouble.createPublicationType(publicationTypeName)).thenReturn(pubTypeDouble);
        when(pubTypeDouble.identity()).thenReturn(pubTypeIdDouble);

        when(_iPublicationTypeRepoDouble.containsOfIdentity(pubTypeIdDouble)).thenReturn(true);

        //Act
        Exception result = assertThrows(IllegalArgumentException.class,
                () -> _controller.addPublicationType(publicationTypeName));

        //Assert
        assertEquals("The publication type " +
                publicationTypeName + " already exists.", result.getMessage());

    }

}
