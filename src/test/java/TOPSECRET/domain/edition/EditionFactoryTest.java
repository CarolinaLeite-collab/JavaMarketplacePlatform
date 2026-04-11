import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.edition.EditionBook;
import TOPSECRET.domain.edition.EditionFactory;
import TOPSECRET.domain.edition.EditionMagazine;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionFactoryTest {

    private BookId _bookIdDouble;
    private PublicationId _publicationIdDouble;
    private PublishingCompanyId _companyIdDouble;
    private Year _publishingYearDouble;
    private Language _languageDouble;
    private Dimension _dimensionDouble;
    private Weight _weightDouble;
    private NumberOfPages _pagesDouble;
    private EditionNumber _editionNumberDouble;
    private Binding _bindingDouble;

    private MagazineId _magazineIdDouble;
    private Periodicity _periodicityDouble;
    private IssueNumber _issueNumberDouble;

    @BeforeEach
    void setUp() {
        _bookIdDouble = mock(BookId.class);
        _publicationIdDouble = mock(PublicationId.class);
        _companyIdDouble = mock(PublishingCompanyId.class);
        _publishingYearDouble = Year.of(2020);
        _languageDouble = mock(Language.class);
        _dimensionDouble = mock(Dimension.class);
        _weightDouble = mock(Weight.class);
        _pagesDouble = mock(NumberOfPages.class);
        _editionNumberDouble = mock(EditionNumber.class);
        _bindingDouble = mock(Binding.class);

        _magazineIdDouble = mock(MagazineId.class);
        _issueNumberDouble = mock(IssueNumber.class);
        _periodicityDouble = mock(Periodicity.class);
    }

    @Test
    void shouldCreateEditionBookSuccessfullyPassingAllFields() {

        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEditionBook(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _pagesDouble,
                _editionNumberDouble,
                _bindingDouble
        );

        //Assert
        EditionBook book = assertInstanceOf(EditionBook.class, result);

        assertSame(_bookIdDouble, book.identity());
        assertSame(_publicationIdDouble, book.getPublicationId());
        assertSame(_companyIdDouble, book.getPublishingCompanyId());
        assertSame(_publishingYearDouble, book.getPublishingYear());
        assertSame(_languageDouble, book.getEditionLanguage());
        assertSame(_dimensionDouble, book.getDimension());
        assertSame(_weightDouble, book.getWeight());
        assertSame(_pagesDouble, book.getNumberOfPages());
        assertSame(_editionNumberDouble, book.getEditionNumber());
        assertSame(_bindingDouble, book.getBinding());
    }

    @Test
    void shouldCreateEditionBookSuccessfullyPassingOnlyMandatoryFields() {

        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEditionBook(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                null,
                null,
                null,
                null,
                null
        );

        //Assert
        EditionBook book = assertInstanceOf(EditionBook.class, result);

        assertSame(_bookIdDouble, book.identity());
        assertSame(_publicationIdDouble, book.getPublicationId());
        assertSame(_companyIdDouble, book.getPublishingCompanyId());
        assertSame(_publishingYearDouble, book.getPublishingYear());
        assertSame(_languageDouble, book.getEditionLanguage());
        assertNull(book.getDimension());
        assertNull(book.getWeight());
        assertNull(book.getNumberOfPages());
        assertNull(book.getEditionNumber());
        assertNull(book.getBinding());
    }

    @Test
    void shouldCreateEditionMagazineSuccessfullyWithAllFields() {

        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEditionMagazine(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                _dimensionDouble,
                _weightDouble,
                _issueNumberDouble,
                _periodicityDouble
        );

        //Assert
        EditionMagazine magazine = assertInstanceOf(EditionMagazine.class, result);

        assertSame(_magazineIdDouble, magazine.identity());
        assertSame(_publicationIdDouble, magazine.getPublicationId());
        assertSame(_companyIdDouble, magazine.getPublishingCompanyId());
        assertSame(_publishingYearDouble, magazine.getPublishingYear());
        assertSame(_languageDouble, magazine.getEditionLanguage());
        assertSame(_dimensionDouble, magazine.getDimension());
        assertSame(_weightDouble, magazine.getWeight());
        assertSame(_issueNumberDouble, magazine.getIssueNumber());
        assertSame(_periodicityDouble, magazine.getPeriodicity());
    }

    @Test
    void shouldCreateEditionMagazineSuccessfullyPassingOnlyMandatoryFields() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEditionMagazine(
                _magazineIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble,
                null,
                null,
                _issueNumberDouble,
                _periodicityDouble
        );

        //Assert
        EditionMagazine magazine = assertInstanceOf(EditionMagazine.class, result);

        assertSame(_magazineIdDouble, magazine.identity());
        assertSame(_publicationIdDouble, magazine.getPublicationId());
        assertSame(_companyIdDouble, magazine.getPublishingCompanyId());
        assertSame(_publishingYearDouble, magazine.getPublishingYear());
        assertSame(_languageDouble, magazine.getEditionLanguage());
        assertNull(magazine.getDimension());
        assertNull(magazine.getWeight());
        assertSame(_issueNumberDouble, magazine.getIssueNumber());
        assertSame(_periodicityDouble, magazine.getPeriodicity());
    }

}