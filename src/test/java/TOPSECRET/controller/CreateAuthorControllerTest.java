package TOPSECRET.controller;

import TOPSECRET.domain.IAuthorRepo;
import TOPSECRET.domain.valueobject.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAuthorControllerTest {

    private IAuthorRepo _iAuthorRepoDouble;

    @BeforeEach
    void setUp() {

        _iAuthorRepoDouble = mock(IAuthorRepo.class);

    }
    @Test
    void testCreateAuthorControllerConstructor() {}

    // SUT & Act
     CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble);


    @Test
    void shouldCreateAuthorWithValidName() {
        //arrange
        String name = "João";
        Author authorDouble = mock(Author.class);

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble);

        //act
        when(_iAuthorRepoDouble.createAuthor("João")).thenReturn(authorDouble);

        Author author = controller.createAuthor(name);

        //assert
        assertNotNull(author);
        assertEquals(authorDouble, author);
    }

    @Test
    void shouldTrimAuthorName() {
        //arrange
        String name = "João";
        Author authorDouble = mock(Author.class);

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble);

        //act
        when(_iAuthorRepoDouble.createAuthor(name)).thenReturn(authorDouble);
        when(authorDouble.getName()).thenReturn(name);

        Author author = controller.createAuthor("João  ");

        //assert
        assertEquals(authorDouble.getName(), author.getName());
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        //Arrange
        when(_iAuthorRepoDouble.createAuthor("Maria")).thenThrow(new IllegalStateException("Author already exists"));

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble);

        //Act
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> controller.createAuthor("Maria "));

        //Assert
        assertEquals("Author already exists", ex.getMessage());
    }

}
