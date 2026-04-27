package MITELOVERS.domain.author;

import MITELOVERS.domain.valueobject.AuthorId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorFactoryTest {
    @Test
    void shouldCreateAuthor() {
        //arrange
        String authorName = "João";

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class,
                             (mock, context) -> {
                                 when(mock.getName())
                                         .thenReturn(authorName);
                             })) {

        //SUT
        AuthorFactory factory = new AuthorFactory();

        //act
        Author author = factory.createAuthor(authorName);

        //assert
        assertEquals(authorName, author.getName());
        }
    }

    @Test
    void shouldCreateAuthorWithId() {
        // arrange
        AuthorId authorId = mock(AuthorId.class);
        String authorName = "João";

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class,
                             (mock, context) -> {
                                 when(mock.getName()).thenReturn(authorName);
                             })) {

        //SUT
        AuthorFactory factory = new AuthorFactory();

        // act
        Author author = factory.createAuthor(authorId, authorName);

        // assert
        assertEquals(authorName, author.getName());
        }
    }

    @Test
    void shouldThrowExceptionWhenAuthorNameIsNull() {
        //SUT
        AuthorFactory factory = new AuthorFactory();

        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor(null));
    }

    @Test
    void shouldThrowExceptionWhenConstructorWithAuthorIdHaveNameNull() {
        // arrange
        AuthorId authorId = mock(AuthorId.class);

        //SUT
        AuthorFactory factory = new AuthorFactory();

        // act & assert
        assertThrows(NullPointerException.class,
                () -> factory.createAuthor(authorId, null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // arrange
        AuthorId authorId = mock(AuthorId.class);
        String authorName = "  ";

        //SUT
        AuthorFactory factory = new AuthorFactory();

        // act & assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor(authorId, authorName));
    }
}
