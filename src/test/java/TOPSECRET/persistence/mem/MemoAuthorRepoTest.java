package TOPSECRET.persistence.mem;

import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.author.AuthorFactory;
import TOPSECRET.domain.valueobject.AuthorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemoAuthorRepoTest {

    private AuthorFactory _authorFactoryDouble;
    private Author _authorDouble;
    private AuthorId _authorIdDouble;
    private String _authorName;

    @BeforeEach
    void setUp() {
        _authorFactoryDouble = mock(AuthorFactory.class);

        _authorName = "Seneca";
        _authorDouble = mock(Author.class);
        _authorIdDouble = mock(AuthorId.class);

        when(_authorDouble.identity()).thenReturn(_authorIdDouble);
        when(_authorFactoryDouble.createAuthor(_authorName)).thenReturn(_authorDouble);

    }


    @Test
    void testConstructor() {

        // SUT
        MemoAuthorRepo authorRepo = new MemoAuthorRepo(_authorFactoryDouble);

    }

    @Test
    void saveShouldStoreAuthorAndReturnIt() {

        // Arrange & SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);
        
        // Act
        Author result = memoAuthorRepo.save(_authorDouble);

        // Assert
        assertEquals(_authorDouble, result);
        assertTrue(memoAuthorRepo.containsOfIdentity(_authorIdDouble));

    }

    @Test
    void saveShouldOverwriteExistingAuthorWithSameId() {

        // Arrange
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);
        memoAuthorRepo.save(_authorDouble);

        Author anotherAuthorWithSameId = mock(Author.class);
        when(anotherAuthorWithSameId.identity()).thenReturn(_authorIdDouble);

        // Act
        memoAuthorRepo.save(anotherAuthorWithSameId);

        // Assert
        assertEquals(anotherAuthorWithSameId, memoAuthorRepo.ofIdentity(_authorIdDouble).get());

    }

    @Test
    void findAllShouldReturnAllSavedAuthors() {

        // Arrange
        Author author2 = mock(Author.class);
        AuthorId author2Id = mock(AuthorId.class);
        when(author2.identity()).thenReturn(author2Id);

        //SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);

        memoAuthorRepo.save(_authorDouble);
        memoAuthorRepo.save(author2);

        // Act
        Iterable<Author> all = memoAuthorRepo.findAll();

        // Assert
        assertTrue(((Collection<Author>) all).contains(_authorDouble));
        assertTrue(((Collection<Author>) all).contains(author2));

    }

    @Test
    void findAllShouldReturnEmptyIfNoAuthorsSaved() {

        // Arrange & SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);

        // Act
        Iterable<Author> allAuthors = memoAuthorRepo.findAll();

        // Assert
        assertFalse(allAuthors.iterator().hasNext());

    }

    @Test
    void containsOfIdentityShouldReturnTrueIfAuthorExists() {

        // Arrange & SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);
        memoAuthorRepo.save(_authorDouble);

        // Act
        boolean exists = memoAuthorRepo.containsOfIdentity(_authorIdDouble);

        // Assert
        assertTrue(exists);

    }

    @Test
    void containsOfIdentityShouldReturnFalseIfAuthorDoesNotExist() {

        //Arrange & SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);

        // Act
        boolean exists = memoAuthorRepo.containsOfIdentity(_authorIdDouble);

        // Assert
        assertFalse(exists);

    }

    @Test
    void ofIdentityShouldReturnAuthorIfExists() {

        // Arrange
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);
        memoAuthorRepo.save(_authorDouble);

        // Act
        Optional<Author> result = memoAuthorRepo.ofIdentity(_authorIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_authorDouble, result.get());

    }

    @Test
    void ofIdentityShouldReturnEmptyIfAuthorDoesNotExist() {

        // Arrange & SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);

        // Act
        Optional<Author> result = memoAuthorRepo.ofIdentity(_authorIdDouble);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void addAuthorShouldCreateAndSaveAuthor() {

        // Arrange & SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);

        // Act
        Author result = memoAuthorRepo.addAuthor(_authorName);

        // Assert
        assertEquals(_authorDouble, result);
        assertTrue(memoAuthorRepo.containsOfIdentity(_authorIdDouble));

    }

    @Test
    void addAuthorShouldCallAuthorFactoryWithCorrectName() {

        // Arrange & SUT
        MemoAuthorRepo memoAuthorRepo = new MemoAuthorRepo(_authorFactoryDouble);

        // Act
        memoAuthorRepo.addAuthor(_authorName);

        // Assert
        verify(_authorFactoryDouble).createAuthor(_authorName);

    }

}