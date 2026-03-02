package TOPSECRET.controller;
import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsInMyLibraryControllerTest {

    private User _user;
    private LibraryRepo _libraryRepo;
    private ListOfPublicationsInMyLibraryController _controller;

    @BeforeEach

    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email("test@isep.com")
        );

        _libraryRepo = new LibraryRepo();
        _controller = new ListOfPublicationsInMyLibraryController(_libraryRepo, _user);
    }


    @Test
    void shouldReturnEmptyList_whenLibraryExistsButEmpty() {
        // Arrange
        Library myLibrary = _libraryRepo.createMyLibrary(_user);

        // Act
        List<PublicationDetails> result = _controller.getListOfPublications(_user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfPublicationsInLibrary() {

        Library myLibrary= _libraryRepo.createMyLibrary(_user);
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        myLibrary.addPublicationToLibrary(p);

        List<PublicationDetails> result = _controller.getListOfPublications(_user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(p.getTitle(), result.get(0).getTitle());
        assertEquals(p.getAuthor(), result.get(0).getAuthor());
        assertEquals(p.getPublicationType(), result.get(0).getPublicationType());
        assertEquals(p.getIdentifier(), result.get(0).getIdentifier());
    }


    @Test
    void shouldThrowException_whenLibraryNotFound() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _libraryRepo.findByUser(_user)
        );
        assertEquals("Library not found for user: Zé Isep", exception.getMessage());
    }

}
