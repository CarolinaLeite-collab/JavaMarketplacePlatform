package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CreatePrivateListOfItemsControllerTest {

    private IListOfItemsRepo _iListOfItemsRepoDouble;
    private IGenreRepo _iGenreRepoDouble;
    private User _userDouble;
    private Genre _actionDouble;
    private Genre _poetryDouble;

    @BeforeEach
    void setUp() {

        _iListOfItemsRepoDouble = mock(IListOfItemsRepo.class);
        _iGenreRepoDouble = mock(IGenreRepo.class);
        _userDouble = mock(User.class);
        _actionDouble = mock(Genre.class);
        _poetryDouble = mock(Genre.class);

    }

    @Test
    void testCreatePrivateListOfItemsController(){

        // SUT & Act
        CreatePrivateListOfItemsController controller = new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userDouble);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfItems listDouble = mock(ListOfItems.class);
        when(_iListOfItemsRepoDouble.addListOfItems(_userDouble, "My List", _actionDouble)).thenReturn(listDouble);

        //SUT
        CreatePrivateListOfItemsController controller = new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        ListOfItems result = controller.createListOfItems(_userDouble, "My List", _actionDouble);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(listDouble, result)
        );
        verify(_iListOfItemsRepoDouble).addListOfItems(_userDouble, "My List", _actionDouble);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(_iListOfItemsRepoDouble.addListOfItems(_userDouble, "My List", _actionDouble))
                .thenThrow(new IllegalArgumentException("List already exists"));
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                controller.createListOfItems(_userDouble, "My List", _actionDouble)); // SUT

        // Assert
        assertNotNull(exception);
    }

    @Test
    void getListOfOfficialGenresDelegatesToRepo() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_actionDouble, _poetryDouble));

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres();

        // Assert
        assertNotNull(result);
        verify(_iGenreRepoDouble).findAll();
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_actionDouble, _poetryDouble));

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _userDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres();
        List<Genre> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        // Assert
        assertTrue(resultList.contains(_actionDouble));
        assertTrue(resultList.contains(_poetryDouble));
    }
}