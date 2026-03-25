package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
/**
class MemoAuthorRepoTest {
    private AuthorFactory factory;
    private MemoAuthorRepo repo;

    @Test
    void createAuthorShouldStoreAuthorWithTrimmedName() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act
        Author author = repo.createAuthor(" Ana   ");
        List<Author> all = repo.findAll();

        //assert
        assertNotNull(author);
        assertEquals("Ana", author.getName());

        assertEquals(1, all.size());
        assertEquals("Ana", all.get(0).getName());
    }

    @Test
    void createAuthorShouldThrowWhenNameIsNull() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> repo.createAuthor(null));

        //assert
        assertEquals("Author name is mandatory", ex.getMessage());
    }

    @Test
    void createAuthorShouldThrowWhenNameIsBlank() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> repo.createAuthor(" "));
    }

    @Test
    void createAuthorShouldThrowWhenNameIsEmpty() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> repo.createAuthor(""));
    }

    @Test
    void createAuthorShouldThrowWhenAuthorAlreadyExistsIgnoringCase() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act
        repo.createAuthor("Ana");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> repo.createAuthor("ana"));

        //assert
        assertEquals("Author already exists", ex.getMessage());
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void findAllShouldReturnDefensiveCopy() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act
        repo.createAuthor("A");
        List<Author> copy = repo.findAll();

        //Modifying the returned list cannot affect the repository
        copy.clear();

        //assert
        assertEquals(1, repo.findAll().size()
        );
    }

    @Test
    void existsByNameShouldReturnFalseOnEmptyRepo() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act + assert
        assertFalse(repo.existsByName("Ana"));
    }

    @Test
    void existsByNameShouldReturnFalseWhenNoMatchExists() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act
        repo.createAuthor("Ana");

        //assert
        assertFalse(repo.existsByName("Bruno"));
    }

    @Test
    void existsByNameShouldReturnTrueWhenMatchExistsIgnoringCase() {
        //arrange
        factory = mock(AuthorFactory.class);

        //SUT
        repo = new MemoAuthorRepo(factory);

        //act
        repo.createAuthor("Ana");

        //assert
        assertTrue(repo.existsByName("aNa"));
    }
}
 */
