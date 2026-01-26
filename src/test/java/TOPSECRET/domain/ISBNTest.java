package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static TOPSECRET.domain.ISBN.isValidIsbn10;
import static TOPSECRET.domain.ISBN.isValidIsbn13;
import static org.junit.jupiter.api.Assertions.*;

public class ISBNTest {
    //Constructor test
    //invalid Isbn10
    @Test
    void returnExceptionforInvalidIsbn10() {
        assertThrows(IllegalArgumentException.class, () -> new ISBN("123456789"));
    }

    //isValidIsbn10 tests
    @Test
    void returnTrueForValidIsbn10() {
        // arrange
        String isbn = "0306406152";
        // act
        boolean result = isValidIsbn10(isbn);
        // assert
        assertTrue(result);
    }

    @Test
    void returnTrueForValidIsbn10endingX() {
        // arrange
        String isbn = "838894522X";
        // act
        boolean result = isValidIsbn10(isbn);
        // assert
        assertTrue(result);
    }

    @Test
    void returnFalseForInvalidIsbn10() {
        // arrange
        String isbn = "03064061510";
        // act
        boolean result = isValidIsbn10(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForNullIsbn10() {
        // arrange
        String isbn = null;
        // act
        boolean result = isValidIsbn10(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForInvalidIsbn10ElevenDigits() {
        // arrange
        String isbn = "11111111111";
        // act
        boolean result = isValidIsbn10(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForInvalidIsbn10LettersDigits() {
        // arrange
        String isbn = "030640A152";
        // act
        boolean result = isValidIsbn10(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForInvalidIsbn10DigitEnd() {
        // arrange
        String isbn = "838894522A";
        // act
        boolean result = isValidIsbn10(isbn);
        // assert
        assertFalse(result);
    }

    //isValidIsbn13 tests
    @Test
    void returnTrueForValidIsbn13() {
        // arrange
        String isbn = "9789896710453";
        // act
        boolean result = isValidIsbn13(isbn);
        // assert
        assertTrue(result);
    }

    @Test
    void returnFalseForNullIsbn13() {
        // arrange
        String isbn = null;
        // act
        boolean result = isValidIsbn13(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForTooLongIsbn13() {
        // arrange
        String isbn = "97898967104536";
        // act
        boolean result = isValidIsbn13(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForTooShortIsbn13() {
        // arrange
        String isbn = "978989671045";
        // act
        boolean result = isValidIsbn13(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForIsbn13WithLetters() {
        // arrange
        String isbn = "97803A6406157";
        // act
        boolean result = isValidIsbn13(isbn);
        // assert
        assertFalse(result);
    }

    @Test
    void returnFalseForIsbn13WithWrongWeights() {
        // arrange
        String isbn = "278989671047";
        // act
        boolean result = isValidIsbn13(isbn);
        // assert
        assertFalse(result);
    }

    //test toIsbn13 method

    @Test
    void returnsSameIfAlreadyIsbn13() {
        String isbn13 = "9780136091813";

        String result = ISBN.toIsbn13(isbn13);

        assertEquals(isbn13, result);
    }
    @Test
    void isValidIsbn13_returnsFalse_whenCheckDigitIsWrong() {
        assertFalse(isValidIsbn13("9780306406158"));
    }

    @Test
    void isValidIsbn13_returnsFalse_whenNonDigitAppearsInFirst12() {
        assertFalse(ISBN.isValidIsbn13("97803A6406157"));
    }
    @Test
    void isValidIsbn13_returnsFalse_whenLastCharIsNonDigit() {
        assertFalse(ISBN.isValidIsbn13("978030640615X")); // last is non-digit
    }

    @ParameterizedTest
    @MethodSource("isbn10To13Samples")
    void toIsbn13_convertsCorrectly_forMultipleExamples(String isbn10, String expectedIsbn13) {
        assertEquals(expectedIsbn13, ISBN.toIsbn13(isbn10));
    }
    static Stream<Arguments> isbn10To13Samples() {
        return Stream.of(
                // These are standard well-known examples:
                Arguments.of("0306406152", "9780306406157"),
                Arguments.of("0134685997", "9780134685991"),
                Arguments.of("0596009208", "9780596009205"),
                Arguments.of("0618260307", "9780618260300")
        );
    }


    //test getIdentifier
    @Test
    void toIsbn13ThrowsWhenNotIsbn10AndNotIsbn13() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ISBN.toIsbn13("1234567890") // 10 chars, mas check digit inválido
        );

        assertEquals("Invalid ISBN", ex.getMessage());
    }

    @Test
    void constructor_normalizesHyphensSpacesAndLowercaseX() {
        ISBN isbn = new ISBN("978-06182-60300");
        assertEquals("9780618260300", isbn.getIdentifier());
    }

    @Test
    void returnsTrueForgetIdentifierIsbn() {
        ISBN isbn = new ISBN("9780618260300");
        String result = isbn.getIdentifier();
        assertEquals("9780618260300", result);
    }

    //test equals()
    @Test
    void returnsTrueForSameIsbn() {
        ISBN isbn = new ISBN("9780618260300");
        Object o = isbn;
        assertEquals(isbn, o);
    }

    @Test
    void equalsIsReflexive() {
        ISBN isbn = new ISBN("9780618260300");
        assertEquals(isbn, isbn);
    }

    @Test
    void isbn10AndIsbn13OfSameBookAreEqual() {
        ISBN isbn10 = new ISBN("0618260307");
        ISBN isbn13 = new ISBN("9780618260300");
        assertEquals(isbn10, isbn13);
        assertEquals(isbn13, isbn10);
    }

    @Test
    void equalsReturnsFalseFordifferentIsbns() {
        //arrange
        ISBN a = new ISBN("9780618260300");
        ISBN b = new ISBN("9780136091813");
        //act
        boolean result = a.equals(b);
        // assert
        assertFalse(result);
    }

    @Test
    void equalsReturnsFalseForDifferentType() {
        //arrange
        ISBN isbn = new ISBN("9780618260300");
        String notAnIsbn = "9780618260300";
        //act
        boolean result = isbn.equals(notAnIsbn);
        // assert
        assertFalse(result);
    }
    @Test
    void normalize_throwsExceptionForNullIsbnString(){
        assertThrows(IllegalArgumentException.class, () -> new ISBN(null));

    }

}
