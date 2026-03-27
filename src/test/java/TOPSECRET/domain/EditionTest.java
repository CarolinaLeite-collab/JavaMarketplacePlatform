
package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionTest {

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
        Edition edition = new Edition(isbnDouble, _numberOfPagesDouble, 1, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNotNull(edition.getIsbn());
        assertNull(edition.getIssn());
    }

    @Test
    void constructorShouldThrowWhenIsbnIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition((ISBN) null, _numberOfPagesDouble, 1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT

    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNullWithIsbn() {
        // Arrange
        ISBN isbnDouble = mock(ISBN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition(isbnDouble, _numberOfPagesDouble, null, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsZeroWithIsbn() {
        // Arrange
        ISBN isbnDouble = mock(ISBN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition(isbnDouble, _numberOfPagesDouble, 0, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNegativeWithIsbn() {
        // Arrange
        ISBN isbnDouble = mock(ISBN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition(isbnDouble, _numberOfPagesDouble, -1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldBuildEditionWithIssn() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act
        Edition edition = new Edition(issnDouble, _numberOfPagesDouble, 1, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNotNull(edition.getIssn());
        assertNull(edition.getIsbn());
    }

    @Test
    void constructorShouldThrowWhenIssnIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition((ISSN) null, _numberOfPagesDouble, 1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsZeroWithIssn() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition(issnDouble, _numberOfPagesDouble, 0, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNegativeWithIssn() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition(issnDouble, _numberOfPagesDouble, -1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldBuildEditionWithoutIdentifier() {
        // Act
        Edition edition = new Edition(_numberOfPagesDouble, null, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNull(edition.getIsbn());
        assertNull(edition.getIssn());
        assertNull(edition.getEditionNumber());
    }

    @Test
    void constructorShouldBuildEditionWithoutIdentifierWithEditionNumber() {
        // Act
        Edition edition = new Edition(_numberOfPagesDouble, 1, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertNull(edition.getIsbn());
        assertNull(edition.getIssn());
        assertEquals(1, edition.getEditionNumber());
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsNegativeWithoutIdentifier() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition(_numberOfPagesDouble, -1, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void constructorShouldThrowWhenEditionNumberIsZeroWithoutIdentifier() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Edition(_numberOfPagesDouble, 0, _publicationDate,
                        _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble)); // SUT
    }

    @Test
    void gettersShouldReturnCorrectValues() {
        // Arrange
        ISSN issnDouble = mock(ISSN.class);

        // Act
        Edition edition = new Edition(issnDouble, _numberOfPagesDouble, 3, _publicationDate,
                _binding, _descriptionDouble, _dimensionDouble, _weightDouble, _languageDouble); // SUT

        // Assert
        assertEquals(3, edition.getEditionNumber());
        assertEquals(_numberOfPagesDouble, edition.getNumberOfPages());
        assertEquals(_publicationDate, edition.getPublicationDate());
        assertEquals(_binding, edition.getBinding());
        assertEquals(_descriptionDouble, edition.getDescription());
        assertEquals(_dimensionDouble, edition.getDimension());
        assertEquals(_weightDouble, edition.getWeight());
        assertEquals(_languageDouble, edition.getLanguage());
    }
}



