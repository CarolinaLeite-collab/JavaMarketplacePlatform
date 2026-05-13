package MITELOVERS.domain.author;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorFactoryTest {
    @Test
    void shouldCreateAuthor() {
        //arrange
        Name authorName = mock(Name.class);

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
        Name authorName = mock(Name.class);

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
}
