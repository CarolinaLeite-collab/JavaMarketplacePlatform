package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MemoAuthorRepoTest {

    private AuthorFactory authorFactoryDouble;

    @BeforeEach
    void setUp() {

        authorFactoryDouble = mock(AuthorFactory.class);

    }

    @Test
    void addAuthorShouldStoreAuthorWithTrimmedName() {

        //SUT
        MemoAuthorRepo repo = new MemoAuthorRepo(authorFactoryDouble);

        //act
        Author author = repo.addAuthor(" Ana   ");
        List<Author> all = repo.findAll();

        //assert
        assertNotNull(author);
        assertEquals("Ana", author.getName());

        assertEquals(1, all.size());
        assertEquals("Ana", all.get(0).getName());
    }

    @Test
    void addAuthorShouldThrowWhenNameIsBlank() {

        //SUT
        MemoAuthorRepo repo = new MemoAuthorRepo(authorFactoryDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> repo.addAuthor(" "));
    }

    @Test
    void addAuthorShouldThrowWhenNameIsEmpty() {

        //SUT
        MemoAuthorRepo repo = new MemoAuthorRepo(authorFactoryDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> repo.addAuthor(""));
    }

    @Test
    void addAuthorShouldThrowWhenAuthorAlreadyExistsIgnoringCase() {

        //SUT
        MemoAuthorRepo repo = new MemoAuthorRepo(authorFactoryDouble);

        //act
        repo.addAuthor("Ana");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> repo.addAuthor("ana"));

        //assert
        assertEquals("Author already exists", ex.getMessage());
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void findAllShouldReturnDefensiveCopy() {

        //SUT
        MemoAuthorRepo repo = new MemoAuthorRepo(authorFactoryDouble);

        //act
        repo.addAuthor("A");
        List<Author> copy = repo.findAll();

        //Modifying the returned list cannot affect the repository
        copy.clear();

        //assert
        assertEquals(1, repo.findAll().size()
        );
    }

    @Test
    void existsByNameShouldReturnFalseOnEmptyRepo() {

        //SUT
        MemoAuthorRepo  repo = new MemoAuthorRepo(authorFactoryDouble);

        //act + assert
        assertFalse(repo.existsByName("Ana"));
    }

    @Test
    void existsByNameShouldReturnFalseWhenNoMatchExists() {

        //SUT
        MemoAuthorRepo repo = new MemoAuthorRepo(authorFactoryDouble);

        //act
        repo.addAuthor("Ana");

        //assert
        assertFalse(repo.existsByName("Bruno"));
    }

    @Test
    void existsByNameShouldReturnTrueWhenMatchExistsIgnoringCase() {

        //SUT
        MemoAuthorRepo repo = new MemoAuthorRepo(authorFactoryDouble);

        //act
        repo.addAuthor("Ana");

        //assert
        assertTrue(repo.existsByName("aNa"));
    }
}
