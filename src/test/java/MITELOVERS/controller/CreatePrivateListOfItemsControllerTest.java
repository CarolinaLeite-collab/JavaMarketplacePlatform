package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
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
    private ListOfItems _listOfItemsDouble;
    private ListOfItemsFactory _factoryDouble;

    @BeforeEach
    void setUp() {

        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _userIdDouble = mock(UserId.class);
        _genreIdDouble = mock(GenreId.class);
        _genreId2Double = mock(GenreId.class);
        _genreDouble = mock(Genre.class);
        _genre2Double = mock(Genre.class);
        _listOfItemsDouble = mock(ListOfItems.class);
        _factoryDouble = mock(ListOfItemsFactory.class);

    }

    @Test
    void testCreatePrivateListOfItemsController() {
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        assertNotNull(controller);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfItemsId listOfItemsId = mock(ListOfItemsId.class);

        when(_listOfItemsDouble.identity()).thenReturn(listOfItemsId);

        when(_factoryDouble.createListOfItems(_userIdDouble, "My List", _genreIdDouble))
                .thenReturn(_listOfItemsDouble);

        when(_iListOfItemsRepoDouble.containsOfIdentity(_listOfItemsDouble.identity()))
                .thenReturn(false);

        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        // Act
        boolean result = controller.createListOfItems(_userIdDouble, "My List", _genreIdDouble);

        // Assert
        assertTrue(result);
        verify(_factoryDouble).createListOfItems(_userIdDouble, "My List", _genreIdDouble);
        verify(_iListOfItemsRepoDouble).save(_listOfItemsDouble);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(_factoryDouble.createListOfItems(_userIdDouble, "My List", _genreIdDouble))
                .thenReturn(_listOfItemsDouble);

        when(_iListOfItemsRepoDouble.containsOfIdentity(_listOfItemsDouble.identity()))
                .thenReturn(true); // duplicate

        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        // Act
        ListOfItems result = controller.addListOfItems(_userIdDouble, "My List", _genreIdDouble);

        // Assert
        assertNull(result);
        verify(_iListOfItemsRepoDouble, never()).save(any());
    }

    @Test
    void getListOfOfficialGenresDelegatesToRepo() {
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_genreDouble, _genre2Double));

        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        Iterable<Genre> result = controller.getListOfOfficialGenres();

        assertNotNull(result);
        verify(_iGenreRepoDouble).findAll();
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_genreDouble, _genre2Double));

        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        Iterable<Genre> result = controller.getListOfOfficialGenres();
        List<Genre> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertTrue(resultList.contains(_genreDouble));
        assertTrue(resultList.contains(_genre2Double));
    }

}
