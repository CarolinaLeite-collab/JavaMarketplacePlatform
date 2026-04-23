package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePrivateListOfItemsControllerTest {

    private IListOfItemsRepo _iListOfItemsRepoDouble;
    private IGenreRepo _iGenreRepoDouble;
    private UserId _userIdDouble;
    private GenreId _genreIdDouble;
    private GenreId _genreId2Double;
    private Genre _genreDouble;
    private Genre  _genre2Double;

    @BeforeEach
    void setUp() {

        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _userIdDouble = mock(UserId.class);
        _genreIdDouble = mock(GenreId.class);
        _genreId2Double = mock(GenreId.class);
        _genreDouble = mock(Genre.class);
        _genre2Double = mock(Genre.class);

    }

    @Test
    void testCreatePrivateListOfItemsController(){

        // SUT & Act
        CreatePrivateListOfItemsController controller = new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        when(_iListOfItemsRepoDouble.addListOfItems(_userIdDouble, "My List", _genreIdDouble))
                .thenReturn(mock(ListOfItems.class));

        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act
        boolean result = controller.createListOfItems(_userIdDouble, "My List", _genreIdDouble);

        // Assert
        assertTrue(result);
        verify(_iListOfItemsRepoDouble).addListOfItems(_userIdDouble, "My List", _genreIdDouble);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(_iListOfItemsRepoDouble.addListOfItems(_userIdDouble, "My List", _genreIdDouble))
                .thenThrow(new IllegalArgumentException("List already exists"));

        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.createListOfItems(_userIdDouble, "My List", _genreIdDouble));
    }

    @Test
    void getListOfOfficialGenresDelegatesToRepo() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_genreDouble, _genre2Double));

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres();

        // Assert
        assertNotNull(result);
        verify(_iGenreRepoDouble).findAll();
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_genreDouble, _genre2Double));

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres();
        List<Genre> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        // Assert
        assertTrue(resultList.contains(_genreDouble));
        assertTrue(resultList.contains(_genre2Double));
    }
}
