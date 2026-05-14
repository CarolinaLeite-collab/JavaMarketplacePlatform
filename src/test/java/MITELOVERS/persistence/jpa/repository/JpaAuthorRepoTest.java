package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.persistence.jpa.assembler.AuthorAssembler;
import MITELOVERS.persistence.jpa.datamodel.AuthorDataModel;
import MITELOVERS.persistence.springdata.IAuthorSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaAuthorRepoTest {

    @Mock
    private IAuthorSpringDataRepo _springDataRepoDouble;

    @Mock
    private AuthorAssembler _assemblerDouble;

    @InjectMocks
    private JpaAuthorRepo _jpaRepoDouble;


    @Test
    void shouldSaveAuthor() {
        // Arrange
        Author authorDouble = mock(Author.class);
        AuthorDataModel dataModelDouble = mock(AuthorDataModel.class);
        AuthorDataModel savedDataModelDouble = mock(AuthorDataModel.class);
        Author savedAuthorDouble = mock(Author.class);

        when(_assemblerDouble.toDataModel(authorDouble)).thenReturn(dataModelDouble);
        when(_springDataRepoDouble.save(dataModelDouble)).thenReturn(savedDataModelDouble);
        when(_assemblerDouble.toDomain(savedDataModelDouble)).thenReturn(savedAuthorDouble);

        // Act
        Author result = _jpaRepoDouble.save(authorDouble);

        // Assert
        assertEquals(savedAuthorDouble, result);
        verify(_assemblerDouble).toDataModel(authorDouble);
        verify(_springDataRepoDouble).save(dataModelDouble);
        verify(_assemblerDouble).toDomain(savedDataModelDouble);
    }

    @Test
    void shouldReturnAllKeys() {

        // Arrange
        AuthorDataModel dataModel1 = mock(AuthorDataModel.class);
        AuthorDataModel dataModel2 = mock(AuthorDataModel.class);

        when(dataModel1.getId()).thenReturn("1");
        when(dataModel2.getId()).thenReturn("2");

        List<AuthorDataModel> dataModels = new ArrayList<>();
        dataModels.add(dataModel1);
        dataModels.add(dataModel2);

        when(_springDataRepoDouble.findAll()).thenReturn(dataModels);

        // Act
        Iterable<AuthorId> result = _jpaRepoDouble.findAllKeys();

        List<AuthorId> ids = new ArrayList<>();
        for (AuthorId id : result) {
            ids.add(id);
        }

        // Assert
        assertEquals(2, ids.size());
        assertTrue(ids.contains(new AuthorId("1")));
        assertTrue(ids.contains(new AuthorId("2")));
    }

    @Test
    void shouldReturnAllAuthors() {

        // Arrange
        AuthorDataModel dataModel1 = mock(AuthorDataModel.class);
        AuthorDataModel dataModel2 = mock(AuthorDataModel.class);

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        List<AuthorDataModel> dataModels = new ArrayList<>();
        dataModels.add(dataModel1);
        dataModels.add(dataModel2);

        when(_springDataRepoDouble.findAll()).thenReturn(dataModels);
        when(_assemblerDouble.toDomain(dataModel1)).thenReturn(author1);
        when(_assemblerDouble.toDomain(dataModel2)).thenReturn(author2);

        // Act
        Iterable<Author> result = _jpaRepoDouble.findAll();

        List<Author> authors = new ArrayList<>();
        for (Author a : result) {
            authors.add(a);
        }

        // Assert
        assertEquals(2, authors.size());
        assertTrue(authors.contains(author1));
        assertTrue(authors.contains(author2));
    }

    @Test
    void shouldReturnAuthorById() {

        // Arrange
        AuthorId idDouble = mock(AuthorId.class);
        AuthorDataModel dataModelDouble = mock(AuthorDataModel.class);
        Author authorDouble = mock(Author.class);

        when(idDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.findById("1")).thenReturn(Optional.of(dataModelDouble));
        when(_assemblerDouble.toDomain(dataModelDouble)).thenReturn(authorDouble);

        // Act
        Optional<Author> result = _jpaRepoDouble.ofIdentity(idDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(authorDouble, result.get());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {

        // Arrange
        AuthorId idDouble = mock(AuthorId.class);

        when(idDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> _jpaRepoDouble.ofIdentity(idDouble));
    }

    @Test
    void shouldReturnTrueWhenAuthorExists() {

        // Arrange
        AuthorId idDouble = mock(AuthorId.class);

        when(idDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.existsById("1")).thenReturn(true);

        // Act
        boolean result = _jpaRepoDouble.containsOfIdentity(idDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenAuthorDoesNotExist() {

        // Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(authorIdDouble.toString()).thenReturn("1");
        when(_springDataRepoDouble.existsById("1")).thenReturn(false);

        // Act
        boolean result = _jpaRepoDouble.containsOfIdentity(authorIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldFindAuthorIdByName() {

        //Arrange
        String name = "Rem Koolhaas";

        AuthorDataModel dataModelDouble = mock(AuthorDataModel.class);
        Author authorDouble = mock(Author.class);
        AuthorId expectedAuthorId = mock(AuthorId.class);

        when(_springDataRepoDouble.findByName(name)).thenReturn(List.of(dataModelDouble));
        when(_assemblerDouble.toDomain(dataModelDouble)).thenReturn(authorDouble);
        when(authorDouble.identity()).thenReturn(expectedAuthorId);

        //Act
        AuthorId result = _jpaRepoDouble.findByName(name);

        //Assert
        assertEquals(expectedAuthorId, result);
    }

    @Test
    void shouldThrowExceptionWhenFindByNameReturnsEmptyList() {

        //Arrange
        String name = "Unknown Author";

        when(_springDataRepoDouble.findByName(name)).thenReturn(List.of());

        //Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> _jpaRepoDouble.findByName(name));
    }
}