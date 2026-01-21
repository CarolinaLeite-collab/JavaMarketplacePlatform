package TOPSECRET.controller;

import TOPSECRET.domain.PublicList;
import TOPSECRET.domain.PublicListRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicListsByGenreControllerTest {

    private PublicListRepo repo;
    private GetPublicListsByGenreController controller;

    @BeforeEach
    void setUp() {
        repo = new PublicListRepo();
        controller = new GetPublicListsByGenreController(repo);

        repo.add(new PublicList("List A", "user1", "Fiction", true, true));   // should appear
        repo.add(new PublicList("List B", "user2", "Fiction", true, false));  // not published
        repo.add(new PublicList("List C", "user3", "Fiction", false, true));  // not public
        repo.add(new PublicList("List D", "user4", "Horror", true, true));    // different genre
    }

    @Test
    void shouldReturnOnlyPublicAndPublishedListsOfGenre() {
        List<PublicList> result = controller.getPublicListsByGenre("Fiction");

        assertEquals(1, result.size());
        assertEquals("List A", result.get(0).getListName());
        assertEquals("user1", result.get(0).getOwnerUsername());
    }

    @Test
    void shouldTrimGenre() {
        List<PublicList> result = controller.getPublicListsByGenre("  Fiction  ");

        assertEquals(1, result.size());
        assertEquals("List A", result.get(0).getListName());
    }

    @Test
    void shouldIgnoreCaseInGenre() {
        List<PublicList> result = controller.getPublicListsByGenre("fiCtIoN");

        assertEquals(1, result.size());
        assertEquals("List A", result.get(0).getListName());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatches() {
        List<PublicList> result = controller.getPublicListsByGenre("Romance");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenGenreIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.getPublicListsByGenre(null)
        );
        assertEquals("Genre is mandatory", ex.getMessage());
    }

    @Test
    void shouldThrowWhenGenreIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(""));
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre("   "));
    }
}