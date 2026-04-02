package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IListOfPublicationsRepo;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPublicListsByGenreControllerTest {

    private IListOfPublicationsRepo _iListOfPublicationsRepoDouble;
    private Genre _genreDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {

        _iListOfPublicationsRepoDouble = mock(IListOfPublicationsRepo.class);
        _genreDouble = mock(Genre.class);
        _userDouble = mock(User.class);
    }

    @Test
    void controllerShouldReturnPublicListsByGenre() {

        // Arrange
        ListOfPublications listA = mock(ListOfPublications.class);

        when(listA.getName()).thenReturn("List A");
        when(listA.getUser()).thenReturn(_userDouble);
        when( _iListOfPublicationsRepoDouble.findPublicListsByGenre(_genreDouble)).thenReturn(List.of(listA));

        // SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController(_iListOfPublicationsRepoDouble, _userDouble);

        // Act
        List<ListOfPublications> result = controller.getPublicListsByGenre(_genreDouble);

        // Assert
        assertEquals("List A", result.get(0).getName());
        assertEquals(_userDouble, result.get(0).getUser());
        verify(_iListOfPublicationsRepoDouble).findPublicListsByGenre(_genreDouble);

    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {

        // Arrange
        //SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController(_iListOfPublicationsRepoDouble, _userDouble);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.getPublicListsByGenre(null)
        );
        assertEquals("Genre is mandatory", ex.getMessage());
    }

    @Test
    void controllerShouldReturnEmptyListWhenNoPublicListsOfGenreExists() {

        // Arrange
        when( _iListOfPublicationsRepoDouble.findPublicListsByGenre(_genreDouble)).thenReturn(List.of());

        // SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController(_iListOfPublicationsRepoDouble, _userDouble);

        // Act
        List<ListOfPublications> result = controller.getPublicListsByGenre(_genreDouble);

        // Assert
        assertTrue(result.isEmpty());
    }
}