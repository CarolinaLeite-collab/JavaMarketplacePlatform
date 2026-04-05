package TOPSECRET.controller;

import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPublicListsByGenreControllerTest {

    private IListOfItemsRepo _iListOfItemsRepoDouble;
    private GenreId _genreIdDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {

        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _genreIdDouble = mock(GenreId.class);
        _userIdDouble = mock(UserId.class);
    }

    @Test
    void controllerShouldReturnPublicListsByGenre() {

        // Arrange
        ListOfItems listA = mock(ListOfItems.class);

        when(listA.getName()).thenReturn("List A");
        when(listA.getUserId()).thenReturn(_userIdDouble);
        when( _iListOfItemsRepoDouble.findPublicListsByGenre(_genreIdDouble)).thenReturn(List.of(listA));

        // SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertEquals("List A", result.get(0).getName());
        assertEquals(_userIdDouble, result.get(0).getUserId());
        verify(_iListOfItemsRepoDouble).findPublicListsByGenre(_genreIdDouble);

    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {

        // Arrange
        //SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

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
        when( _iListOfItemsRepoDouble.findPublicListsByGenre(_genreIdDouble)).thenReturn(List.of());

        // SUT
        GetPublicListsByGenreController controller = new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }
}
