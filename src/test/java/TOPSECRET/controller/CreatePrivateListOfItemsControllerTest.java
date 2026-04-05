package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.IGenreRepo;
import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfItems listDouble = mock(ListOfItems.class);
        when(_iListOfItemsRepoDouble.addListOfItems(_userIdDouble, "My List", _genreIdDouble)).thenReturn(listDouble);

        //SUT
        CreatePrivateListOfItemsController controller = new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act
        ListOfItems result = controller.createListOfItems(_userIdDouble, "My List", _genreIdDouble);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(listDouble, result)
        );
        verify(_iListOfItemsRepoDouble).addListOfItems(_userIdDouble, "My List", _genreIdDouble);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(_iListOfItemsRepoDouble.addListOfItems(_userIdDouble, "My List", _genreIdDouble)).thenReturn(null);

        //SUT
        CreatePrivateListOfItemsController controller = new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act
        ListOfItems duplicate = controller.createListOfItems(_userIdDouble, "My List", _genreIdDouble);

        // Assert
        assertNull(duplicate);
        verify(_iListOfItemsRepoDouble).addListOfItems(_userIdDouble, "My List", _genreIdDouble);
    }

    @Test
    void getListOfOfficialGenresReturnsUnmodifiableList() {
        // Arrange
        when(_iGenreRepoDouble.getListOfOfficialGenres()).thenReturn(List.of(_genreDouble, _genre2Double));

        //SUT
        CreatePrivateListOfItemsController controller = new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act
        List<Genre> officialGenres = controller.getListOfOfficialGenres();

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> officialGenres.add(mock(Genre.class)));
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        // Arrange
        when(_iGenreRepoDouble.getListOfOfficialGenres()).thenReturn(List.of(_genreDouble, _genre2Double));

        //SUT
        CreatePrivateListOfItemsController controller = new CreatePrivateListOfItemsController(_iListOfItemsRepoDouble, _iGenreRepoDouble, _userIdDouble);

        // Act
        List<Genre> officialGenres = controller.getListOfOfficialGenres();

        // Assert
        assertAll(
                () -> assertEquals(2, officialGenres.size()),
                () -> assertTrue(officialGenres.contains(_genreDouble)),
                () -> assertTrue(officialGenres.contains(_genre2Double))
        );
    }
}
