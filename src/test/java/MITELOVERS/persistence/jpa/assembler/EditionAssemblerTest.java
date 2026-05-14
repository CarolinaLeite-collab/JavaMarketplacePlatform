package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.DimensionDataModel;
import MITELOVERS.persistence.jpa.datamodel.EditionDataModel;
import MITELOVERS.persistence.jpa.datamodel.PublicationIdDataModel;
import MITELOVERS.persistence.jpa.datamodel.WeightDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionAssemblerTest {

    private EditionFactory _editionFactoryDouble;
    private EditionDataModel _editionDataModel;
    private Edition _editionDouble;
    private EditionId _editionIdDouble;
    private PublicationTypeId _typeIdDouble;
    private ISBN _identifierDouble;
    private PublicationId _publicationIdDouble;
    private PublicationIdDataModel _pubIdDmDouble;
    private Title _titleDouble;
    private AuthorId _authorIdDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private Dimension _dimensionDouble;
    private DimensionDataModel _dimensionDmDouble;
    private Weight _weightDouble;
    private WeightDataModel _weightDmDouble;
    private NumberOfPages _numberOfPagesDouble;
    private EditionNumber _editionNumberDouble;


    @BeforeEach
    void setUp() {
        _editionFactoryDouble = mock(EditionFactory.class);
        _editionDataModel = mock(EditionDataModel.class);

        _editionDouble = mock(Edition.class);
        _editionIdDouble = mock(EditionId.class);
        _typeIdDouble = mock(PublicationTypeId.class);
        _identifierDouble = mock(ISBN.class);
        _publicationIdDouble = mock(PublicationId.class);
        _pubIdDmDouble = mock(PublicationIdDataModel.class);
        _titleDouble = mock(Title.class);
        _authorIdDouble = mock(AuthorId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _dimensionDouble = mock(Dimension.class);
        _dimensionDmDouble = mock(DimensionDataModel.class);
        _weightDouble = mock(Weight.class);
        _weightDmDouble = mock(WeightDataModel.class);
        _numberOfPagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble = mock(EditionNumber.class);

        when(_editionDouble.identity()).thenReturn(_editionIdDouble);
        when(_editionIdDouble.toString()).thenReturn("E-ABC12345");
        when(_editionDouble.getPublicationTypeId()).thenReturn(_typeIdDouble);
        when(_typeIdDouble.toString()).thenReturn("BOOK");
        when(_editionDouble.getIdentifier()).thenReturn(_identifierDouble);
        when(_identifierDouble.getIdentifier()).thenReturn("978-3-16-148410-0");
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
        when(_publicationIdDouble.getTitle()).thenReturn(_titleDouble);
        when(_titleDouble.toString()).thenReturn("Clean Code");
        when(_publicationIdDouble.getAuthorId()).thenReturn(_authorIdDouble);
        when(_authorIdDouble.toString()).thenReturn("Martin R.U.-ABC123");
        when(_publicationIdDouble.getReleaseYear()).thenReturn(Year.of(2008));
        when(_editionDouble.getPublishingCompanyId()).thenReturn(_publishingCompanyIdDouble);
        when(_publishingCompanyIdDouble.toString()).thenReturn("PRENTICE HALL");
        when(_editionDouble.getPublishingYear()).thenReturn(Year.of(2008));
        when(_editionDouble.getEditionLanguage()).thenReturn(Language.ENGLISH);

        when(_editionDataModel.getId()).thenReturn("E-ABC12345");
        when(_editionDataModel.getPublishingCompanyId()).thenReturn("PUB-123");
        when(_editionDataModel.getPublishingYear()).thenReturn(2008);
        when(_editionDataModel.getPublicationIdDm()).thenReturn(_pubIdDmDouble);
        when(_pubIdDmDouble.getTitle()).thenReturn("Clean Code");
        when(_pubIdDmDouble.getAuthorId()).thenReturn("Martin R.U.-ABC123");
        when(_pubIdDmDouble.getReleaseYear()).thenReturn(2008);
        when(_editionDataModel.getEditionLanguage()).thenReturn(Language.ENGLISH);
        when(_editionDataModel.getNumberOfPages()).thenReturn(300);

    }

    @Test
    void toDataModelMandatoryFieldsMapsCorrectly() {
        // SUT
        EditionAssembler editionAssembler = new EditionAssembler(_editionFactoryDouble);

        // Act
        EditionDataModel result = editionAssembler.toDataModel(_editionDouble);

        // Assert
        assertAll(
                () -> assertEquals("E-ABC12345", result.getId()),
                () -> assertEquals("BOOK", result.getTypeId()),
                () -> assertEquals("978-3-16-148410-0", result.getIdentifier()),
                () -> assertEquals("ISBN", result.getIdentifierType()),
                () -> assertEquals("Clean Code", result.getPublicationIdDm().getTitle()),
                () -> assertEquals("Martin R.U.-ABC123", result.getPublicationIdDm().getAuthorId()),
                () -> assertEquals(2008, result.getPublicationIdDm().getReleaseYear()),
                () -> assertEquals("PRENTICE HALL", result.getPublishingCompanyId()),
                () -> assertEquals(2008, result.getPublishingYear()),
                () -> assertEquals(Language.ENGLISH, result.getEditionLanguage()),
                () -> assertNull(result.getDimensionDm()),
                () -> assertNull(result.getWeightDm()),
                () -> assertNull(result.getNumberOfPages()),
                () -> assertNull(result.getEditionNumber()),
                () -> assertNull(result.getBinding())
        );
    }

    @Test
    void toDataModelWithOptionalFieldsMapsCorrectly() {
        // Arrange
        when(_editionDouble.getDimension()).thenReturn(_dimensionDouble);
        when(_dimensionDouble.getWidth()).thenReturn(20.0);
        when(_dimensionDouble.getHeight()).thenReturn(25.0);
        when(_dimensionDouble.getThickness()).thenReturn(3.0);
        when(_dimensionDouble.getUnit()).thenReturn(DimensionUnit.CENTIMETERS);
        when(_editionDouble.getWeight()).thenReturn(_weightDouble);
        when(_weightDouble.getValue()).thenReturn(500.0);
        when(_weightDouble.getWeightUnit()).thenReturn(Weight.WeightUnit.GRAMS);
        when(_editionDouble.getNumberOfPages()).thenReturn(_numberOfPagesDouble);
        when(_numberOfPagesDouble.getNumberOfPages()).thenReturn(464);
        when(_editionDouble.getEditionNumber()).thenReturn(_editionNumberDouble);
        when(_editionNumberDouble.getValue()).thenReturn(1);
        when(_editionDouble.getBinding()).thenReturn(Binding.HARDCOVER);

        // SUT
        EditionAssembler editionAssembler = new EditionAssembler(_editionFactoryDouble);

        // Act
        EditionDataModel result = editionAssembler.toDataModel(_editionDouble);

        // Assert
        assertAll(
                () -> assertNotNull(result.getDimensionDm()),
                () -> assertEquals(20.0, result.getDimensionDm().getWidth()),
                () -> assertEquals(25.0, result.getDimensionDm().getHeight()),
                () -> assertEquals(3.0, result.getDimensionDm().getThickness()),
                () -> assertNotNull(result.getWeightDm()),
                () -> assertEquals(500.0, result.getWeightDm().getValue()),
                () -> assertEquals(464, result.getNumberOfPages()),
                () -> assertEquals(1, result.getEditionNumber()),
                () -> assertEquals(Binding.HARDCOVER, result.getBinding())
        );
    }

    @Test
    void toDomainWithAllOptionalFieldsReturnsEdition() {
        // Arrange
        when(_editionDataModel.getTypeId()).thenReturn("BOOK");
        when(_editionDataModel.getIdentifierType()).thenReturn("ISBN");
        when(_editionDataModel.getIdentifier()).thenReturn("978-3-16-148410-0");
        when(_editionDataModel.getBinding()).thenReturn(Binding.HARDCOVER);
        when(_editionDataModel.getDimensionDm()).thenReturn(_dimensionDmDouble);
        when(_dimensionDmDouble.getWidth()).thenReturn(20.0);
        when(_dimensionDmDouble.getHeight()).thenReturn(30.0);
        when(_dimensionDmDouble.getThickness()).thenReturn(5.0);
        when(_dimensionDmDouble.getUnit()).thenReturn("cm");
        when(_editionDataModel.getWeightDm()).thenReturn(_weightDmDouble);
        when(_weightDmDouble.getValue()).thenReturn(1.5);
        when(_weightDmDouble.getWeightUnit()).thenReturn("kg");
        when(_editionDataModel.getEditionNumber()).thenReturn(1);

        when(_editionFactoryDouble.createEdition(
                any(EditionId.class),
                any(PublicationTypeId.class),
                any(ISBN.class),
                any(PublicationId.class),
                any(PublishingCompanyId.class),
                any(Year.class),
                any(Language.class),
                any(Dimension.class),
                any(Weight.class),
                any(NumberOfPages.class),
                any(EditionNumber.class),
                eq(Binding.HARDCOVER)
        )).thenReturn(_editionDouble);

        // SUT
        EditionAssembler editionAssembler = new EditionAssembler(_editionFactoryDouble);

        // Act
        Edition result = editionAssembler.toDomain(_editionDataModel);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomainWithISBNReturnsEdition() {
        // Arrange
        when(_editionDataModel.getTypeId()).thenReturn("BOOK");
        when(_editionDataModel.getIdentifier()).thenReturn("978-3-16-148410-0");
        when(_editionDataModel.getIdentifierType()).thenReturn("ISBN");
        when(_editionDataModel.getEditionNumber()).thenReturn(null);
        when(_editionDataModel.getBinding()).thenReturn(null);
        when(_editionDataModel.getDimensionDm()).thenReturn(null);
        when(_editionDataModel.getWeightDm()).thenReturn(null);

        when(_editionFactoryDouble.createEdition(any(EditionId.class), any(PublicationTypeId.class), any(ISBN.class),
                any(PublicationId.class), any(PublishingCompanyId.class), any(Year.class), any(Language.class),
                isNull(), isNull(), any(NumberOfPages.class), isNull(), isNull())).thenReturn(_editionDouble);

        // SUT
        EditionAssembler editionAssembler = new EditionAssembler(_editionFactoryDouble);

        // Act
        Edition result = editionAssembler.toDomain(_editionDataModel);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomainWithISSNReturnsEdition() {
        //Arrange
        when(_editionDataModel.getTypeId()).thenReturn("MAGAZINE");
        when(_editionDataModel.getIdentifier()).thenReturn("2156-5570");
        when(_editionDataModel.getIdentifierType()).thenReturn("ISSN");
        when(_editionDataModel.getEditionNumber()).thenReturn(null);
        when(_editionDataModel.getBinding()).thenReturn(null);
        when(_editionDataModel.getDimensionDm()).thenReturn(null);
        when(_editionDataModel.getWeightDm()).thenReturn(null);

        when(_editionFactoryDouble.createEdition(any(EditionId.class), any(PublicationTypeId.class), any(ISSN.class),
                any(PublicationId.class), any(PublishingCompanyId.class), any(Year.class), any(Language.class),
                isNull(), isNull(), any(NumberOfPages.class), isNull(), isNull())).thenReturn(_editionDouble);

        // SUT
        EditionAssembler editionAssembler = new EditionAssembler(_editionFactoryDouble);

        // Act
        Edition result = editionAssembler.toDomain(_editionDataModel);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomainWithNoIdentifierReturnEdition() {

        // Arrange
        when(_editionDataModel.getTypeId()).thenReturn("MAGAZINE");
        when(_editionDataModel.getIdentifier()).thenReturn(null);
        when(_editionDataModel.getIdentifierType()).thenReturn("NoIdentifier");
        when(_editionDataModel.getEditionNumber()).thenReturn(null);
        when(_editionDataModel.getBinding()).thenReturn(null);
        when(_editionDataModel.getDimensionDm()).thenReturn(null);
        when(_editionDataModel.getWeightDm()).thenReturn(null);

        when(_editionFactoryDouble.createEdition(any(EditionId.class), any(PublicationTypeId.class), any(NoIdentifier.class),
                any(PublicationId.class), any(PublishingCompanyId.class), any(Year.class), any(Language.class),
                isNull(), isNull(), any(NumberOfPages.class), isNull(), isNull())).thenReturn(_editionDouble);

        // SUT
        EditionAssembler editionAssembler = new EditionAssembler(_editionFactoryDouble);

        // Act
        Edition result = editionAssembler.toDomain(_editionDataModel);

        // Assert
        assertNotNull(result);

    }

    @Test
    void toDomainWithUnknownIdentifierTypeThrowsException() {
        // Arrange
        when(_editionDataModel.getTypeId()).thenReturn("BOOK");
        when(_editionDataModel.getIdentifier()).thenReturn("215");
        when(_editionDataModel.getIdentifierType()).thenReturn("UNKNOWN");
        when(_editionDataModel.getEditionNumber()).thenReturn(null);
        when(_editionDataModel.getBinding()).thenReturn(null);
        when(_editionDataModel.getDimensionDm()).thenReturn(null);
        when(_editionDataModel.getWeightDm()).thenReturn(null);

        when(_editionFactoryDouble.createEdition(any(EditionId.class), any(PublicationTypeId.class), any(NoIdentifier.class),
                any(PublicationId.class), any(PublishingCompanyId.class), any(Year.class), any(Language.class),
                isNull(), isNull(), any(NumberOfPages.class), isNull(), isNull())).thenReturn(_editionDouble);

        // SUT
        EditionAssembler editionAssembler = new EditionAssembler(_editionFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> editionAssembler.toDomain(_editionDataModel));
    }



}