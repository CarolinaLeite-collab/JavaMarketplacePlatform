package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuthorRepo;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.author.Author;
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
    User _userDouble = mock(User.class);
    CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userDouble);


    @Test
    void shouldCreateAuthorWithValidName() {
        //arrange
        String name = "João";
        Author authorDouble = mock(Author.class);
        User _userDouble = mock(User.class);

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userDouble);

        //act
        when(_iAuthorRepoDouble.addAuthor("João")).thenReturn(authorDouble);

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
        User _userDouble = mock(User.class);

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userDouble);

        //act
        when(_iAuthorRepoDouble.addAuthor(name)).thenReturn(authorDouble);
        when(authorDouble.getName()).thenReturn(name);

        Author author = controller.createAuthor("João  ");

        //assert
        assertEquals(authorDouble.getName(), author.getName());
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        //Arrange
        User _userDouble = mock(User.class);

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _userDouble);

        //Act
        when(_iAuthorRepoDouble.addAuthor("Maria")).thenThrow(new IllegalStateException("Author already exists"));
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> controller.createAuthor("Maria "));

        //Assert
        assertEquals("Author already exists", ex.getMessage());
    }

}
