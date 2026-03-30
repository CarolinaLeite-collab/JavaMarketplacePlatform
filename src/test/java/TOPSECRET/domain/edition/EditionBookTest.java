
package TOPSECRET.domain.edition;

import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionBookTest {

    private final NumberOfPages _numberOfPagesDouble = mock(NumberOfPages.class);
    private final LocalDate _publicationDate = LocalDate.of(2001, 4, 23);
    private final Binding _binding = Binding.SADDLE_STITCH;
    private final Description _descriptionDouble = mock(Description.class);
    private final Dimension _dimensionDouble = mock(Dimension.class);
    private final Weight _weightDouble = mock(Weight.class);
    private final Language _languageDouble = mock(Language.class);


    @Test
    void constructorShouldBuildEditionWithIsbn() {

        // Arrange
        ISBN isbnDouble = mock(ISBN.class);

        // Act
        EditionBook editionBook = new EditionBook(isbnDouble, _numberOfPagesDouble, 1, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNotNull(editionBook.getIsbn());
        assertNull(editionBook.getIssn());
    }

    @Test
    void constructorShouldThrowWhenIsbnIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook((ISBN) null, _numberOfPagesDouble, 1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT

    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNullWithIsbn() {
        // Arrange
        ISBN isbnDouble = mock(ISBN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook(isbnDouble, _numberOfPagesDouble, null, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsZeroWithIsbn() {
        // Arrange
        ISBN isbnDouble = mock(ISBN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook(isbnDouble, _numberOfPagesDouble, 0, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNegativeWithIsbn() {
        // Arrange
        ISBN isbnDouble = mock(ISBN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook(isbnDouble, _numberOfPagesDouble, -1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldBuildEditionWithIssn() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act
        EditionBook editionBook = new EditionBook(issnDouble, _numberOfPagesDouble, 1, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNotNull(editionBook.getIssn());
        assertNull(editionBook.getIsbn());
    }

    @Test
    void constructorShouldThrowWhenIssnIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook((ISSN) null, _numberOfPagesDouble, 1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsZeroWithIssn() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook(issnDouble, _numberOfPagesDouble, 0, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNegativeWithIssn() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook(issnDouble, _numberOfPagesDouble, -1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldBuildEditionWithoutIdentifier() {
        // Act
        EditionBook editionBook = new EditionBook(_numberOfPagesDouble, null, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNull(editionBook.getIsbn());
        assertNull(editionBook.getIssn());
        assertNull(editionBook.getEditionNumber());
    }

    @Test
    void constructorShouldBuildEditionWithoutIdentifierWithEditionNumber() {
        // Act
        EditionBook editionBook = new EditionBook(_numberOfPagesDouble, 1, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNull(editionBook.getIsbn());
        assertNull(editionBook.getIssn());
        assertEquals(1, editionBook.getEditionNumber());
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNegativeWithoutIdentifier() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook(_numberOfPagesDouble, -1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsZeroWithoutIdentifier() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new EditionBook(_numberOfPagesDouble, 0, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void gettersShouldReturnCorrectValues() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act
        EditionBook editionBook = new EditionBook(issnDouble, _numberOfPagesDouble, 3, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertEquals(3, editionBook.getEditionNumber());
        assertEquals(_numberOfPagesDouble, editionBook.getNumberOfPages());
        assertEquals(_publicationDate, editionBook.getPublicationDate());
        assertEquals(_binding, editionBook.getBinding());
        assertEquals(_descriptionDouble, editionBook.getDescription());
        assertEquals(_dimensionDouble, editionBook.getDimension());
        assertEquals(_weightDouble, editionBook.getWeight());
        assertEquals(_languageDouble, editionBook.getLanguage());
    }
}



