import TOPSECRET.domain.edition.EditionBook;
import TOPSECRET.domain.edition.EditionFactory;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionBookTest {

    private EditionFactory _factoryDouble;
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

    private static final String expectedMessagePublicationId = "PublicationId is required";
    private static final String expectedMessageCompanyId = "PublishingCompanyId is required";
    private static final String expectedMessageYear = "PublishingYear is required";
    private static final String expectedMessageLanguage = "Language is required";
    private static final String expectedMessageIsbnRule = "Books published after 1970 must have a valid ISBN.";

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
    }

    @Test
    void shouldBuildEditionBookSuccessfully() {
        //Act
        //SUT
        EditionBook book = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        //Assert
        assertNotNull(book);
        assertSame(_bookIdDouble, book.identity());
        assertSame(_publicationIdDouble, book.getPublicationId());
        assertSame(_companyIdDouble, book.getPublishingCompanyId());
        assertSame(_publishingYearDouble, book.getPublishingYear());
        assertSame(_languageDouble, book.getEditionLanguage());
    }

    @Test
    void shouldBuildEditionBookWithOptionalFields() {
        //Act
        //SUT
        EditionBook book = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        )
                .withDimension(_dimensionDouble)
                .withWeight(_weightDouble)
                .withNumberOfPages(_pagesDouble)
                .withEditionNumber(_editionNumberDouble)
                .withBinding(_bindingDouble)
                .build();

        //Assert
        assertSame(_dimensionDouble, book.getDimension());
        assertSame(_weightDouble, book.getWeight());
        assertSame(_pagesDouble, book.getNumberOfPages());
        assertSame(_editionNumberDouble, book.getEditionNumber());
        assertSame(_bindingDouble, book.getBinding());
    }

    @Test
    void shouldThrowWhenPublicationIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new EditionBook.Builder(_bookIdDouble, null, _companyIdDouble, _publishingYearDouble, _languageDouble).build());

        //Assert
        assertEquals(expectedMessagePublicationId, exception.getMessage());
    }

    @Test
    void shouldThrowWhenPublishingCompanyIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new EditionBook.Builder(_bookIdDouble, _publicationIdDouble, null, _publishingYearDouble, _languageDouble).build());

        //Assert
        assertEquals(expectedMessageCompanyId, exception.getMessage());
    }

    @Test
    void shouldThrowWhenPublishingYearIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new EditionBook.Builder(_bookIdDouble, _publicationIdDouble, _companyIdDouble, null, _languageDouble).build());

        //Assert
        assertEquals(expectedMessageYear, exception.getMessage());
    }

    @Test
    void shouldThrowWhenLanguageIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionBook.Builder(
                        _bookIdDouble,
                        _publicationIdDouble,
                        _companyIdDouble,
                        _publishingYearDouble,
                        null
                ).build());

        //Assert
        assertEquals(expectedMessageLanguage, exception.getMessage());
    }

    @Test
    void shouldGenerateNoIsbnBookWhenBookIdIsNullAndYearIs1970OrBefore() {
        //Act
        //SUT
        EditionBook book = new EditionBook.Builder(
                null,
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1950),
                _languageDouble
        ).build();

        //Assert
        assertInstanceOf(NoIsbnBook.class, book.identity());
    }

    @Test
    void shouldGenerateNoIsbnBookWhenBookIdIsNullAndYearIs1970() {
        //Act
        //SUT
        EditionBook book = new EditionBook.Builder(
                null,
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1970),
                _languageDouble
        ).build();

        //Assert
        assertInstanceOf(NoIsbnBook.class, book.identity());
    }

    @Test
    void shouldThrowWhenNoIsbnBookIsUsedAfter1970() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new EditionBook.Builder(
                        null,
                        _publicationIdDouble,
                        _companyIdDouble,
                        Year.of(1990),
                        _languageDouble
                ).build());

        //Assert
        assertEquals(expectedMessageIsbnRule, exception.getMessage());
    }

    @Test
    void shouldReturnStringRepresentation() {
        //Arrange
        EditionBook book = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        String expected = "Id: " + _bookIdDouble +
                "\nPublication: " + _publicationIdDouble +
                "\nPublishing Company: " + _companyIdDouble +
                "\nYear: " + _publishingYearDouble +
                "\nLanguage: " + _languageDouble;

        //Act
        //SUT
        String result = book.toString();

        //Assert
        assertEquals(expected,result);
    }

    @Test
    void shouldReturnStringRepresentationWithOptionalFields() {
        //Arrange
        EditionBook book = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        )
                .withDimension(_dimensionDouble)
                .withWeight(_weightDouble)
                .withNumberOfPages(_pagesDouble)
                .withEditionNumber(_editionNumberDouble)
                .withBinding(_bindingDouble)
                .build();

        String expected = "Id: " + _bookIdDouble +
                "\nPublication: " + _publicationIdDouble +
                "\nPublishing Company: " + _companyIdDouble +
                "\nYear: " + _publishingYearDouble +
                "\nLanguage: " + _languageDouble +
                "\nDimension: " + _dimensionDouble +
                "\nWeight: " + _weightDouble +
                "\nNumber of pages: " + _pagesDouble +
                "\nEdition number: " + _editionNumberDouble +
                "\nBinding: " + _bindingDouble;

        //Act
        //SUT
        String result = book.toString();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void equalsShouldBeTrueWhenSameInstance() {
        //Arrange
        EditionBook book = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book.equals(book);

        //Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldBeTrueWhenSameBookId() {
        //Arrange
        EditionBook book1 = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.equals(book2);

        //Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldBeFalseWhenDifferentBookId() {
        //Arrange
        BookId otherBookId = mock(BookId.class);

        EditionBook book1 = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                otherBookId,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.equals(book2);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldBeFalseWhenDifferentType() {
        //Arrange
        EditionBook book = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        Object other = new Object();

        //Act
        //SUT
        boolean result = book.equals(other);

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldBeSameWhenSameAttributes() {
        //Arrange
        EditionBook book1 = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                _bookIdDouble,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.sameAs(book2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldBeTrueWhenSameISBN() {
        //Arrange
        BookId isbn = mock(ISBN.class);

        EditionBook book1 = new EditionBook.Builder(
                isbn,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                isbn,
                mock(PublicationId.class), // different
                mock(PublishingCompanyId.class),
                Year.of(1990),
                mock(Language.class)
        ).build();

        //Act
        boolean result = book1.sameAs(book2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldBeFalseWhenDifferentISBN() {
        //Arrange
        BookId isbn1 = mock(ISBN.class);
        BookId isbn2 = mock(ISBN.class);

        EditionBook book1 = new EditionBook.Builder(
                isbn1,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                isbn2,
                _publicationIdDouble,
                _companyIdDouble,
                _publishingYearDouble,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.sameAs(book2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeTrueWhenNonIsbnAndSameBusinessAttributes() {
        //Arrange
        Year publishingYear = Year.of(1930);
        BookId internalId1 = mock(NoIsbnBook.class);
        BookId internalId2 = mock(NoIsbnBook.class);

        EditionBook book1 = new EditionBook.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.sameAs(book2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIsbnAndDifferentPublicationId() {
        //Arrange
        Year publishingYear = Year.of(1930);

        PublicationId publicationId1 = mock(PublicationId.class);
        PublicationId publicationId2 = mock(PublicationId.class);

        BookId internalId1 = mock(NoIsbnBook.class);
        BookId internalId2 = mock(NoIsbnBook.class);

        EditionBook book1 = new EditionBook.Builder(
                internalId1,
                publicationId1,
                _companyIdDouble,
                publishingYear,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                internalId2,
                publicationId2,
                _companyIdDouble,
                publishingYear,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.sameAs(book2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIsbnAndDifferentPublishingCompanyId() {
        //Arrange
        Year publishingYear = Year.of(1930);

        PublishingCompanyId publishingCompanyId1 = mock(PublishingCompanyId.class);
        PublishingCompanyId publishingCompanyId2 = mock(PublishingCompanyId.class);

        BookId internalId1 = mock(NoIsbnBook.class);
        BookId internalId2 = mock(NoIsbnBook.class);

        EditionBook book1 = new EditionBook.Builder(
                internalId1,
                _publicationIdDouble,
                publishingCompanyId1,
                publishingYear,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                internalId2,
                _publicationIdDouble,
                publishingCompanyId2,
                publishingYear,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.sameAs(book2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIsbnAndDifferentPublishingYear() {
        //Arrange
        Year publishingYear1 = Year.of(1930);
        Year publishingYear2 = Year.of(1932);

        BookId internalId1 = mock(NoIsbnBook.class);
        BookId internalId2 = mock(NoIsbnBook.class);

        EditionBook book1 = new EditionBook.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear1,
                _languageDouble
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear2,
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book1.sameAs(book2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenNonIsbnAndDifferentLanguage() {
        //Arrange
        Year publishingYear = Year.of(1930);

        Language editionLanguage1 = mock(Language.class);
        Language editionLanguage2 = mock(Language.class);

        BookId internalId1 = mock(NoIsbnBook.class);
        BookId internalId2 = mock(NoIsbnBook.class);

        EditionBook book1 = new EditionBook.Builder(
                internalId1,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                editionLanguage1
        ).build();

        EditionBook book2 = new EditionBook.Builder(
                internalId2,
                _publicationIdDouble,
                _companyIdDouble,
                publishingYear,
                editionLanguage2
        ).build();

        //Act
        //SUT
        boolean result = book1.sameAs(book2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenObjectIsNull() {
        //Arrange
        EditionBook book = new EditionBook.Builder(
                new NoIsbnBook("BOOK-1"),
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1960),
                _languageDouble
        ).build();

        //Act
        //SUT
        boolean result = book.sameAs(null);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldBeFalseWhenObjectIsDifferentType() {
        //Arrange
        EditionBook book = new EditionBook.Builder(
                new NoIsbnBook("BOOK-1"),
                _publicationIdDouble,
                _companyIdDouble,
                Year.of(1960),
                _languageDouble
        ).build();

        Object other = new Object();

        //Act
        //SUT
        boolean result = book.sameAs(other);

        //Assert
        assertFalse(result);
    }
}