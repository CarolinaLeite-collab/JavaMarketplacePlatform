package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsRepoTest {

    private User user1;
    private Genre actionGenre;
    private Genre poetryGenre;
    private GenreRepo genreRepo;
    private ListOfPublicationsRepo repo;

    @BeforeEach
    void setUp() {
        user1 = new User(new Name("Joaquim"), new Email("test@isep.com"));

        genreRepo = new GenreRepo();
        actionGenre = genreRepo.create("Action");
        poetryGenre = genreRepo.create("Poetry");

        repo = new ListOfPublicationsRepo(genreRepo);
    }

    @Test
    void createListSuccessfully() {
        // Arrange & Act
        ListOfPublications list = repo.createListOfPublications(user1, "My List", actionGenre);

        // Assert
        assertNotNull(list);
        assertEquals(1, repo.getListOfListOfPublications().size());
    }

    @Test
    void cannotCreateDuplicateList() {
        // Arrange
        repo.createListOfPublications(user1, "My List", actionGenre);

        // Act
        ListOfPublications duplicate = repo.createListOfPublications(user1, "My List", actionGenre);

        // Assert
        assertNull(duplicate);
        assertEquals(1, repo.getListOfListOfPublications().size());
    }

    @Test
    void createListWithNullsShouldThrow() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.createListOfPublications(null, "Name", actionGenre));
        assertThrows(IllegalArgumentException.class, () -> repo.createListOfPublications(user1, null, actionGenre));
        assertThrows(IllegalArgumentException.class, () -> repo.createListOfPublications(user1, "Name", null));
    }

    @Test
    void getListReturnsCopy() {
        // Arrange
        repo.createListOfPublications(user1, "My List", actionGenre);

        // Act
        var lists = repo.getListOfListOfPublications();

        // Assert
        assertEquals(1, lists.size());
        assertThrows(UnsupportedOperationException.class, () -> lists.add(new ListOfPublications(user1, "Other List", poetryGenre)));
    }

}