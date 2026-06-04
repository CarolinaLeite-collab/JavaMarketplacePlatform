package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @InjectMocks
    private AuthorService _authorService;

    @Mock
    private IAuthorRepo _iAuthorRepo;

    @Mock
    private AuthorFactory _authorFactory;

    @Test
    void registerAuthorReturnsRawDomainAuthor() {
        // Arrange
        String authorName = "Sample Name";
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_authorFactory.createAuthor(any(Name.class))).thenReturn(authorDouble);
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(_iAuthorRepo.containsOfIdentity(authorIdDouble)).thenReturn(false);
        when(_iAuthorRepo.save(authorDouble)).thenReturn(authorDouble);

        // Act
        Author result = _authorService.registerAuthor(authorName);

        // Assert
        assertNotNull(result);
        assertSame(authorDouble, result);
    }

    @Test
    void registerAuthorThrowsWhenDuplicate() {
        // Arrange
        String authorName = "Sample Name";
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_authorFactory.createAuthor(any(Name.class))).thenReturn(authorDouble);
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(_iAuthorRepo.containsOfIdentity(authorIdDouble)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> _authorService.registerAuthor(authorName));

        assertEquals("Author already exists in the repository", exception.getMessage());
    }

    @Test
    void getAllAuthorsReturnsRawDomainEntities() {
        // Arrange
        Author authorOne = mock(Author.class);
        Author authorTwo = mock(Author.class);
        List<Author> authorsList = List.of(authorOne, authorTwo);

        when(_iAuthorRepo.findAll()).thenReturn(authorsList);

        // Act
        Iterable<Author> result = _authorService.getAllAuthors();

        // Assert
        assertNotNull(result);
        assertSame(authorsList, result);
    }

    @Test
    void getAuthorByIdReturnsOptionalWithAuthorWhenFound() {
        // Arrange
        String idString = "SAMPLE-ID";
        Author authorDouble = mock(Author.class);

        when(_iAuthorRepo.ofIdentity(any(AuthorId.class))).thenReturn(Optional.of(authorDouble));

        // Act
        Optional<Author> result = _authorService.getAuthorById(idString);

        // Assert
        assertTrue(result.isPresent());
        assertSame(authorDouble, result.get());
    }

    @Test
    void getAuthorByIdReturnsEmptyOptionalWhenNotFound() {
        // Arrange
        String idString = "NON-EXISTENT";

        when(_iAuthorRepo.ofIdentity(any(AuthorId.class))).thenReturn(Optional.empty());

        // Act
        Optional<Author> result = _authorService.getAuthorById(idString);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuthorsIdReturnsAllAuthorIdsFromRepository() {
        // Arrange
        Author authorOne = mock(Author.class);
        Author authorTwo = mock(Author.class);
        AuthorId idOne = mock(AuthorId.class);
        AuthorId idTwo = mock(AuthorId.class);

        when(authorOne.identity()).thenReturn(idOne);
        when(authorTwo.identity()).thenReturn(idTwo);
        when(_iAuthorRepo.findAll()).thenReturn(List.of(authorOne, authorTwo));

        // Act
        Iterable<AuthorId> result = _authorService.getAuthorsId();

        // Assert
        assertNotNull(result);
        List<AuthorId> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(idOne));
        assertTrue(resultList.contains(idTwo));
    }
}