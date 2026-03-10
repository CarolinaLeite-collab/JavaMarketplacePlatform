package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RegisterNewPublicationControllerTest {

    @Test
    void registerNewPublicationController_withSucess() {
        //arrange
        PublicationFactory _pfDouble = mock(PublicationFactory.class);
        PublicationRepo pubRepo = new PublicationRepo(_pfDouble);
        RegisterNewPublicationController controller =
                new RegisterNewPublicationController(pubRepo);
        PublicationType type = new PublicationType("BOOK");
        Identifier identifier = new ISBN("9780691181950");
        Year year = Year.of(2019);
        Title title = new Title("How to Keep Your Cool");
        Author author = new Author("Seneca");
        PublishingCompany publisher = new PublishingCompany("Penguin");

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
        PublicationFactory _pfDouble = mock(PublicationFactory.class);
        PublicationRepo repo = new PublicationRepo(_pfDouble);
        RegisterNewPublicationController controller = new RegisterNewPublicationController(repo);

        PublicationType type = new PublicationType("BOOK");
        Identifier id = new ISBN("9780691181950");
        Year year = Year.of(2019);
        Title title = new Title("How to Keep Your Cool");
        Author author = new Author("Seneca");
        PublishingCompany publisher = new PublishingCompany("Penguin");
        //act
        controller.registerPublication(type, id, year, title, author, publisher, null, null);

        assertThrows(IllegalArgumentException.class, () ->
                controller.registerPublication(type, id, year, title, author, publisher, null, null)
        );
    }
}