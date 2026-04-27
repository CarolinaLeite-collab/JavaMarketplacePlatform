package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationTypeDataModelTest {

    private PublicationType _publicationTypeDouble;
    private PublicationTypeId _publicationTypeIdDouble;

    @BeforeEach
    void setUp() {

        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypeIdDouble = mock(PublicationTypeId.class);

    }

    @Test
    void shouldConstructPublicationTypeDataModel() {

        // Arrange
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);

        // SUT & Act
        PublicationTypeDataModel dataModel = new PublicationTypeDataModel(_publicationTypeDouble);

    }

    @Test
    void shouldCreateDataModelFromDomainObject() {

        // Arrange
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");

        // SUT & Act
        PublicationTypeDataModel dataModel = new PublicationTypeDataModel(_publicationTypeDouble);
        String result = dataModel.getPublicationTypeId();

        // Assert
        assertEquals("BOOK", result);

    }

    @Test
    void shouldNormalizeIdToUpperCase() {

        // Arrange
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);
        when(_publicationTypeIdDouble.toString()).thenReturn("MAGAZINE");

        // SUT & Act
        PublicationTypeDataModel dataModel = new PublicationTypeDataModel(_publicationTypeDouble);
        String result = dataModel.getPublicationTypeId();

        // Assert
        assertEquals("MAGAZINE", result);

    }

    @Test
    void shouldCreateEmptyDataModelWithNoArgsConstructor() {

        // SUT & Act
        PublicationTypeDataModel dataModel = new PublicationTypeDataModel();
        String result = dataModel.getPublicationTypeId();

        // Assert
        assertNull(result);

    }

    @Test
    void shouldCreateDataModelWithAllArgsConstructor() {

        // SUT & Act
        PublicationTypeDataModel dataModel = new PublicationTypeDataModel("BOOK");
        String result = dataModel.getPublicationTypeId();

        // Assert
        assertEquals("BOOK", result);
    }

}