package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicListTest {

    @Test
    void shouldExposeConstructorValuesThroughGetters() {
        PublicList list = new PublicList(
                "List A", "user1", "Fiction", true, true);

        assertAll(
                () -> assertEquals("List A", list.getListName()),
                () -> assertEquals("user1", list.getOwnerUsername()),
                () -> assertEquals("Fiction", list.getGenre()),
                () -> assertTrue(list.isPublic()),
                () -> assertTrue(list.isPublished())
        );
    }

    @Test
    void shouldSupportNonPublicOrNonPublishedFlags() {
        PublicList nonPublic = new PublicList("List B", "user2", "Fiction", false, true);
        PublicList nonPublished = new PublicList("List C", "user3", "Fiction", true, false);

        assertAll(
                () -> assertFalse(nonPublic.isPublic()),
                () -> assertTrue(nonPublic.isPublished()),
                () -> assertTrue(nonPublished.isPublic()),
                () -> assertFalse(nonPublished.isPublished())
        );
    }

    @Test
    void shouldAllowGenresWithDifferentCase() {
        PublicList list = new PublicList("List D", "user4", "fiCtIoN", true, true);

        assertEquals("fiCtIoN", list.getGenre());
    }

    @Test
    void shouldAllowSpacesInNameAndUsernameAsProvided() {
        // PublicList currently does not normalize/trim values; it stores exactly what it receives.
        PublicList list = new PublicList("  My List  ", "  user  ", "Fiction", true, true);

        assertAll(
                () -> assertEquals("  My List  ", list.getListName()),
                () -> assertEquals("  user  ", list.getOwnerUsername())
        );
    }
}