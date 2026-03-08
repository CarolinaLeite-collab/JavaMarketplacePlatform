package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CreatePrivateListOfPublicationsControllerTest {

    private ListOfPublicationsRepo repo;
    private GenreRepo genreRepo;
    private CreatePrivateListOfPublicationsController controller;

    private User user;
    private Genre action;
    private Genre poetry;

    @BeforeEach
    void setUp() {
        repo = mock(ListOfPublicationsRepo.class);
        genreRepo = mock(GenreRepo.class);

        controller = new CreatePrivateListOfPublicationsController(repo, genreRepo, null);

        user = new User(new Name("Joaquim"), new Email("test@isep.com"));
        action = new Genre("Action");
        poetry = new Genre("Poetry");
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfPublications list = new ListOfPublications(user, "My List", action);

        when(repo.addListOfPublications(user, "My List", action)).thenReturn(list);

        // Act
        ListOfPublications result = controller.createListOfPublications(user, "My List", action);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(list, result)
        );
        verify(repo).addListOfPublications(user, "My List", action);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(repo.addListOfPublications(user, "My List", action)).thenReturn(null);

        // Act
        ListOfPublications duplicate = controller.createListOfPublications(user, "My List", action);

        // Assert
        assertNull(duplicate);
        verify(repo).addListOfPublications(user, "My List", action);
    }

    @Test
    void getListOfOfficialGenresReturnsUnmodifiableList() {
        // Arrange
        when(genreRepo.getListOfOfficialGenres()).thenReturn(List.of(action, poetry));

        // Act
        List<Genre> officialGenres = controller.getListOfOfficialGenres();

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> officialGenres.add(new Genre("Horror")));
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        // Arrange
        when(genreRepo.getListOfOfficialGenres()).thenReturn(List.of(action, poetry));

        // Act
        List<Genre> officialGenres = controller.getListOfOfficialGenres();

        // Assert
        assertAll(
                () -> assertEquals(2, officialGenres.size()),
                () -> assertTrue(officialGenres.contains(action)),
                () -> assertTrue(officialGenres.contains(poetry))
        );
    }
}