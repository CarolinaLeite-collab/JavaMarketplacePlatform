package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.UserId;
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
        when(listA.getGenreId()).thenReturn(_genreIdDouble);
        when(listA.isPrivate()).thenReturn(false);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(listA));

        GetPublicListsByGenreController controller =
                new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("List A", result.get(0).getName());
        assertEquals(_userIdDouble, result.get(0).getUserId());
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {
        GetPublicListsByGenreController controller =
                new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.getPublicListsByGenre(null)
        );

        assertEquals("Genre is mandatory", ex.getMessage());
    }

    @Test
    void controllerShouldReturnEmptyListWhenNoPublicListsOfGenreExists() {
        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        GetPublicListsByGenreController controller =
                new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void controllerShouldIgnorePrivateLists() {
        // Arrange
        ListOfItems pub = mock(ListOfItems.class);
        ListOfItems priv = mock(ListOfItems.class);

        when(pub.getGenreId()).thenReturn(_genreIdDouble);
        when(pub.isPrivate()).thenReturn(false);

        when(priv.getGenreId()).thenReturn(_genreIdDouble);
        when(priv.isPrivate()).thenReturn(true);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(pub, priv));

        GetPublicListsByGenreController controller =
                new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(pub, result.get(0));
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void controllerShouldIgnoreListsOfDifferentGenre() {
        // Arrange
        GenreId otherGenre = mock(GenreId.class);

        ListOfItems list = mock(ListOfItems.class);
        when(list.getGenreId()).thenReturn(otherGenre);
        when(list.isPrivate()).thenReturn(false);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list));

        GetPublicListsByGenreController controller =
                new GetPublicListsByGenreController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
        verify(_iListOfItemsRepoDouble).findAll();
    }

}
