package MITELOVERS.domain.valueobject;

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

    @Test
    void equalsReturnsTrueForSameInstance() {
        NumberOfPages n1 = new NumberOfPages(50);

        assertEquals(n1, n1);
    }

    @Test
    void equalsReturnsFalseForDifferentType() {
        NumberOfPages n1 = new NumberOfPages(50);

        assertNotEquals(n1, "50");
    }

    @Test
    void equalsReturnsFalseForDifferentValues() {
        NumberOfPages n1 = new NumberOfPages(50);
        NumberOfPages n2 = new NumberOfPages(100);

        assertNotEquals(n1, n2);
    }

    @Test
    void hashCodeSameForEqualObjects() {
        NumberOfPages n1 = new NumberOfPages(100);
        NumberOfPages n2 = new NumberOfPages(100);

        assertEquals(n1.hashCode(), n2.hashCode());
    }

    @Test
    void hashCodeDifferentForDifferentObjects() {
        NumberOfPages n1 = new NumberOfPages(100);
        NumberOfPages n2 = new NumberOfPages(200);

        assertNotEquals(n1.hashCode(), n2.hashCode());
    }


}
