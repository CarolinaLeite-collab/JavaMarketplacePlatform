package MITELOVERS.controller;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(CreateAuthorController.class)
@ActiveProfiles("jpa")
class CreateAuthorControllerTest {

    @MockBean
    IAuthorRepo _iAuthorRepoDouble;

    @MockBean
    AuthorFactory _authorFactoryDouble;

    @Autowired
    CreateAuthorController _createAuthorController;

    private Author _authorDouble;
    private Name _nameDouble;


    @BeforeEach
    void setUp() {

        _authorDouble = mock(Author.class);

        _nameDouble = mock(Name.class);

        when(_authorFactoryDouble.createAuthor(_nameDouble)).thenReturn(_authorDouble);
        when(_iAuthorRepoDouble.save(_authorDouble)).thenReturn(_authorDouble);

    }


    @Test
    void testCreateAuthorControllerConstructor() {

        // SUT & Act
        _createAuthorController = new CreateAuthorController(_iAuthorRepoDouble, _authorFactoryDouble);
    }

    @Test
    void createAuthorShouldCreateAndSaveAuthor() {

        // Act
        Author result = _createAuthorController.createAuthor(_nameDouble);

        // Assert
        assertEquals(_authorDouble, result);
        verify(_authorFactoryDouble).createAuthor(_nameDouble);
        verify(_iAuthorRepoDouble).save(_authorDouble);

    }

}
