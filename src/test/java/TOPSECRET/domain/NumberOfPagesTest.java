package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberOfPagesTest {
    @Test
    void shouldSucceedForValidNumberOfPages(){
        //arrange
        int number = 67;
        NumberOfPages numberOfPages = new NumberOfPages(number);

        //act
        int pages = numberOfPages.getNumberOfPages();

        //assert
        assertEquals(number, pages);
    }

    @Test
    void shouldThrowExceptionForZeroNumberOfPages(){
        int number = 0;

        assertThrows(IllegalArgumentException.class, ()->{NumberOfPages pages = new NumberOfPages(number);});
    }

    @Test
    void shouldThrowExceptionForNegativeNumberOfPages(){
        int number = -56;

        assertThrows(IllegalArgumentException.class, ()->{NumberOfPages pages = new NumberOfPages(number);});
    }
}