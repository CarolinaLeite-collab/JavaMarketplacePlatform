package TOPSECRET.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberOfPagesTest {
    @Test
    void testNumberOfPages() {
        NumberOfPages num = new NumberOfPages(250);
        assertEquals(250, num.getNumberOfPages());
    }

    @Test
    void negative_numberOfPages() {
        int num = -1;
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NumberOfPages(num));
    }

    @Test
    void zero_numberOfPages() {
        int num = 0;
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NumberOfPages(num));
    }
}
