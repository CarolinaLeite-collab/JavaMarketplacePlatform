package TOPSECRET.controller;

import TOPSECRET.domain.IAuthorRepo;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAuthorControllerTest {

    private IAuthorRepo _iAuthorRepoDouble;
    private UserId _adminIdDouble;

    @BeforeEach
    void setUp() {

        _iAuthorRepoDouble = mock(IAuthorRepo.class);
        _adminIdDouble = mock(UserId.class);

    }
    @Test
    void testCreateAuthorControllerConstructor() {}

    // SUT & Act
    CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _adminIdDouble);


    @Test
    void shouldCreateAuthorWithValidName() {
        //arrange
        String name = "João";
        Author authorDouble = mock(Author.class);

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _adminIdDouble);

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

        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _adminIdDouble);

        //act
        when(_iAuthorRepoDouble.addAuthor(name)).thenReturn(authorDouble);
        when(authorDouble.getName()).thenReturn(name);

        Author author = controller.createAuthor("João  ");

        //assert
        assertEquals(authorDouble.getName(), author.getName());
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        //SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _adminIdDouble);

        //Act
        when(_iAuthorRepoDouble.addAuthor("Maria")).thenThrow(new IllegalStateException("Author already exists"));
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> controller.createAuthor("Maria "));

        //Assert
        assertEquals("Author already exists", ex.getMessage());
    }

}
