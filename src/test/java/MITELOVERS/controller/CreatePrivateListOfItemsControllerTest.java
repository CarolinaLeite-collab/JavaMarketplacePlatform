package MITELOVERS.controller;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CreatePrivateListOfItemsControllerTest {

    @MockBean
    IListOfItemsRepo _iListOfItemsRepoDouble;

    @MockBean
    IGenreRepo _iGenreRepoDouble;

    @MockBean
    ListOfItemsFactory _factoryDouble;

    @MockBean
    UserId _userIdDouble;

    @InjectMocks
    CreatePrivateListOfItemsController _controller;

    private GenreId _genreIdDouble;
    private Genre _genreDouble;
    private Genre _genre2Double;
    private ListOfItems _listOfItemsDouble;
    private Name _nameDouble;

    @BeforeEach
    void setUp() throws InstantiationException {
        MockitoAnnotations.openMocks(this);

        _genreIdDouble = mock(GenreId.class);
        _genreDouble = mock(Genre.class);
        _genre2Double = mock(Genre.class);
        _listOfItemsDouble = mock(ListOfItems.class);
        _nameDouble = new Name("My List");
    }

    @Test
    void testCreatePrivateListOfItemsController() {
        // SUT
        _controller = new CreatePrivateListOfItemsController(
                _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);
    }

    @Test
    void shouldCreateListSuccessfully() {
        // Arrange
        ListOfItemsId listOfItemsId = mock(ListOfItemsId.class);
        when(_listOfItemsDouble.identity()).thenReturn(listOfItemsId);
        when(_factoryDouble.createListOfItems(_userIdDouble, _nameDouble, _genreIdDouble))
                .thenReturn(_listOfItemsDouble);
        when(_iListOfItemsRepoDouble.containsOfIdentity(_listOfItemsDouble.identity()))
                .thenReturn(false);

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        // Act
        boolean result = controller.createListOfItems(_userIdDouble, _nameDouble, _genreIdDouble);

        // Assert
        assertTrue(result);
        verify(_factoryDouble).createListOfItems(_userIdDouble, _nameDouble, _genreIdDouble);
        verify(_iListOfItemsRepoDouble).save(_listOfItemsDouble);
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        when(_factoryDouble.createListOfItems(_userIdDouble, _nameDouble, _genreIdDouble))
                .thenReturn(_listOfItemsDouble);
        when(_iListOfItemsRepoDouble.containsOfIdentity(_listOfItemsDouble.identity()))
                .thenReturn(true);

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        // Act
        ListOfItems result = controller.addListOfItems(_userIdDouble, _nameDouble, _genreIdDouble);

        // Assert
        assertNull(result);
        verify(_iListOfItemsRepoDouble, never()).save(any());
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(_genreDouble, _genre2Double));

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres();
        List<Genre> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        // Assert
        assertTrue(resultList.contains(_genreDouble));
        assertTrue(resultList.contains(_genre2Double));
    }

    @Test
    void addListOfItemsShouldReturnNewListWhenNotDuplicate() {
        // Arrange
        ListOfItems newList = mock(ListOfItems.class);
        ListOfItemsId id = mock(ListOfItemsId.class);

        when(newList.identity()).thenReturn(id);
        when(_factoryDouble.createListOfItems(_userIdDouble, _nameDouble, _genreIdDouble))
                .thenReturn(newList);
        when(_iListOfItemsRepoDouble.containsOfIdentity(id)).thenReturn(false);

        // SUT
        CreatePrivateListOfItemsController controller =
                new CreatePrivateListOfItemsController(
                        _iListOfItemsRepoDouble, _iGenreRepoDouble, _factoryDouble, _userIdDouble);

        // Act
        ListOfItems result = controller.addListOfItems(_userIdDouble, _nameDouble, _genreIdDouble);

        // Assert
        assertSame(newList, result);
    }
}