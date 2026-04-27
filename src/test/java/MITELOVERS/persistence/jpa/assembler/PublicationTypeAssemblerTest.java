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
    void shouldConstructPublicationTypeAssembler() {

        // SUT & Act
        PublicationTypeAssembler assembler = new PublicationTypeAssembler(_factoryDouble);

    }

    @Test
    void shouldConvertDomainToDataModel() {

        // Arrange
        when(_publicationTypeDouble.identity()).thenReturn(_publicationTypeIdDouble);
        when(_publicationTypeIdDouble.toString()).thenReturn("BOOK");

        // SUT
        PublicationTypeAssembler assembler = new PublicationTypeAssembler(_factoryDouble);

        // Act
        PublicationTypeDataModel result = assembler.domain2DM(_publicationTypeDouble);

        // Assert
        assertEquals("BOOK", result.getPublicationTypeId());

    }

    @Test
    void shouldConvertDataModelToDomain() {

        // Arrange
        when(_dataModelDouble.getPublicationTypeId()).thenReturn("BOOK");
        when(_factoryDouble.createPublicationType(any(PublicationTypeId.class))).thenReturn(_publicationTypeDouble);

        // SUT
        PublicationTypeAssembler assembler = new PublicationTypeAssembler(_factoryDouble);

        // Act
        PublicationType result = assembler.DM2Domain(_dataModelDouble);

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
        PublicationTypeAssembler assembler = new PublicationTypeAssembler(_factoryDouble);

        // Act
        PublicationTypeDataModel dm = assembler.domain2DM(_publicationTypeDouble);
        PublicationType reconstructed = assembler.DM2Domain(_dataModelDouble);

        // Assert
        assertEquals(_publicationTypeDouble, reconstructed);

    }

}