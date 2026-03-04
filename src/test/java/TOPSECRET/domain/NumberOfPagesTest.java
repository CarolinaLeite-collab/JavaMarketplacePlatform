package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberOfPagesTest {
    @Test
    void should_throw_exception_for_zero_number_of_pages(){
        int number = 0;

        assertThrows(IllegalArgumentException.class, ()->{NumberOfPages pages = new NumberOfPages(number);});
    }

    @Test
    void should_throw_exception_for_negative_number_of_pages(){
        int number = -56;

        assertThrows(IllegalArgumentException.class, ()->{NumberOfPages pages = new NumberOfPages(number);});
    }

    @Test
    void should_succeed_for_valid_number_of_pages(){
        //arrange
        int number = 67;
        NumberOfPages numberOfPages = new NumberOfPages(number);

        //act
        int pages = numberOfPages.getNumberOfPages();

        //assert
        assertEquals(67, pages);
    }

}