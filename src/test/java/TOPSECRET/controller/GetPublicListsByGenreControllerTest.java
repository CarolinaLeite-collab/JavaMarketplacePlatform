package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.IListOfPublicationsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPublicListsByGenreControllerTest {

    private IListOfPublicationsRepo _iRepoDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _iRepoDouble = mock(IListOfPublicationsRepo.class);
        _genreDouble = mock(Genre.class);
    }

    @Test
    void controllerShouldReturnPublicListsByGenre() {

        // Arrange
        ListOfPublications listA = mock(ListOfPublications.class);
        User userDouble = mock(User.class);
        when(listA.getName()).thenReturn("List A");
        when(listA.getUser()).thenReturn(userDouble);
        when( _iRepoDouble.findPublicListsByGenre(_genreDouble)).thenReturn(List.of(listA));
        // SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController(_iRepoDouble);

        // Act
        List<ListOfPublications> result = controller.getPublicListsByGenre(_genreDouble);

        // Assert
        assertEquals("List A", result.get(0).getName());
        assertEquals(userDouble, result.get(0).getUser());
        verify(_iRepoDouble).findPublicListsByGenre(_genreDouble);

    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {

        // Arrange
        //SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController( _iRepoDouble);

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
        when( _iRepoDouble.findPublicListsByGenre(_genreDouble)).thenReturn(List.of());
        // SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController( _iRepoDouble);

        // Act
        List<ListOfPublications> result = controller.getPublicListsByGenre(_genreDouble);

        // Assert
        assertTrue(result.isEmpty());
    }
}