package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRatingTest {

    @Test
    void tests_whether_constants_exist() {
        assertNotNull(UserRating.ONE_STAR);
        assertNotNull(UserRating.TWO_STARS);
        assertNotNull(UserRating.THREE_STARS);
        assertNotNull(UserRating.FOUR_STARS);
        assertNotNull(UserRating.FIVE_STARS);
    }

    @Test
    void tests_whether_stars_are_correcty_returned() {
        assertEquals("★", UserRating.ONE_STAR.toString());
        assertEquals("★★", UserRating.TWO_STARS.toString());
        assertEquals("★★★", UserRating.THREE_STARS.toString());
        assertEquals("★★★★", UserRating.FOUR_STARS.toString());
        assertEquals("★★★★★", UserRating.FIVE_STARS.toString());
    }

    @Test
    void tests_whether_ratings_are_accessible_via_values() {
        UserRating[] ratings = UserRating.values();

        assertEquals(5, ratings.length);
        assertArrayEquals(new UserRating[]{
                UserRating.ONE_STAR, UserRating.TWO_STARS,
                UserRating.THREE_STARS, UserRating.FOUR_STARS,
                UserRating.FIVE_STARS
        }, ratings);
    }

    @Test
    void tests_whether_valueOf_works() {
        assertEquals(UserRating.THREE_STARS, UserRating.valueOf("THREE_STARS"));
    }

    @Test
    void tests_whether_different_ratings_are_not_equal() {
        assertNotEquals(UserRating.ONE_STAR, UserRating.FIVE_STARS);
    }
}
