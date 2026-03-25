package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorFactoryTest {
    @Test
    void shouldCreateAuthor() {
        // arrange
        String authorName = "João";
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class,
                             (mock, context) -> {
                                 when(mock.getName())
                                         .thenReturn("João");
                             })) {
            AuthorFactory factory = new AuthorFactory();
            // act
            Author author = factory.createAuthor(authorName);
            //assert
            assertEquals(authorName, author.getName());
        }
    }

    @Test
    void shouldThrowErrorWhenAuthorNameNull() {
        AuthorFactory factory = new AuthorFactory();

        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor(null));
    }
}
