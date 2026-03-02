package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddPublicationOnLibraryControllerTest {

    private AddPublicationOnLibraryController _controller;
    private PublicationRepo _publicationRepo;
    private LibraryRepo _libraryRepo;

    private User _user;
    private Publication _p1;
    private Publication _p2;

    @BeforeEach
    void setUp() {
        _publicationRepo = new PublicationRepo();
        _libraryRepo = new LibraryRepo();
        _controller = new AddPublicationOnLibraryController(_publicationRepo, _libraryRepo);

        _user = new User(
                new Name("Maria"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", Address.Country.PORTUGAL, "1000-205", null),
                new Email("maria123@hotmail.com"),
                new Phone(new PhonePrefix("+351"), "918902632"));
        _p1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();
        _p2 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789723701241"))
                .year(Year.of(2020))
                .title(new Title("The Hobbit"))
                .author(new Author("Somebody"))
                .publisher(new PublishingCompany("Girafa"))
                .build();

        _publicationRepo.add(_p1);
        _publicationRepo.add(_p2);

        _libraryRepo.createMyLibrary(_user);
    }

    @Test
    void shouldReturnLibraryOfGivenUser() {
        // Checks if the user's library is correctly returned

        // Act
        Library result = _controller.getMyLibrary(_user);

        // Assert
        assertNotNull(result);
        assertEquals(_user, result.getUser());
    }

    @Test
    void shouldReturnAllPublicationsWhenLibraryIsEmpty() {
        // Verifies that all publications are returned when the user's library is empty

        // Act
        List<Publication> result = _controller.getListOfAvailablePublications(_user);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_p1));
        assertTrue(result.contains(_p2));
    }

    @Test
    void shouldReturnOnlyPublicationsNotInLibrary() {
        // Verifies that only publications not already present in the user's library are returned

        // Arrange
        _controller.addPublicationToLibrary(_p1, _user);

        // Act
        List<Publication> result = _controller.getListOfAvailablePublications(_user);

        // Assert
        assertEquals(1, result.size());
        assertFalse(result.contains(_p1));
        assertTrue(result.contains(_p2));
    }

    @Test
    void shouldAddPublicationToLibrary() {
        // Checks that a publication is correctly added to the library

        // Act
        boolean result = _controller.addPublicationToLibrary(_p1, _user);

        // Assert
        assertTrue(result);

        Library library = _controller.getMyLibrary(_user);
        assertTrue(library.getAllPublications().contains(_p1));
    }

    @Test
    void shouldNotAllowDuplicatePublicationInLibrary() {
        // Checks that adding a duplicate publication is rejected

        // Arrange
        _controller.addPublicationToLibrary(_p1, _user);

        // Act
        boolean result = _controller.addPublicationToLibrary(_p1, _user);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenLibraryDoesNotExist() {
        // Verifies that an exception is thrown when attempting to retrieve a library for a user without one

        User otherUser = new User(new Name("João"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", Address.Country.PORTUGAL, "1000-205", null),
                new Email("joao123@hotmail.com"),
                new Phone(new PhonePrefix("+351"), "918902632"));

        assertThrows(IllegalStateException.class, () -> _controller.getMyLibrary(otherUser)
        );
    }
}