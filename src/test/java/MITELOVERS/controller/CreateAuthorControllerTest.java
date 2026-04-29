package MITELOVERS.controller;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.Name;
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
    private Name _authorNameDouble;

    @BeforeEach
    void setUp() {

        _authorFactoryDouble = mock(AuthorFactory.class);

        _iAuthorRepoDouble = mock(IAuthorRepo.class);
        _authorDouble = mock(Author.class);
        _userIdDouble = mock(UserId.class);

        _authorNameDouble = mock(Name.class);

        when(_authorFactoryDouble.createAuthor(_authorNameDouble)).thenReturn(_authorDouble);
        when(_iAuthorRepoDouble.save(_authorDouble)).thenReturn(_authorDouble);

    }


    @Test
    void testCreateAuthorControllerConstructor() {

        // SUT & Act
        new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

    }

    @Test
    void shouldCreateAuthorSuccessfully() {

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        // Act
        Author result = controller.createAuthor(_authorNameDouble);

        // Assert
        assertEquals(_authorDouble, result);

    }

    @Test
    void shouldPassNameToFactoryWithoutModification() {

        // Arrange
        CreateAuthorController controller =
                new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("   Tolstói   ");

        // Act
        controller.createAuthor(nameDouble);

        // Assert
        verify(_authorFactoryDouble).createAuthor(nameDouble);
    }

    @Test
    void addAuthorShouldCreateAndSaveAuthor() {

        // Arrange & SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        // Act
        Author result = controller.addAuthor(_authorNameDouble);

        // Assert
        assertEquals(_authorDouble, result);
        verify(_authorFactoryDouble).createAuthor(_authorNameDouble);
        verify(_iAuthorRepoDouble).save(_authorDouble);

    }

    @Test
    void addAuthorShouldCallAuthorFactoryWithCorrectName() {

        // SUT
        CreateAuthorController controller = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble, _userIdDouble);

        // Act
        controller.addAuthor(_authorNameDouble);

        // Assert
        verify(_authorFactoryDouble).createAuthor(_authorNameDouble);

    }

}
