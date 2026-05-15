package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationTypeAssemblerTest {

    private PublicationTypeFactory _factoryDouble;
    private PublicationType _publicationTypeDouble;
    private PublicationTypeId _publicationTypeIdDouble;
    private PublicationTypeDataModel _dataModelDouble;

    @BeforeEach
    void setUp() {

        _factoryDouble = mock(PublicationTypeFactory.class);
        _publicationTypeDouble = mock(PublicationType.class);
        _publicationTypeIdDouble = mock(PublicationTypeId.class);
        _dataModelDouble = mock(PublicationTypeDataModel.class);

    }

    @Test
    void shouldConvertDomainToDataModel() {

        // Arrange
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");

        // SUT
        PublicationTypeAssembler ptAssembler = new PublicationTypeAssembler(_factoryDouble);

        // Act
        PublicationTypeDataModel ptDataModel = ptAssembler.toDataModel(_publicationTypeDouble);
        String result = ptDataModel.getPublicationTypeId();

        // Assert
        assertEquals("BOOK", result);

    }

    @Test
    void shouldConvertDataModelToDomain() {

        // Arrange
        when(_dataModelDouble.getPublicationTypeId()).thenReturn("BOOK");
        when(_factoryDouble.createPublicationType(any(PublicationTypeId.class))).thenReturn(_publicationTypeDouble);

        // SUT
        PublicationTypeAssembler ptAssembler = new PublicationTypeAssembler(_factoryDouble);

        // Act
        PublicationType result = ptAssembler.toDomain(_dataModelDouble);

        // Assert
        assertEquals(_publicationTypeDouble, result);

    }

    @Test
    void shouldMaintainIdentityAfterRoundTrip() {

        // Arrange
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");
        when(_dataModelDouble.getPublicationTypeId()).thenReturn("BOOK");
        when(_factoryDouble.createPublicationType(any(PublicationTypeId.class))).thenReturn(_publicationTypeDouble);

        // SUT
        PublicationTypeAssembler ptAssembler = new PublicationTypeAssembler(_factoryDouble);

        // Act
        PublicationTypeDataModel ptDataModel = ptAssembler.toDataModel(_publicationTypeDouble);
        PublicationType result = ptAssembler.toDomain(_dataModelDouble);

        // Assert
        assertEquals(_publicationTypeDouble, result);

    }

}