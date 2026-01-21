package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicListRepoTest {

    @Test
    void findPublicListPublishedByGenreShouldFilterCorrectly() {
        PublicListRepo repo = new PublicListRepo();

        repo.add(new PublicList("A", "u1", "Fiction", true, true));
        repo.add(new PublicList("B", "u2", "Fiction", true, false));
        repo.add(new PublicList("C", "u3", "Horror", true, true));

        List<PublicList> result = repo.findPublicListsPublishedByGenre("Fiction");

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getListName());
        assertEquals("u1", result.get(0).getOwnerUsername());
    }
}