package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorRepoTest {

    @Test
    void createShouldStoreAuthorWithTrimmedName() {
        AuthorRepo repo = new AuthorRepo();

        Author a = repo.create(" Ana   ");

        assertNotNull(a);
        assertEquals("Ana", a.getName());

        List<Author> all = repo.findAll();
        assertEquals(1, all.size());
        assertEquals("Ana", all.get(0).getName());
    }

    @Test
    void createShouldThrowWhenNameIsNull() {
        AuthorRepo repo = new AuthorRepo();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> repo.create(null));
        assertEquals("Author name is mandatory", ex.getMessage());
    }

    @Test
    void createShouldThrowWhenNameIsBlank() {
        AuthorRepo repo = new AuthorRepo();

        assertThrows(IllegalArgumentException.class, () -> repo.create(" "));
    }

    @Test
    void createShouldThrowWhenNameIsEmpty() {
        AuthorRepo repo = new AuthorRepo();

        assertThrows(IllegalArgumentException.class, () -> repo.create(""));
    }

    @Test
    void createShouldThrowWhenAuthorAlreadyExistsIgnoringCase() {
        AuthorRepo repo = new AuthorRepo();

        repo.create("Ana");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> repo.create("ana"));
        assertEquals("Author already exists", ex.getMessage());
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void findAllShouldReturnDefensiveCopy() {
        AuthorRepo repo = new AuthorRepo();

        repo.create("A");
        List<Author> copy = repo.findAll();

        //Modifying the returned list cannot affect the repository
        copy.clear();

        assertEquals(1, repo.findAll().size()
        );
    }


}
