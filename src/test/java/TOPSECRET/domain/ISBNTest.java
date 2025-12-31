package TOPSECRET.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ISBNTest {

    @Test
    void validTenDigitLong() {
        // arrange
        long number = 1010101923L;
        // act
        ISBN isbn = new ISBN(number);
        // assert
        Assertions.assertTrue(isbn.isSameISBN(number));
    }

    @Test
    void validThirteenDigitLong() {
        // arrange
        long number = 9781010101923L;
        // act
        ISBN isbn = new ISBN(number);
        // assert
        Assertions.assertTrue(isbn.isSameISBN(number));
    }

    @Test
    void invalidDigitLong_Negative() {
        // arrange
        long number = -10101019L;
        // act and assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ISBN(number));
    }

    @Test
    void invalidDigitLong_FiveDigits() {
        // arrange
        long number = 11019L;
        // act and assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ISBN(number));
    }
}
