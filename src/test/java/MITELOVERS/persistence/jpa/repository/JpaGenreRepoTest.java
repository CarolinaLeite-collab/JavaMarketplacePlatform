package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.assembler.GenreAssembler;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import MITELOVERS.persistence.springdata.IGenreSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaGenreRepoTest {

    @Mock
    private IGenreSpringDataRepo _genreSpringDataRepoDouble;

    @Mock
    private GenreAssembler _genreAssemblerDouble;

    @InjectMocks
    private JpaGenreRepo _jpaGenreRepo;

    @Test
    void testSaveShouldReturnDomainEntity() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        GenreDataModel dataModelDouble = mock(GenreDataModel.class);
        GenreDataModel savedDataModelDouble = mock(GenreDataModel.class);
        Genre savedGenreDouble = mock(Genre.class);

        when(_genreAssemblerDouble.toDataModel(genreDouble)).thenReturn(dataModelDouble);
        when(_genreSpringDataRepoDouble.save(dataModelDouble)).thenReturn(savedDataModelDouble);
        when(_genreAssemblerDouble.toDomain(savedDataModelDouble)).thenReturn(savedGenreDouble);

        // Act
        Genre result = _jpaGenreRepo.save(genreDouble);

        // Assert
        assertEquals(savedGenreDouble, result);
        verify(_genreAssemblerDouble).toDataModel(genreDouble);
        verify(_genreSpringDataRepoDouble).save(dataModelDouble);
        verify(_genreAssemblerDouble).toDomain(savedDataModelDouble);
    }

    @Test
    void testFindAllKeysShouldReturnListOfIds() {
        // Arrange
        GenreDataModel dataModelDouble = mock(GenreDataModel.class);
        when(dataModelDouble.getId()).thenReturn("FANTASY");
        when(_genreSpringDataRepoDouble.findAll()).thenReturn(List.of(dataModelDouble));

        // Act
        Iterable<GenreId> result = _jpaGenreRepo.findAllKeys();
        List<GenreId> resultList = new ArrayList<>();
        for (GenreId id : result) {
            resultList.add(id);
        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals(new GenreId("FANTASY"), resultList.get(0));
    }

    @Test
    void testFindAllShouldReturnAllSavedGenres() {
        // Arrange
        GenreDataModel dataModelDouble = mock(GenreDataModel.class);
        Genre genreDouble = mock(Genre.class);

        when(_genreSpringDataRepoDouble.findAll()).thenReturn(List.of(dataModelDouble));
        when(_genreAssemblerDouble.toDomain(dataModelDouble)).thenReturn(genreDouble);

        // Act
        Iterable<Genre> result = _jpaGenreRepo.findAll();
        List<Genre> resultList = new ArrayList<>();
        for (Genre genre : result) {
            resultList.add(genre);
        }

        // Assert
        assertEquals(1, resultList.size());
        assertTrue(resultList.contains(genreDouble));
    }

    @Test
    void testOfIdentityShouldReturnGenreOfACertainId() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        GenreDataModel dataModelDouble = mock(GenreDataModel.class);
        Genre genreDouble = mock(Genre.class);

        when(genreIdDouble.toString()).thenReturn("FANTASY");
        when(_genreSpringDataRepoDouble.findById("FANTASY")).thenReturn(Optional.of(dataModelDouble));
        when(_genreAssemblerDouble.toDomain(dataModelDouble)).thenReturn(genreDouble);

        // Act
        Optional<Genre> result = _jpaGenreRepo.ofIdentity(genreIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(genreDouble, result.get());
    }

    @Test
    void testContainsOfIdentityShouldReturnTrueWhenSavedGenreExists() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.toString()).thenReturn("FANTASY");
        when(_genreSpringDataRepoDouble.existsById("FANTASY")).thenReturn(true);

        // Act
        boolean result = _jpaGenreRepo.containsOfIdentity(genreIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityShouldReturnFalseWhenSavedGenreDoesNotExist() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.toString()).thenReturn("FANTASY");
        when(_genreSpringDataRepoDouble.existsById("FANTASY")).thenReturn(false);

        // Act
        boolean result = _jpaGenreRepo.containsOfIdentity(genreIdDouble);

        // Assert
        assertFalse(result);
    }
}