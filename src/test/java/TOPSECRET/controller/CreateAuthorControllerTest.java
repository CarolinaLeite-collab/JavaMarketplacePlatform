package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAuthorControllerTest {

    private CreateAuthorController controller;

    // Isolation between tests
    @BeforeEach
    void setUp() {
        AuthorRepo repo = new AuthorRepo();
        controller = new CreateAuthorController(repo);
    }

    @Test
    void shouldCreateAuthorWithValidName() {
        Author author = controller.createAuthor("João");
        assertNotNull(author);
        assertEquals("João", author.getName());
    }

    @Test
    // Trims unecessary spaces
    void shouldTrimAuthorName() {
        Author author = controller.createAuthor(" Ana    ");
        assertEquals("Ana", author.getName());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.createAuthor(null));
        assertEquals("Author name is mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> controller.createAuthor(""));
    }

    @Test
    void shouldThrowExceptionWhenAuthorNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> controller.createAuthor("   "));
    }

    @Test
    void shouldThrowWhenAuthorAlreadyExists() {
        controller.createAuthor("Maria");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> controller.createAuthor(" maria "));
        assertEquals("Author already exists", ex.getMessage());
    }


}