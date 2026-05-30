package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.dto.AuthorResponseDTO;
import MITELOVERS.mapper.AuthorResponseDTOMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    @Mock
    private AuthorResponseDTOMapper _authorResponseDTOMapper;

    @Test
    void registerAuthorReturnsResponseDTO() {
        // Arrange
        String authorName = "Sample Name";
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        AuthorResponseDTO responseDTODouble = mock(AuthorResponseDTO.class);

        when(_authorFactory.createAuthor(any(Name.class))).thenReturn(authorDouble);
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(_iAuthorRepo.containsOfIdentity(authorIdDouble)).thenReturn(false);
        when(_iAuthorRepo.save(authorDouble)).thenReturn(authorDouble);
        when(_authorResponseDTOMapper.toModel(authorDouble)).thenReturn(responseDTODouble);

        // Act
        AuthorResponseDTO result = _authorService.registerAuthor(authorName);

        // Assert
        assertSame(responseDTODouble, result);
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
    void getAllAuthorsReturnsMappedList() {
        // Arrange
        Author authorOne = mock(Author.class);
        Author authorTwo = mock(Author.class);
        AuthorResponseDTO dtoOne = mock(AuthorResponseDTO.class);
        AuthorResponseDTO dtoTwo = mock(AuthorResponseDTO.class);

        when(_iAuthorRepo.findAll()).thenReturn(List.of(authorOne, authorTwo));
        when(_authorResponseDTOMapper.toModel(authorOne)).thenReturn(dtoOne);
        when(_authorResponseDTOMapper.toModel(authorTwo)).thenReturn(dtoTwo);

        // Act
        List<AuthorResponseDTO> result = _authorService.getAllAuthors();

        // Assert
        assertEquals(2, result.size());
        assertSame(dtoOne, result.get(0));
        assertSame(dtoTwo, result.get(1));
    }

    @Test
    void getAuthorsIdReturnsAllAuthorIdsFromRepository() {
        // Arrange
        AuthorId authorIdOne = mock(AuthorId.class);
        AuthorId authorIdTwo = mock(AuthorId.class);
        List<AuthorId> authorIds = List.of(authorIdOne, authorIdTwo);

        when(_iAuthorRepo.findAllKeys()).thenReturn(authorIds);

        // Act
        Iterable<AuthorId> result = _authorService.getAuthorsId();

        // Assert
        assertSame(authorIds, result);
    }
}
