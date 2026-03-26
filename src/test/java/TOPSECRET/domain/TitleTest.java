package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleTest {

    @Test
    void validTitle() {

        //arrange
        String myTitle = "My Title";

        //act
        Title title = new Title(myTitle);

        //assert
        assertEquals(myTitle, title.getTitle());

    }

    @Test
    void validTrimWhitespace() {

        //arrange
        String myTitle = "  My Title ";

        //act
        Title title = new Title(myTitle);

        //assert
        assertEquals("My Title", title.getTitle());

    }

    @Test
    void validMixCapitalizationTitle() {

        //arrange
        String mixCapitalizedTitle = "The cat in the Hat";

        //act
        Title title = new Title(mixCapitalizedTitle);

        //assert
        assertEquals("the cat in the hat", title.getLowercaseTitle());

    }

    @Test
    void validMixCapitalizationAndWhitespace() {

        //arrange
        String mixCapitalizedWhitespaceTitle = " pRide and prejUdice     ";

        //act
        Title title = new Title(mixCapitalizedWhitespaceTitle);

        //assert
        assertEquals(title.getLowercaseTitle(), "pride and prejudice");

    }

    @Test
    void invalidNullTitle() {

        //arrange
        String nullTitle = null;
        String expectedMessage = "Title cannot be null, empty, or blank";

        //act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Title(nullTitle));

        //assert
        assertEquals(expectedMessage, exception.getMessage());

    }

    @Test
    void invalidEmptyTitle() {

        //arrange
        String emptyTitle = "";
        String expectedMessage = "Title cannot be null, empty, or blank";

        //act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Title(emptyTitle));

        //assert
        assertEquals(expectedMessage, exception.getMessage());

    }

    @Test
    void invalidBlankTitle() {

        //arrange
        String blankTitle = " ";
        String expectedMessage = "Title cannot be null, empty, or blank";

        //act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Title(blankTitle));

        //assert
        assertEquals(expectedMessage, exception.getMessage());

    }

    @Test
    void titleWithSameName() {

        //arrange
        Title title = new Title("My Title");
        Title title1 = new Title("My Title");

        //act
        boolean result = title.equals(title1);

        //assert
        assertTrue(result);

    }

    @Test
    void titleWithSameNameHaveSameHashCode() {

        //arrange
        Title title = new Title("My Title");
        Title title1 = new Title("MY TITLE");

        //act
        boolean result = title.equals(title1);

        //assert
        assertTrue(result);

    }

    @Test
    void titleEqualsItself() {

        //arrange
        Title title = new Title("My Title");

        //act
        boolean result = title.equals(title);

        //act & assert
        assertTrue(result);

    }

    @Test
    void titleNotEqualsToNull() {

        //arrange
        Title title = new Title("My Title");

        //act
        boolean result = title.equals(null);

        //act & assert
        assertFalse(result);

    }

    @Test
    void equalsReturnsFalseWhenObjectIsNotTitle() {

        //arrange
        Title title = new Title("Book");

        //act
        boolean result = title.equals("Not a Title");

        //assert
        assertFalse(result);

    }

    @Test
    void titleNotEqualsToDifferentType() {

        //arrange
        Title title = new Title("My Title");

        //act
        boolean result = title.equals("Not a Title");

        //assert
        assertNotEquals("My Title", title);

    }

    @Test
    void titleWithDifferentNameAreNotEqual() {

        //arrange
        Title title = new Title("My Title");
        Title title1 = new Title("My First Title");

        //act
        boolean result = title.equals(title1);
        boolean result1 = title.hashCode() == title1.hashCode();

        //assert
        assertFalse(result);
        assertFalse(result1);

    }

    @Test
    void titleEqualsIgnoresCase() {

        //arrange
        Title title = new Title("My Title");
        Title title1 = new Title("My TITLE");

        //act
        boolean result = title.equals(title1);
        boolean result1 = title.hashCode() == title1.hashCode();

        //assert
        assertTrue(result);
        assertTrue(result1);

    }

}