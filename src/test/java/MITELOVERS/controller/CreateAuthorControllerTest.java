package MITELOVERS.controller;

import MITELOVERS.controllers.cli.CreateAuthorController;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class CreateAuthorControllerTest {

    @Mock
    IAuthorRepo _iAuthorRepoDouble;

    @Mock
    AuthorFactory _authorFactoryDouble;

    @InjectMocks
    CreateAuthorController _createAuthorController;


    @Test
    void testCreateAuthorControllerConstructor() {

        // SUT & Act
        _createAuthorController = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble);
    }

    @Test
    void createAuthorShouldCreateAndSaveAuthor() {

        // Arrange
        Author authorDouble = mock(Author.class);
        Name nameDouble = mock(Name.class);

        when(_authorFactoryDouble.createAuthor(nameDouble)).thenReturn(authorDouble);
        when(_iAuthorRepoDouble.save(authorDouble)).thenReturn(authorDouble);

        // Act
        Author result = _createAuthorController.createAuthor(nameDouble);

        // Assert
        assertEquals(authorDouble, result);
        verify(_authorFactoryDouble).createAuthor(nameDouble);
        verify(_iAuthorRepoDouble).save(authorDouble);

    }

}
