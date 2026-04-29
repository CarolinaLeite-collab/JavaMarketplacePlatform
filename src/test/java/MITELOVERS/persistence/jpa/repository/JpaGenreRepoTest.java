package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.assembler.GenreAssembler;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import MITELOVERS.persistence.jpa.springdata.IGenreSpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaGenreRepoTest {

    @Mock
    private IGenreSpringDataRepo _genreSpringDataRepoDouble;

    @Mock
    private GenreAssembler _genreAssemblerDouble;

    @Mock
    private Genre _genreDouble;

    @Mock
    private GenreDataModel _dataModelDouble;

    private JpaGenreRepo _jpaGenreRepo;

    @BeforeEach
    void setUp() {
        _jpaGenreRepo = new JpaGenreRepo(_genreSpringDataRepoDouble, _genreAssemblerDouble);
    }

    @Test
    void testAConstructor() {
        new JpaGenreRepo(_genreSpringDataRepoDouble, _genreAssemblerDouble);
    }

    @Test
    void testSaveShouldReturnDomainEntity() {
        // Arrange
        when(_genreAssemblerDouble.toDataModel(_genreDouble)).thenReturn(_dataModelDouble);
        when(_genreSpringDataRepoDouble.save(_dataModelDouble)).thenReturn(_dataModelDouble);
        when(_genreAssemblerDouble.toDomain(_dataModelDouble)).thenReturn(_genreDouble);

        // Act
        Genre result = _jpaGenreRepo.save(_genreDouble);

        // Assert
        assertEquals(_genreDouble, result);
        verify(_genreAssemblerDouble).toDataModel(_genreDouble);
        verify(_genreSpringDataRepoDouble).save(_dataModelDouble);
        verify(_genreAssemblerDouble).toDomain(_dataModelDouble);
    }

    @Test
    void testFindAllKeysShouldReturnListOfIds() {
        // Arrange
        when(_genreSpringDataRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_dataModelDouble.getId()).thenReturn("Fantasy");

        // Act
        Iterable<GenreId> result = _jpaGenreRepo.findAllKeys();
        List<GenreId> resultList = new ArrayList<>();

        for (GenreId id : result) {
            resultList.add(id);
        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals("FANTASY", resultList.get(0).toString());
    }

    @Test
    void testFindAllShouldReturnAllSavedGenres() {
        // Arrange
        when(_genreSpringDataRepoDouble.findAll()).thenReturn(List.of(_dataModelDouble));
        when(_genreAssemblerDouble.toDomain(_dataModelDouble)).thenReturn(_genreDouble);

        // Act
        Iterable<Genre> result = _jpaGenreRepo.findAll();
        List<Genre> resultList = new ArrayList<>();

        for (Genre genre : result) {
            resultList.add(genre);
        }

        // Assert
        assertEquals(1, resultList.size());
        assertEquals(_genreDouble, resultList.get(0));
    }

    @Test
    void testOfIdentityShouldReturnGenreOfACertainId() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.toString()).thenReturn("Fantasy");
        when(_genreSpringDataRepoDouble.findById(genreIdDouble.toString())).thenReturn(Optional.of(_dataModelDouble));
        when(_genreAssemblerDouble.toDomain(_dataModelDouble)).thenReturn(_genreDouble);

        // Act
        Optional<Genre> result = _jpaGenreRepo.ofIdentity(genreIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(_genreDouble, result.get());
    }

    @Test
    void testOfIdentityShouldReturnEmptyWhenNotFound() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.toString()).thenReturn("Missing");
        when(_genreSpringDataRepoDouble.findById(genreIdDouble.toString())).thenReturn(Optional.empty());

        // Act
        Optional<Genre> result = _jpaGenreRepo.ofIdentity(genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testContainsOfIdentityShouldReturnTrueWhenSavedGenreExists() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.toString()).thenReturn("Fantasy");
        when(_genreSpringDataRepoDouble.existsById(genreIdDouble.toString())).thenReturn(true);

        // Act
        boolean result = _jpaGenreRepo.containsOfIdentity(genreIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityShouldReturnFalseWhenSavedGenreDoesNotExist() {
        // Arrange
        GenreId otherGenreIdDouble = mock(GenreId.class);
        when(otherGenreIdDouble.toString()).thenReturn("Other");
        when(_genreSpringDataRepoDouble.existsById(otherGenreIdDouble.toString())).thenReturn(false);

        // Act
        boolean result = _jpaGenreRepo.containsOfIdentity(otherGenreIdDouble);

        // Assert
        assertFalse(result);
    }
}

