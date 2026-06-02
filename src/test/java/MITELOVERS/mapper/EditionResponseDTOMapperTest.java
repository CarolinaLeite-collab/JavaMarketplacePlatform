package MITELOVERS.mapper;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.EditionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionResponseDTOMapperTest {

    private Edition _editionDouble;
    private EditionId _editionIdDouble;
    private PublicationTypeId _typeIdDouble;
    private Identifier _identifierDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp() {

        _editionDouble = mock(Edition.class);
        _editionIdDouble = mock(EditionId.class);
        _typeIdDouble = mock(PublicationTypeId.class);
        _identifierDouble = mock(ISBN.class);
        _publicationIdDouble = mock(PublicationId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_editionDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_editionDouble.identity()).thenReturn(_editionIdDouble);
        when(_editionIdDouble.toString()).thenReturn("E-ABC12345");
        when(_editionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);
        when(_typeIdDouble.toString()).thenReturn("BOOK");
        when(_editionDouble.getIdentifier()).thenReturn(_identifierDouble);
        when(_identifierDouble.toString()).thenReturn("9780747532743");
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
        when(_publicationIdDouble.toString()).thenReturn("Clean Code - Martin R.U.-ABC123 (2008)");
        when(_editionDouble.getPublishingCompanyId()).thenReturn(_publishingCompanyIdDouble);
        when(_publishingCompanyIdDouble.toString()).thenReturn("PRENTICE HALL");
        when(_editionDouble.getPublishingYear()).thenReturn(Year.of(2008));
        when(_editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(_editionDouble.getDimension()).thenReturn(null);
        when(_editionDouble.getWeight()).thenReturn(null);
        when(_editionDouble.getNumberOfPages()).thenReturn(null);
        when(_editionDouble.getEditionNumber()).thenReturn(null);
        when(_editionDouble.getBinding()).thenReturn(null);

    }

    @Test
    void toModelMandatoryFieldsMapsCorrectly() {
        //Arrange & SUT
        EditionResponseDTOMapper _responseDTOMapper = new EditionResponseDTOMapper();

        // Act
        EditionResponseDTO result = _responseDTOMapper.toModel(_editionDouble);

        // Assert
        assertAll(
                () -> assertEquals("E-ABC12345", result.getEditionId()),
                () -> assertEquals("BOOK", result.getPublicationTypeId()),
                () -> assertEquals("9780747532743", result.getIdentifier()),
                () -> assertEquals("Clean Code - Martin R.U.-ABC123 (2008)", result.getPublicationId()),
                () -> assertEquals("PRENTICE HALL", result.getPublishingCompanyId()),
                () -> assertEquals(2008, result.getPublishingYear()),
                () -> assertEquals("en", result.getLanguage())
        );
    }

    @Test
    void toModelOptionalFieldsNullWhenNotPresent() {
        //Arrange & SUT
        EditionResponseDTOMapper _responseDTOMapper = new EditionResponseDTOMapper();

        // Act
        EditionResponseDTO result = _responseDTOMapper.toModel(_editionDouble);

        // Assert
        assertAll(
                () -> assertNull(result.getDimension()),
                () -> assertNull(result.getWeight()),
                () -> assertNull(result.getNumberOfPages()),
                () -> assertNull(result.getEditionNumber()),
                () -> assertNull(result.getBinding())
        );
    }

    @Test
    void toModelWithDimensionMapsDimensionDTO() {
        // Arrange
        Dimension dimensionDouble = mock(Dimension.class);
        when(dimensionDouble.getWidth()).thenReturn(20.0);
        when(dimensionDouble.getHeight()).thenReturn(25.0);
        when(dimensionDouble.getThickness()).thenReturn(3.0);
        when(dimensionDouble.getUnit()).thenReturn(DimensionUnit.CENTIMETERS);
        when(_editionDouble.getDimension()).thenReturn(dimensionDouble);

        //SUT
        EditionResponseDTOMapper _responseDTOMapper = new EditionResponseDTOMapper();

        // Act
        EditionResponseDTO result = _responseDTOMapper.toModel(_editionDouble);

        // Assert
        assertNotNull(result.getDimension());
        assertAll(
                () -> assertEquals(20.0, result.getDimension().getWidth()),
                () -> assertEquals(25.0, result.getDimension().getHeight()),
                () -> assertEquals(3.0, result.getDimension().getThickness()),
                () -> assertEquals("centimeters", result.getDimension().getUnit())
        );
    }

    @Test
    void toModelWithWeightMapsWeightDTO() {
        // Arrange
        Weight weightDouble = mock(Weight.class);
        when(weightDouble.getValue()).thenReturn(500.0);
        when(weightDouble.getWeightUnit()).thenReturn(Weight.WeightUnit.GRAMS);
        when(_editionDouble.getWeight()).thenReturn(weightDouble);

        //SUT
        EditionResponseDTOMapper _responseDTOMapper = new EditionResponseDTOMapper();

        // Act
        EditionResponseDTO result = _responseDTOMapper.toModel(_editionDouble);

        // Assert
        assertNotNull(result.getWeight());
        assertEquals(500.0, result.getWeight().getValue());
    }

    @Test
    void toModelWithNumberOfPagesMapsCorrectly() {
        // Arrange
        NumberOfPages numberOfPagesDouble = mock(NumberOfPages.class);
        when(numberOfPagesDouble.getNumberOfPages()).thenReturn(300);
        when(_editionDouble.getNumberOfPages()).thenReturn(numberOfPagesDouble);

        //SUT
        EditionResponseDTOMapper _responseDTOMapper = new EditionResponseDTOMapper();

        // Act
        EditionResponseDTO result = _responseDTOMapper.toModel(_editionDouble);

        // Assert
        assertEquals(300, result.getNumberOfPages());
    }

    @Test
    void toModelWithEditionNumberMapsCorrectly() {
        // Arrange
        EditionNumber editionNumberDouble = mock(EditionNumber.class);
        when(editionNumberDouble.getValue()).thenReturn(1);
        when(_editionDouble.getEditionNumber()).thenReturn(editionNumberDouble);

        //SUT
        EditionResponseDTOMapper _responseDTOMapper = new EditionResponseDTOMapper();

        // Act
        EditionResponseDTO result = _responseDTOMapper.toModel(_editionDouble);

        // Assert
        assertEquals(1, result.getEditionNumber());
    }

    @Test
    void toModelWithBindingMapsCorrectly() {
        // Arrange
        when(_editionDouble.getBinding()).thenReturn(Binding.HARDCOVER);

        //SUT
        EditionResponseDTOMapper _responseDTOMapper = new EditionResponseDTOMapper();

        // Act
        EditionResponseDTO result = _responseDTOMapper.toModel(_editionDouble);

        // Assert
        assertEquals("Hardcover binding", result.getBinding());
    }

}