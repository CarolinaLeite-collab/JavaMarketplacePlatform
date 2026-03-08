package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPublicListsByGenreControllerTest {

    private GetPublicListsByGenreController _controller;
    private ListOfPublicationsRepo _repoDouble;

    private Genre _genreDouble;
    private User _userDouble;

    @BeforeEach
    void setUp() {

        _repoDouble = mock(ListOfPublicationsRepo.class);
        _controller = new GetPublicListsByGenreController(_repoDouble);

        _genreDouble = mock(Genre.class);
        _userDouble = mock(User.class);
    }

    @Test
    void controllerShouldReturnPublicListsByGenre() {

        //Arrange
        ListOfPublications _listA = mock(ListOfPublications.class);
        when(_listA.getName()).thenReturn("List A");
        when(_listA.getUser()).thenReturn(_userDouble);
        when(_repoDouble.findPublicListsByGenre(_genreDouble)).thenReturn(List.of(_listA));

        //At
        List<ListOfPublications> result = _controller.getPublicListsByGenre(_genreDouble); //SUT

        //Assert
        assertEquals("List A", result.get(0).getName());
        assertEquals(_userDouble, result.get(0).getUser());
        verify(_repoDouble).findPublicListsByGenre(_genreDouble);

    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {
        //Arrange
        //(no additional setup needed beyond setup)


        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.getPublicListsByGenre(null) //(SUT)
        );
        assertEquals("Genre is mandatory", ex.getMessage());
    }

    @Test
    void controllerShouldReturnEmptyListWhenNoPublicListsOfGenreExists() {

        //Arrange
        when(_repoDouble.findPublicListsByGenre(_genreDouble)).thenReturn(List.of());

        //Act
        List<ListOfPublications> result = _controller.getPublicListsByGenre(_genreDouble); //SUT

        //Assert
        assertTrue(result.isEmpty());
    }
}