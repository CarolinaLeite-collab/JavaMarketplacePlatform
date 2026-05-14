package MITELOVERS.domain.edition;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionFactoryTest {

    private EditionId _editionIdDouble;

    private PublicationTypeId _bookTypeIdDouble;
    private Identifier _bookIdentifierDouble;

    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _companyIdDouble;
    private Language _languageDouble;

    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _pagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    private Year _publishingYear;


    @BeforeEach
    void setUp() {
        _editionIdDouble = mock(EditionId.class);

        _bookTypeIdDouble = mock(PublicationTypeId.class);
        when(_bookTypeIdDouble.isBook()).thenReturn(true);

        _bookIdentifierDouble = mock(ISBN.class);

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
    void shouldCreateEditionWithEditionId() {

        //Arrange
        EditionFactory factory = new EditionFactory();

        //Act
        //SUT
        Edition result = factory.createEdition(
                _editionIdDouble,
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYear,
                _languageDouble,
                null, null, null, null, null
        );

        //Assert
        assertNotNull(result);
        assertSame(_editionIdDouble, result.getEditionId());
        assertSame(_bookTypeIdDouble, result.getPublicationTypeId());
        assertSame(_bookIdentifierDouble, result.getIdentifier());
        assertSame(_publicationIdDouble, result.getPublicationId());
        assertSame(_companyIdDouble, result.getPublishingCompanyId());
        assertEquals(_publishingYear, result.getPublishingYear());
        assertSame(_languageDouble, result.getEditionLanguage());
        assertNull(result.getDimension());
        assertNull(result.getWeight());
        assertNull(result.getNumberOfPages());
        assertNull(result.getEditionNumber());
        assertNull(result.getBinding());
    }

    @Test
    void shouldCreateEditionWithGivenMandatoryArguments() {

        //Arrange
        EditionFactory factory = new EditionFactory();

        //Act
        //SUT
        Edition result = factory.createEdition(
                _bookTypeIdDouble,
                _bookIdentifierDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYear,
                _languageDouble,
                null, null, null, null, null
        );

        //Assert
        assertNotNull(result);
        assertSame(_bookTypeIdDouble, result.getPublicationTypeId());
        assertSame(_bookIdentifierDouble, result.getIdentifier());
        assertSame(_publicationIdDouble, result.getPublicationId());
        assertSame(_companyIdDouble, result.getPublishingCompanyId());
        assertEquals(_publishingYear, result.getPublishingYear());
        assertSame(_languageDouble, result.getEditionLanguage());
        assertNull(result.getDimension());
        assertNull(result.getWeight());
        assertNull(result.getNumberOfPages());
        assertNull(result.getEditionNumber());
        assertNull(result.getBinding());
    }

    @Test
    void shouldCreateEditionWithOptionalFields() {
        //Arrange
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEdition(
                _bookTypeIdDouble,
                _bookIdentifierDouble,
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

        //Assert
        assertNotNull(result);
        assertSame(_dimensionDouble, result.getDimension());
        assertSame(_weightDouble, result.getWeight());
        assertSame(_pagesDouble, result.getNumberOfPages());
        assertSame(_editionNumberDouble, result.getEditionNumber());
        assertSame(_bindingDouble, result.getBinding());
    }

}
