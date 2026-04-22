package MITELOVERS.controller;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreateAuthorControllerTest {

    private IAuthorRepo _iAuthorRepoDouble;
    private Author _authorDouble;
    private UserId _userIdDouble;
    private AuthorFactory _authorFactoryDouble;
    private String _authorName;

    @BeforeEach
    void setUp() {

        _authorFactoryDouble = mock(AuthorFactory.class);

        _iAuthorRepoDouble = mock(IAuthorRepo.class);
        _authorDouble = mock(Author.class);
        _userIdDouble = mock(UserId.class);

        _authorName = "Seneca";

        when(_authorFactoryDouble.createAuthor(_authorName)).thenReturn(_authorDouble);
        when(_iAuthorRepoDouble.save(_authorDouble)).thenReturn(_authorDouble);

    }


    @Test
    void testCreateAuthorControllerConstructor() {

        // SUT & Act
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

    }

    @Test
    void shouldCreateAuthorSuccessfully() {

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        // Act
        Author result = controller.createAuthor("Seneca");

        // Assert
        assertEquals(_authorDouble, result);

    }

    @Test
    void shouldTrimAuthorNameBeforeSaving() {

        // Arrange & SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        // Act
        controller.createAuthor("   Tolstói   ");

        // Assert
        verify(_authorFactoryDouble).createAuthor("Tolstói");

    }

    @Test
    void addAuthorShouldCreateAndSaveAuthor() {

        // Arrange & SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        // Act
        Author result = controller.addAuthor(_authorName);

        // Assert
        assertEquals(_authorDouble, result);
        verify(_authorFactoryDouble).createAuthor(_authorName);
        verify(_iAuthorRepoDouble).save(_authorDouble);

    }

    @Test
    void addAuthorShouldCallAuthorFactoryWithCorrectName() {

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        // Act
        controller.addAuthor(_authorName);

        // Assert
        verify(_authorFactoryDouble).createAuthor(_authorName);

    }

}
