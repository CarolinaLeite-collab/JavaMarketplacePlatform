package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsTest {

    private User user1;
    private User user2;
    private Genre actionGenre;
    private Genre poetryGenre;

    @BeforeEach
    void setUp() {
        user1 = new User (
                new Name ("Trump"),
                new Email ("usa@isep.com")
        );
        user2 = new User (
                new Name ("Putin"),
                new Email ("russia@isep.com")
        );

        GenreRepo genreRepo = new GenreRepo();
        actionGenre = genreRepo.addGenre("Action");
        poetryGenre = genreRepo.addGenre("Poetry");
    }

    @Test
    void constructsListSuccessfully() {
        // Arrange & Act
        ListOfPublications list = new ListOfPublications(user1,"My favorite books",actionGenre);

        // Assert
        assertNotNull(list);
    }

    @Test
    void constructorShouldThrowExceptionForNulls() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ListOfPublications(null, "Name", actionGenre));
        assertThrows(IllegalArgumentException.class, () -> new ListOfPublications(user1, null, actionGenre));
        assertThrows(IllegalArgumentException.class, () -> new ListOfPublications(user1, "Name", null));
    }

    @Test
    void equalsShouldReturnTrueForSameArguments() {
        //Arrange
        ListOfPublications list1 = new ListOfPublications(user1, "My List", actionGenre);
        ListOfPublications list2 = new ListOfPublications(user1, "My List", actionGenre);

        // Act & Assert
        assertEquals(list1, list2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentArguments() {
        //Arrange
        // Same name
        ListOfPublications list1 = new ListOfPublications(user1, "My List", actionGenre);
        ListOfPublications list2 = new ListOfPublications(user2, "My List", poetryGenre);
        // Same genre
        ListOfPublications list3 = new ListOfPublications(user1, "My List", actionGenre);
        ListOfPublications list4 = new ListOfPublications(user2, "Books list", actionGenre);
        // Same user
        ListOfPublications list5 = new ListOfPublications(user1, "My List", actionGenre);
        ListOfPublications list6 = new ListOfPublications(user1, "Books list", poetryGenre);

        // Act & Assert
        assertNotEquals(list1, list2);
        assertNotEquals(list3, list4);
        assertNotEquals(list5, list6);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        // Arrange
        ListOfPublications list = new ListOfPublications(user1, "My List", actionGenre);

        // Act & Assert
        assertNotEquals(list, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        // Arrange
        ListOfPublications list = new ListOfPublications(user1, "My List", actionGenre);
        String notAList = "not a ListOfPublications";

        // Act & Assert
        assertNotEquals(list, notAList);
    }

    @Test
    void makeListPublicWhenInitiallyPrivate() {
        // switchVisibility() – changes visibility from private to public
        ListOfPublications list = new ListOfPublications(user1, "Lista", actionGenre);

        assertTrue(list.isPrivate());
        list.switchVisibility();
        assertFalse(list.isPrivate());
    }

    @Test
    void makeListPrivateWhenPublic() {
        // switchVisibility() – changes visibility from public back to private
        ListOfPublications list = new ListOfPublications(user1, "Lista", actionGenre);

        list.switchVisibility();
        assertFalse(list.isPrivate());

        list.switchVisibility();
        assertTrue(list.isPrivate());
    }

    @Test
    void createsValidListWhenParametersAreValid() {
        // Constructor – creates a valid list with default values
        ListOfPublications list = new ListOfPublications(user1, "Lista", actionGenre);

        assertNotNull(list);
        assertTrue(list.isPrivate());
        assertTrue(list.getPublications().isEmpty());
    }

    @Test
    void throwsExceptionWhenUserIsNull() {
        // Constructor – throws exception when user is null
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(null, "Lista", actionGenre));
    }

    @Test
    void throwsExceptionWhenListNameIsNull() {
        // Constructor – throws exception when list name is null
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(user1, null, actionGenre));
    }

    @Test
    void throwsExceptionWhenGenreIsNull() {
        // Constructor – throws exception when genre is null
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(user1, "Lista", null));
    }
}