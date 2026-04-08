import TOPSECRET.domain.edition.EditionBook;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionBookTest {

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
    }

    @Test
    void shouldBuildEditionBookSuccessfully() {
        //Act
        //SUT
        EditionBook book = new EditionBook.Builder(
                _bookId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language
        ).build();

        //Assert
        assertNotNull(book);
        assertSame(_bookId, book.getId());
        assertSame(_publicationId, book.getPublication());
        assertSame(_companyId, book.getPublishingCompany());
        assertSame(_publishingYear, book.getPublishingYear());
        assertSame(_language, book.getEditionLanguage());
    }

    @Test
    void shouldBuildEditionBookWithOptionalFields() {
        //Act
        //SUT
        EditionBook book = new EditionBook.Builder(
                _bookId,
                _publicationId,
                _companyId,
                _publishingYear,
                _language
        )
                .withDimension(_dimension)
                .withWeight(_weight)
                .withNumberOfPages(_pages)
                .withEditionNumber(_editionNumber)
                .withBinding(_binding)
                .build();

        //Assert
        assertSame(_dimension, book.getDimension());
        assertSame(_weight, book.getWeight());
        assertSame(_pages, book.getNumberOfPages());
        assertSame(_editionNumber, book.getEditionNumber());
        assertSame(_binding, book.getBinding());
    }

    @Test
    void shouldThrowWhenBookIdIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new EditionBook.Builder(
                        null,
                        _publicationId,
                        _companyId,
                        _publishingYear,
                        _language
                ).build()
        );
    }

    @Test
    void shouldThrowWhenPublicationIdIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new EditionBook.Builder(
                        _bookId,
                        null,
                        _companyId,
                        _publishingYear,
                        _language
                ).build()
        );
    }

    @Test
    void shouldThrowWhenPublishingCompanyIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditionBook.Builder(
                        _bookId,
                        _publicationId,
                        null,
                        _publishingYear,
                        _language
                ).build()
        );
    }

    @Test
    void shouldThrowWhenPublishingYearIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new EditionBook.Builder(
                        _bookId,
                        _publicationId,
                        _companyId,
                        null,
                        _language
                ).build()
        );
    }

    @Test
    void shouldThrowWhenLanguageIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new EditionBook.Builder(
                        _bookId,
                        _publicationId,
                        _companyId,
                        _publishingYear,
                        null
                ).build()
        );
    }
}