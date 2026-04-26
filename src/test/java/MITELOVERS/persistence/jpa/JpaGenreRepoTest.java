package MITELOVERS.persistence.jpa;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.assembler.GenreAssembler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Import({JpaGenreRepo.class, GenreAssembler.class, GenreFactory.class})
class JpaGenreRepoTest {

    @Autowired
    private JpaGenreRepo _jpaGenreRepo;

    @Autowired
    private GenreFactory _genreFactory;

    @Test
    @Tag("integration")
    void saveAndOfIdentityShouldPersistAndLoadGenre() {
        // Arrange
        Genre _genre = _genreFactory.createGenre("Fantasy");

        // Act
        Genre savedGenre = _jpaGenreRepo.save(_genre);

        // Assert
        assertNotNull(savedGenre);
        assertEquals("Fantasy", savedGenre.getGenre());
        assertTrue(_jpaGenreRepo.ofIdentity(new GenreId("Fantasy")).isPresent());
        assertEquals("Fantasy", _jpaGenreRepo.ofIdentity(new GenreId("Fantasy")).get().getGenre());
    }

    @Test
    @Tag("integration")
    void containsOfIdentityShouldReturnTrueForStoredAndFalseForMissing() {
        // Arrange
        Genre _stored = _genreFactory.createGenre("Horror");
        _jpaGenreRepo.save(_stored);

        // Act
        boolean containsStored = _jpaGenreRepo.containsOfIdentity(new GenreId("Horror"));
        boolean containsMissing = _jpaGenreRepo.containsOfIdentity(new GenreId("Romance"));

        // Assert
        assertTrue(containsStored);
        assertFalse(containsMissing);
    }

    @Test
    @Tag("integration")
    void findAllAndFindAllKeysShouldReturnAllPersistedGenres() {
        // Arrange
        _jpaGenreRepo.save(_genreFactory.createGenre("Drama"));
        _jpaGenreRepo.save(_genreFactory.createGenre("Poetry"));

        // Act
        List<Genre> allGenres = new ArrayList<>();
        _jpaGenreRepo.findAll().forEach(allGenres::add);

        List<GenreId> allKeys = new ArrayList<>();
        _jpaGenreRepo.findAllKeys().forEach(allKeys::add);

        // Assert
        assertEquals(2, allGenres.size());
        assertEquals(2, allKeys.size());

        Set<String> genreNames = new HashSet<>();
        allGenres.forEach(genre -> genreNames.add(genre.getGenre()));

        Set<String> genreIds = new HashSet<>();
        allKeys.forEach(genreId -> genreIds.add(genreId.toString()));

        assertTrue(genreNames.contains("Drama"));
        assertTrue(genreNames.contains("Poetry"));
        assertTrue(genreIds.contains("DRAMA"));
        assertTrue(genreIds.contains("POETRY"));
    }
}
