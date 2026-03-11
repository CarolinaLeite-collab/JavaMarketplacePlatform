package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberOfPagesTest {

    @Test
    void shouldSucceedForValidNumberOfPages(){
        //arrange
        int number = 67;
        NumberOfPages numberOfPages = new NumberOfPages(number);

        NumberOfPages numberOfPages2 = new NumberOfPages(number);

        //act
        int pages = numberOfPages.getNumberOfPages();

        //assert
        assertNotNull(numberOfPages);
        assertEquals(number, pages);
        assertEquals(numberOfPages, numberOfPages2);
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