package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.persistence.jpa.assembler.AuthorAssembler;
import MITELOVERS.persistence.jpa.datamodel.AuthorDataModel;
import MITELOVERS.persistence.springdata.IAuthorSpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaAuthorRepoTest {

    private IAuthorSpringDataRepo _springDataRepoDouble;
    private AuthorAssembler _assemblerDouble;
    private JpaAuthorRepo _jpaRepoDouble;

    @BeforeEach
    void setup() {
        _springDataRepoDouble = mock(IAuthorSpringDataRepo.class);
        _assemblerDouble = mock(AuthorAssembler.class);
        _jpaRepoDouble = new JpaAuthorRepo(_springDataRepoDouble, _assemblerDouble);
    }

    @Test
    void testConstructor() {
        // SUT
        new JpaAuthorRepo(_springDataRepoDouble, _assemblerDouble);
    }

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

        // SUT
        new JpaAuthorRepo(_springDataRepoDouble, _assemblerDouble);

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

        // SUT
        new JpaAuthorRepo(_springDataRepoDouble, _assemblerDouble);

        // Act
        Iterable<AuthorId> result = _jpaRepoDouble.findAllKeys();

        List<AuthorId> ids = new ArrayList<>();
        for (AuthorId id : result) {
            ids.add(id);
        }

        // Assert
        assertEquals(2, ids.size());
        assertEquals("1", ids.get(0).toString());
        assertEquals("2", ids.get(1).toString());
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
        IAuthorSpringDataRepo springRepoDouble = mock(IAuthorSpringDataRepo.class);
        AuthorAssembler assemblerDouble = mock(AuthorAssembler.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(authorIdDouble.toString()).thenReturn("1");
        when(springRepoDouble.existsById("1")).thenReturn(false);

        JpaAuthorRepo repo = new JpaAuthorRepo(springRepoDouble, assemblerDouble);

        // Act
        boolean result = repo.containsOfIdentity(authorIdDouble);

        // Assert
        assertFalse(result);
    }
}