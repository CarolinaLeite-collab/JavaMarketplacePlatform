package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleTest {

    @Test
    void validTitle() throws InstantiationException {

        //arrange
        String myTitle = "My Title";

        //act
        Title title = new Title(myTitle);

        //assert
        assertEquals(title.getTitle(), myTitle);

    }

    @Test
    void validTrimWhitespace() throws InstantiationException {

        //arrange
        String myTitle = "  My Title ";

        //act
        Title title = new Title(myTitle);

        //assert
        assertEquals(title.getTitle(), "My Title");

    }

    @Test
    void validMixCapitalizationTitle() throws InstantiationException {

        //arrange
        String mixCapitalizedTitle = "The cat in the Hat";

        //act
        Title title = new Title(mixCapitalizedTitle);

        //assert
        assertEquals(title.getLowercaseTitle(), "the cat in the hat");

    }

    @Test
    void validMixCapitalizationAndWhitespace() throws InstantiationException {

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

}