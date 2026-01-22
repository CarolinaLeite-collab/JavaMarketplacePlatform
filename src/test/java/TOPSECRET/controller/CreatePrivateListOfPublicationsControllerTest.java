package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePrivateListOfPublicationsControllerTest {

    private User user1;
    private Genre actionGenre;
    private Genre poetryGenre;
    private GenreRepo genreRepo;
    private ListOfPublicationsRepo repo;
    private CreatePrivateListOfPublicationsController controller;

    @BeforeEach
    void setUp() {
        user1 = new User(new Name("Joaquim"), new Email("test@isep.com"));

        genreRepo = new GenreRepo();
        actionGenre = genreRepo.create("Action");
        poetryGenre = genreRepo.create("poetry");

        repo = new ListOfPublicationsRepo(genreRepo);
        controller = new CreatePrivateListOfPublicationsController(repo, genreRepo);
    }

    @Test
    void shouldcreateListSuccessfully() {
        // Arrange & Act
        ListOfPublications list = controller.createListOfPublications(user1, "My List", actionGenre);

        // Assert
        assertNotNull(list);
        assertEquals(1, repo.getListOfPublications().size());
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        controller.createListOfPublications(user1, "My List", actionGenre);

        // Act
        ListOfPublications duplicate = controller.createListOfPublications(user1, "My List", actionGenre);

        // Assert
        assertNull(duplicate);
        assertEquals(1, repo.getListOfPublications().size());
    }

    @Test
    void getOfficialGenresReturnsUnmodifiableList() {
        List<Genre> officialGenres = controller.getOfficialGenres();

        assertThrows(UnsupportedOperationException.class, () -> officialGenres.add(new Genre("Horror")));
    }

    @Test
    void getOfficialGenresReturnsCorrectList() {
        List<Genre> officialGenres = controller.getOfficialGenres();

        assertNotNull(officialGenres);
        assertEquals(2, officialGenres.size());
        assertTrue(officialGenres.contains(actionGenre));
        assertTrue(officialGenres.contains(poetryGenre));
    }




}