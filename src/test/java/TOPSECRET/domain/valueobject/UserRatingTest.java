package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRatingTest {

    @Test
    void testsWhetherConstantsExist() {
        assertNotNull(UserRating.ONE_STAR);
        assertNotNull(UserRating.TWO_STARS);
        assertNotNull(UserRating.THREE_STARS);
        assertNotNull(UserRating.FOUR_STARS);
        assertNotNull(UserRating.FIVE_STARS);
    }

    @Test
    void testsWhetherStarsAreCorrectlyReturned() {
        assertEquals("★", UserRating.ONE_STAR.toString());
        assertEquals("★★", UserRating.TWO_STARS.toString());
        assertEquals("★★★", UserRating.THREE_STARS.toString());
        assertEquals("★★★★", UserRating.FOUR_STARS.toString());
        assertEquals("★★★★★", UserRating.FIVE_STARS.toString());
    }

    @Test
    void testsWhetherRatingsAreAccessibleViaValues() {
        UserRating[] ratings = UserRating.values();

        assertEquals(5, ratings.length);
        assertArrayEquals(new UserRating[]{
                UserRating.ONE_STAR, UserRating.TWO_STARS,
                UserRating.THREE_STARS, UserRating.FOUR_STARS,
                UserRating.FIVE_STARS
        }, ratings);
    }

    @Test
    void testsWhetherValueOfWorks() {
        assertEquals(UserRating.THREE_STARS, UserRating.valueOf("THREE_STARS"));
    }

    @Test
    void testsWhetherDifferentRatingsAreNotEqual() {
        assertNotEquals(UserRating.ONE_STAR, UserRating.FIVE_STARS);
    }
}
