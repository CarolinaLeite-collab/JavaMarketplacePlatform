package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class RegisterNewPublicationControllerTest {

    @Test
    void registerNewPublicationController_withSucess() {
        //arrange
        PublicationRepo pubRepo = new PublicationRepo();
        RegisterNewPublicationController controller =
                new RegisterNewPublicationController(pubRepo);
        PublicationType type = new PublicationType("BOOK");
        Identifier identifier = new ISBN("9780691181950");
        Year year = Year.of(2019);
        Title title = new Title("How to Keep Your Cool");
        Author author = new Author("Seneca");
        Publisher publisher = new Publisher("Penguin");

        //act
        Publication result = controller.registerPublication(type, identifier, year, title, author, publisher, null, null);

        // assert
        assertNotNull(result);
        assertEquals(identifier, result.getIdentifier());
        assertEquals(title, result.getTitle());
    }

    @Test
    void constructor_withNullRepo_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RegisterNewPublicationController(null));
        // This will only pass if you add Objects.requireNonNull in the constructor.
    }

    @Test
    void registerPublication_whenPublicationAlreadyExists_throwsIllegalArgumentException() {
        //arrange
        PublicationRepo repo = new PublicationRepo();
        RegisterNewPublicationController controller = new RegisterNewPublicationController(repo);

        PublicationType type = new PublicationType("BOOK");
        Identifier id = new ISBN("9780691181950");
        Year year = Year.of(2019);
        Title title = new Title("How to Keep Your Cool");
        Author author = new Author("Seneca");
        Publisher publisher = new Publisher("Penguin");
        //act
        controller.registerPublication(type, id, year, title, author, publisher, null, null);

        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(type, id, year, title, author, publisher, null, null)
        );
    }
}