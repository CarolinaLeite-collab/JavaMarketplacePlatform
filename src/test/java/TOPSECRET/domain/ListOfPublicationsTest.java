package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsTest {

    private User _user1;
    private User _user2;
    private Genre _actionGenre;
    private Genre _poetryGenre;

    @BeforeEach
    void setUp() {
        _user1 = new User (
                new Name ("Trump"),
                new Email ("usa@isep.com")
        );
        _user2 = new User (
                new Name ("Putin"),
                new Email ("russia@isep.com")
        );

        GenreRepo genreRepo = new GenreRepo();
        _actionGenre = genreRepo.addGenre("Action");
        _poetryGenre = genreRepo.addGenre("Poetry");
    }

    @Test
    void constructsListSuccessfully() {
        // Arrange & Act
        ListOfPublications list = new ListOfPublications(_user1,"My favorite books", _actionGenre);

        // Assert
        assertNotNull(list);
    }

    @Test
    void constructorShouldThrowExceptionForNulls() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ListOfPublications(null, "Name", _actionGenre));
        assertThrows(IllegalArgumentException.class, () -> new ListOfPublications(_user1, null, _actionGenre));
        assertThrows(IllegalArgumentException.class, () -> new ListOfPublications(_user1, "Name", null));
    }

    @Test
    void equalsShouldReturnTrueForSameArguments() {
        //Arrange
        ListOfPublications list1 = new ListOfPublications(_user1, "My List", _actionGenre);
        ListOfPublications list2 = new ListOfPublications(_user1, "My List", _actionGenre);

        // Act & Assert
        assertEquals(list1, list2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentArguments() {
        //Arrange
        // Same name
        ListOfPublications list1 = new ListOfPublications(_user1, "My List", _actionGenre);
        ListOfPublications list2 = new ListOfPublications(_user2, "My List", _poetryGenre);
        // Same genre
        ListOfPublications list3 = new ListOfPublications(_user1, "My List", _actionGenre);
        ListOfPublications list4 = new ListOfPublications(_user2, "Books list", _actionGenre);
        // Same user
        ListOfPublications list5 = new ListOfPublications(_user1, "My List", _actionGenre);
        ListOfPublications list6 = new ListOfPublications(_user1, "Books list", _poetryGenre);

        // Act & Assert
        assertNotEquals(list1, list2);
        assertNotEquals(list3, list4);
        assertNotEquals(list5, list6);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1, "My List", _actionGenre);

        // Act & Assert
        assertNotEquals(list, null);
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        // Arrange
        ListOfPublications list = new ListOfPublications(_user1, "My List", _actionGenre);
        String notAList = "not a ListOfPublications";

        // Act & Assert
        assertNotEquals(list, notAList);
    }

    @Test
    void makeListPublicWhenInitiallyPrivate() {
        // switchVisibility() – changes visibility from private to public
        ListOfPublications list = new ListOfPublications(_user1, "Lista", _actionGenre);

        assertTrue(list.isPrivate());
        list.switchVisibility();
        assertFalse(list.isPrivate());
    }

    @Test
    void makeListPrivateWhenPublic() {
        // switchVisibility() – changes visibility from public back to private
        ListOfPublications list = new ListOfPublications(_user1, "Lista", _actionGenre);

        list.switchVisibility();
        assertFalse(list.isPrivate());

        list.switchVisibility();
        assertTrue(list.isPrivate());
    }

    @Test
    void createsValidListWhenParametersAreValid() {
        // Constructor – creates a valid list with default values
        ListOfPublications list = new ListOfPublications(_user1, "Lista", _actionGenre);

        assertNotNull(list);
        assertTrue(list.isPrivate());
        assertTrue(list.getPublications().isEmpty());
    }

    @Test
    void throwsExceptionWhenUserIsNull() {
        // Constructor – throws exception when user is null
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(null, "Lista", _actionGenre));
    }

    @Test
    void throwsExceptionWhenListNameIsNull() {
        // Constructor – throws exception when list name is null
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(_user1, null, _actionGenre));
    }

    @Test
    void throwsExceptionWhenGenreIsNull() {
        // Constructor – throws exception when genre is null
        assertThrows(IllegalArgumentException.class,
                () -> new ListOfPublications(_user1, "Lista", null));
    }

    @Test
    void publicationsListShouldStartEmpty() {
        ListOfPublications list = new ListOfPublications(_user1, "Lista", _actionGenre);
        assertTrue(list.getPublications().isEmpty());
    }

    @Test
    void addPublicationShouldAddSuccessfully() {
        ListOfPublications list = new ListOfPublications(_user1, "Lista", _actionGenre);

        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("0306406152")) // valid
                .year(Year.of(2000))
                .title(new Title("Some Title"))
                .author(new Author("Some Author"))
                .publisher(new PublishingCompany("Some Publisher"))
                .genre(_actionGenre)
                .build();

        list.addPublication(pub);

        assertEquals(1, list.getPublications().size());
        assertEquals(pub, list.getPublications().get(0));
    }

    @Test
    void addPublicationShouldThrowWhenDuplicate() {
        ListOfPublications list = new ListOfPublications(_user1, "Lista", _actionGenre);

        Publication pub = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(2020))
                .title(new Title("Magazine A"))
                .publisher(new PublishingCompany("Publisher M"))
                .genre(_actionGenre)
                .build();

        list.addPublication(pub);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> list.addPublication(pub)
        );
        assertEquals("Publication already in list", ex.getMessage());
    }
}