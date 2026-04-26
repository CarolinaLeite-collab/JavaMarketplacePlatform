package MITELOVERS.persistence;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemAuthorRepoTest {

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
        MemAuthorRepo authorRepo = new MemAuthorRepo(_authorFactoryDouble);

    }

    @Test
    void saveShouldStoreAuthorAndReturnIt() {

        // Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);
        
        // Act
        Author result = memAuthorRepo.save(_authorDouble);

        // Assert
        assertEquals(_authorDouble, result);
        assertTrue(memAuthorRepo.containsOfIdentity(_authorIdDouble));

    }

    @Test
    void saveShouldOverwriteExistingAuthorWithSameId() {

        // Arrange
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);
        memAuthorRepo.save(_authorDouble);

        Author anotherAuthorWithSameId = mock(Author.class);
        when(anotherAuthorWithSameId.identity()).thenReturn(_authorIdDouble);

        // Act
        memAuthorRepo.save(anotherAuthorWithSameId);

        // Assert
        assertEquals(anotherAuthorWithSameId, memAuthorRepo.ofIdentity(_authorIdDouble).get());

    }

    @Test
    void findAllShouldReturnAllSavedAuthors() {

        // Arrange
        Author author2 = mock(Author.class);
        AuthorId author2Id = mock(AuthorId.class);
        when(author2.identity()).thenReturn(author2Id);

        //SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        memAuthorRepo.save(_authorDouble);
        memAuthorRepo.save(author2);

        // Act
        Iterable<Author> all = memAuthorRepo.findAll();

        // Assert
        assertTrue(((Collection<Author>) all).contains(_authorDouble));
        assertTrue(((Collection<Author>) all).contains(author2));

    }

    @Test
    void findAllShouldReturnEmptyIfNoAuthorsSaved() {

        // Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        // Act
        Iterable<Author> allAuthors = memAuthorRepo.findAll();

        // Assert
        assertFalse(allAuthors.iterator().hasNext());

    }

    @Test
    void containsOfIdentityShouldReturnTrueIfAuthorExists() {

        // Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);
        memAuthorRepo.save(_authorDouble);

        // Act
        boolean exists = memAuthorRepo.containsOfIdentity(_authorIdDouble);

        // Assert
        assertTrue(exists);

    }

    @Test
    void containsOfIdentityShouldReturnFalseIfAuthorDoesNotExist() {

        //Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        // Act
        boolean exists = memAuthorRepo.containsOfIdentity(_authorIdDouble);

        // Assert
        assertFalse(exists);

    }

    @Test
    void ofIdentityShouldReturnAuthorIfExists() {

        // Arrange
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);
        memAuthorRepo.save(_authorDouble);

        // Act
        Optional<Author> result = memAuthorRepo.ofIdentity(_authorIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_authorDouble, result.get());

    }

    @Test
    void ofIdentityShouldReturnEmptyIfAuthorDoesNotExist() {

        // Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        // Act
        Optional<Author> result = memAuthorRepo.ofIdentity(_authorIdDouble);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void addAuthorShouldCreateAndSaveAuthor() {

        // Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        // Act
        Author result = memAuthorRepo.addAuthor(_authorName);

        // Assert
        assertEquals(_authorDouble, result);
        assertTrue(memAuthorRepo.containsOfIdentity(_authorIdDouble));

    }

    @Test
    void addAuthorShouldCallAuthorFactoryWithCorrectName() {

        // Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        // Act
        memAuthorRepo.addAuthor(_authorName);

        // Assert
        verify(_authorFactoryDouble).createAuthor(_authorName);

    }

    @Test
    void findAllKeysShouldReturnEmptyWhenRepoIsEmpty() {

        // Arrange & SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        // Act
        List<AuthorId> keys = memAuthorRepo.findAllKeys();

        // Assert
        assertTrue(keys.isEmpty());

    }

    @Test
    void findAllKeysShouldReturnAllStoredKeys() {

        // Arrange
        Author author2 = mock(Author.class);
        AuthorId author2Id = mock(AuthorId.class);

        when(_authorDouble.identity()).thenReturn(_authorIdDouble);
        when(author2.identity()).thenReturn(author2Id);

        // SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);

        memAuthorRepo.save(_authorDouble);
        memAuthorRepo.save(author2);

        // Act
        List<AuthorId> keys = memAuthorRepo.findAllKeys();

        // Assert
        assertEquals(2, keys.size());
        assertTrue(keys.contains(_authorIdDouble));
        assertTrue(keys.contains(author2Id));

    }

    @Test
    void findAllKeysShouldReturnCopyNotAffectingRepo() {

        // Arrange
        when(_authorDouble.identity()).thenReturn(_authorIdDouble);

        // SUT
        MemAuthorRepo memAuthorRepo = new MemAuthorRepo(_authorFactoryDouble);
        memAuthorRepo.save(_authorDouble);

        // Act
        List<AuthorId> keys = memAuthorRepo.findAllKeys();
        keys.clear();

        // Assert
        assertTrue(memAuthorRepo.containsOfIdentity(_authorIdDouble));

    }

}
