package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorRepo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAuthorControllerTest {

    private CreateAuthorController controller;
    private AuthorRepo authorRepoDouble;

    @Test
    void shouldCreateAuthorWithValidName() {
        //arrange
        authorRepoDouble = mock(AuthorRepo.class);
        String name = "João";
        Author authorDouble = mock(Author.class);

        //SUT
        controller = new CreateAuthorController(authorRepoDouble);

        //act
        when(authorRepoDouble.createAuthor("João")).thenReturn(authorDouble);

        Author author = controller.createAuthor(name);

        //assert
        assertNotNull(author);
        assertEquals(authorDouble, author);
    }

    @Test
    void shouldTrimAuthorName() {
        //arrange
        authorRepoDouble = mock(AuthorRepo.class);
        String name = "João";
        Author authorDouble = mock(Author.class);

        //SUT
        controller = new CreateAuthorController(authorRepoDouble);

        //act
        when(authorRepoDouble.createAuthor(name)).thenReturn(authorDouble);
        when(authorDouble.getName()).thenReturn(name);

        Author author = controller.createAuthor("João  ");

        //assert
        assertEquals(authorDouble.getName(), author.getName());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNameIsNull() {
        //arrange
        authorRepoDouble = mock(AuthorRepo.class);

        //SUT
        controller = new CreateAuthorController(authorRepoDouble);

        //act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> controller.createAuthor(null));

        //assert
        assertEquals("Author name is mandatory", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNameIsEmpty() {
        //arrange
        authorRepoDouble = mock(AuthorRepo.class);

        //SUT
        controller = new CreateAuthorController(authorRepoDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> controller.createAuthor(""));
    }

    @Test
    void shouldThrowExceptionWhenAuthorNameIsBlank() {
        //arrange
        authorRepoDouble = mock(AuthorRepo.class);

        //SUT
        controller = new CreateAuthorController(authorRepoDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> controller.createAuthor("   "));
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        //arrange
        authorRepoDouble = mock(AuthorRepo.class);

        //SUT
        controller = new CreateAuthorController(authorRepoDouble);

        //act
        when(authorRepoDouble.createAuthor("Maria")).thenThrow(new IllegalStateException("Author already exists"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> controller.createAuthor("Maria "));

        //assert
        assertEquals("Author already exists", ex.getMessage());
    }

}
