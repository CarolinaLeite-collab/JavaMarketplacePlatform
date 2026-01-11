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

    @Test
    void invalidDigitLong_FourteenDigits() {
        // arrange
        long number = 11019000000000L;
        // act and assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ISBN(number));
    }

    @Test
    void validISBNequals (){
        // arrange
        long number1 = 9781010101923L;
        long number2 = 9781010101923L;
        // act
        ISBN isbn1 = new ISBN(number1);
        ISBN isbn2 = new ISBN(number2);

        // assert
        Assertions.assertTrue(isbn1.hashCode()==isbn2.hashCode());
        Assertions.assertTrue(isbn1.equals(isbn2));
    }

    @Test
    void invalidISBNequals (){
        // arrange
        long number = 9781010101923L;
        // act
        ISBN isbn = new ISBN(number);

        // assert
        Assertions.assertFalse(isbn.equals(null));
    }
    @Test
    void invalidISBNequals_diferentClassObject (){
        // arrange
        long number = 9781010101923L;
        // act
        ISBN isbn = new ISBN(number);

        // assert
        Assertions.assertFalse(isbn.equals("hjgjhgj"));
    }
}
