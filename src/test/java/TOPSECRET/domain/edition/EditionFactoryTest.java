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

    private BookId _bookId;
    private PublicationId _publicationId;
    private PublishingCompanyId _companyId;
    private Year _publishingYear;
    private Language _language;
    private Dimension _dimension;
    private Weight _weight;
    private NumberOfPages _pages;
    private EditionNumber _editionNumber;
    private Binding _binding;

    private MagazineId _magazineId;
    private Periodicity _periodicity;
    private IssueNumber _issueNumber;

    @BeforeEach
    void setUp() {
        _bookId = mock(BookId.class);
        _publicationId = mock(PublicationId.class);
        _companyId = mock(PublishingCompanyId.class);
        _publishingYear = Year.of(2020);
        _language = mock(Language.class);
        _dimension = mock(Dimension.class);
        _weight = mock(Weight.class);
        _pages = mock(NumberOfPages.class);
        _editionNumber = mock(EditionNumber.class);
        _binding = mock(Binding.class);

        _magazineId = mock(MagazineId.class);
        _issueNumber = mock(IssueNumber.class);
        _periodicity = mock(Periodicity.class);
    }

    @Test
    void shouldCreateEditionBookSuccessfullyPassingAllFields() {

        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEditionBook(
                _bookId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language,
                _dimension,
                _weight,
                _pages,
                _editionNumber,
                _binding
        );

        //Assert
        EditionBook book = assertInstanceOf(EditionBook.class, result);

        assertSame(_bookId, book.getId());
        assertSame(_publicationId, book.getPublication());
        assertSame(_companyId, book.getPublishingCompany());
        assertSame(_publishingYear, book.getPublishingYear());
        assertSame(_language, book.getEditionLanguage());
        assertSame(_dimension, book.getDimension());
        assertSame(_weight, book.getWeight());
        assertSame(_pages, book.getNumberOfPages());
        assertSame(_editionNumber, book.getEditionNumber());
        assertSame(_binding, book.getBinding());
    }

    @Test
    void shouldCreateEditionBookSuccessfullyPassingOnlyMandatoryFields() {

        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEditionBook(
                _bookId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language,
                null,
                null,
                null,
                null,
                null
        );

        //Assert
        EditionBook book = assertInstanceOf(EditionBook.class, result);

        assertSame(_bookId, book.getId());
        assertSame(_publicationId, book.getPublication());
        assertSame(_companyId, book.getPublishingCompany());
        assertSame(_publishingYear, book.getPublishingYear());
        assertSame(_language, book.getEditionLanguage());
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
                _magazineId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language,
                _dimension,
                _weight,
                _issueNumber,
                _periodicity
        );

        //Assert
        EditionMagazine magazine = assertInstanceOf(EditionMagazine.class, result);

        assertSame(_magazineId, magazine.getId());
        assertSame(_publicationId, magazine.getPublication());
        assertSame(_companyId, magazine.getPublishingCompany());
        assertSame(_publishingYear, magazine.getPublishingYear());
        assertSame(_language, magazine.getEditionLanguage());
        assertSame(_dimension, magazine.getDimension());
        assertSame(_weight, magazine.getWeight());
        assertSame(_issueNumber, magazine.getIssueNumber());
        assertSame(_periodicity, magazine.getPeriodicity());
    }

    @Test
    void shouldThrowWhenPublishingYearIsNullForBook() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.createEditionBook(
                        _bookId,
                        _publicationId,
                        _companyId,
                        null,
                        _language,
                        _dimension,
                        _weight,
                        _pages,
                        _editionNumber,
                        _binding
                )
        );
    }

    @Test
    void shouldThrowWhenPublishingYearIsNullForMagazine() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.createEditionMagazine(
                        _magazineId,
                        _publicationId,
                        _companyId,
                        null,
                        _language,
                        _dimension,
                        _weight,
                        _issueNumber,
                        _periodicity
                )
        );
    }

    @Test
    void shouldCreateEditionMagazineSuccessfullyPassingOnlyMandatoryFields() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act
        Edition result = factory.createEditionMagazine(
                _magazineId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language,
                null,
                null,
                _issueNumber,
                _periodicity
        );

        //Assert
        EditionMagazine magazine = assertInstanceOf(EditionMagazine.class, result);

        assertSame(_magazineId, magazine.getId());
        assertSame(_publicationId, magazine.getPublication());
        assertSame(_companyId, magazine.getPublishingCompany());
        assertSame(_publishingYear, magazine.getPublishingYear());
        assertSame(_language, magazine.getEditionLanguage());
        assertNull(magazine.getDimension());
        assertNull(magazine.getWeight());
        assertSame(_issueNumber, magazine.getIssueNumber());
        assertSame(_periodicity, magazine.getPeriodicity());
    }

    @Test
    void shouldAllowNoIdBookBefore1970() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        NoIdBook noId = new NoIdBook();
        Year year = Year.of(1960);

        //Act
        Edition edition = factory.createEditionBook(
                noId,
                _publicationId,
                _companyId,
                year,
                _language,
                _dimension,
                _weight,
                _pages,
                _editionNumber,
                _binding
        );

        assertNotNull(edition);
    }

    @Test
    void shouldRejectNoIdBookAfter1970() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        NoIdBook noId = mock(NoIdBook.class);
        Year year = Year.of(2000);

        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createEditionBook(noId,
                        _publicationId,
                        _companyId,
                        year,
                        _language,
                        _dimension,
                        _weight, _pages,
                        _editionNumber,
                        _binding
                )
        );

        // Assert
        assertEquals(
                "Books published after 1970 must have a valid ISBN",
                exception.getMessage()
        );
    }

    @Test
    void shouldAllowNoIdMagazineBefore1976() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        NoIdMagazine noId = mock(NoIdMagazine.class);
        Year year = Year.of(1960);

        //Act
        Edition edition = factory.createEditionMagazine(
                noId,
                _publicationId,
                _companyId, year,
                _language,
                _dimension,
                _weight,
                _issueNumber,
                _periodicity
        );

        //Assert
        assertNotNull(edition);
    }

    @Test
    void shouldRejectNoIdMagazineAfter1976() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        NoIdMagazine noId = mock(NoIdMagazine.class);
        Year year = Year.of(2000);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                factory.createEditionMagazine(
                        noId,
                        _publicationId,
                        _companyId,
                        year,
                        _language,
                        _dimension,
                        _weight,
                        _issueNumber,
                        _periodicity
                )
        );

        // Assert
        assertEquals(
                "Magazines published after 1976 must have a valid ISSN",
                exception.getMessage()
        );
    }

    //boundary tests
    @Test
    void shouldAllowNoIdBookIn1970() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        NoIdBook noId = mock(NoIdBook.class);
        Year year = Year.of(1970);

        //Act
        Edition edition = factory.createEditionBook(
                noId,
                _publicationId,
                _companyId,
                year,
                _language,
                _dimension,
                _weight,
                _pages,
                _editionNumber,
                _binding
        );

        //Assert
        assertNotNull(edition);
    }

    @Test
    void shouldAllowNoIdMagazineIn1976() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        NoIdMagazine noId = mock(NoIdMagazine.class);
        Year year = Year.of(1976);

        //Act
        Edition edition = factory.createEditionMagazine(
                noId,
                _publicationId,
                _companyId,
                year,
                _language,
                _dimension,
                _weight,
                _issueNumber,
                _periodicity
        );

        //Assert
        assertNotNull(edition);
    }

    @Test
    void shouldThrowWhenIssueNumberIsNull() {
        //Arrange
        //SUT
        EditionFactory factory = new EditionFactory();

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.createEditionMagazine(
                        _magazineId,
                        _publicationId,
                        _companyId,
                        _publishingYear,
                        _language,
                        _dimension,
                        _weight,
                        null, // obrigatório
                        _periodicity
                )
        );
    }
}