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

        //Title can be anything, but has to exist/cannot be null

        //arrange
        String nullTitle = null;
        String expectedMessage = "Title cannot be null, empty, or blank";

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                new Title(nullTitle)
        );

    }

    @Test
    void invalidEmptyTitle() {

        //Title can be anything, but has to exist/cannot be empty

        //arrange
        String emptyTitle = "";
        String expectedMessage = "Title cannot be null, empty, or blank";

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                new Title(emptyTitle)
        );


    }

    @Test
    void invalidBlankTitle() {

        //Title can be anything, but has to exist/cannot be blank

        //arrange
        String blankTitle = " ";
        String expectedMessage = "Title cannot be null, empty, or blank";

        //act and assert
        assertThrows(IllegalArgumentException.class, () ->
                new Title(blankTitle)
        );

    }

    @Test
    void titleWithSameName() {
        Title title = new Title("My Title");
        Title title1 = new Title("My Title");

        assertEquals(title, title1);
    }

    @Test
    void titleWithSameNameHaveSameHashCode() {
        Title title = new Title("My Title");
        Title title1 = new Title("MY TITLE");

        assertEquals(title.hashCode(), title1.hashCode());
    }

    @Test
    void titleEqualsItself() {
        Title title = new Title("My Title");

        assertEquals(title, title);
    }

    @Test
    void titleNotEqualsToNull() {
        Title title = new Title("My Title");

        assertNotEquals(null, title);
    }

    @Test
    void titleNotEqualsToDifferentType() {
        Title title = new Title("My Title");

        assertNotEquals("My Title", title);
    }

    @Test
    void titleWithDifferentNameAreNotEqual() {
        Title title = new Title("My Title");
        Title title1 = new Title("My First Title");

        assertNotEquals(title, title1);
        assertNotEquals(title.hashCode(), title1.hashCode());
    }

    @Test
    void titleEqualsIgnoresCase() {
        Title title = new Title("My Title");
        Title title1 = new Title("My TITLE");

        assertEquals(title, title1);
        assertEquals(title.hashCode(), title1.hashCode());
    }

}