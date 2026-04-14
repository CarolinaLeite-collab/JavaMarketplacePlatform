package TOPSECRET.domain.edition;

import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionFactoryTest {

    private PublicationTypeId _typeIdDouble;
    private Identifier _identifierDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _companyIdDouble;
    private Language _languageDouble;

    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _pagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    private Year _publishingYear;

    private static final String expectedMessageType = "Publication Type Id is required";

    @BeforeEach
    void setUp() {
        _typeIdDouble = mock(PublicationTypeId.class);
        _identifierDouble = mock(Identifier.class);
        _publicationIdDouble = mock(PublicationId.class);
        _companyIdDouble = mock(PublishingCompanyId.class);
        _languageDouble = mock(Language.class);

        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
        _pagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble = mock(EditionNumber.class);
        _bindingDouble = mock(Binding.class);

        _publishingYear = Year.of(2020);
    }

    @Test
    void shouldCreateEditionSuccessfully() {
        // Arrange
        EditionFactory factory = new EditionFactory();

        // Act
        Edition result = factory.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYear,
                _languageDouble,
                null, null, null, null, null
        );

        // Assert
        assertNotNull(result);
        assertSame(_typeIdDouble, result.getPublicationTypeId());
        assertSame(_identifierDouble, result.getIdentifier());
        assertSame(_publicationIdDouble, result.getPublicationId());
        assertSame(_companyIdDouble, result.getPublishingCompanyId());
        assertSame(_publishingYear, result.getPublishingYear());
        assertSame(_languageDouble, result.getEditionLanguage());
    }

    @Test
    void shouldCreateEditionWithOptionalFields() {
        // Arrange
        EditionFactory factory = new EditionFactory();

        // Act
        Edition result = factory.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYear,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        // Assert
        assertNotNull(result);
        assertSame(_dimensionDouble, result.getDimension());
        assertSame(_weightDouble, result.getWeight());
        assertSame(_pagesDouble, result.getNumberOfPages());
        assertSame(_editionNumberDouble, result.getEditionNumber());
        assertSame(_bindingDouble, result.getBinding());
    }

    @Test
    void shouldThrowWhenTypeIdIsNull() {
        // Arrange
        EditionFactory factory = new EditionFactory();

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createEdition(
                        null,
                        _identifierDouble,
                        _publicationIdDouble,
                        _companyIdDouble,
                        _publishingYear,
                        _languageDouble,
                        null, null, null, null, null
                )
        );

        // Assert
        assertEquals(expectedMessageType, exception.getMessage());
    }

    @Test
    void shouldUsePublicationTypeIdInEdition() {
        // Arrange
        EditionFactory factory = new EditionFactory();

        // Act
        Edition result = factory.createEdition(
                _typeIdDouble,
                _identifierDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYear,
                _languageDouble,
                null, null, null, null, null
        );

        // Assert
        assertSame(_typeIdDouble, result.getPublicationTypeId());
    }
}