package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class GetPublicListsByGenreControllerTest {

    @Mock
    private IListOfItemsRepo _iListOfItemsRepoDouble;

    @Mock
    private ListOfItems _publicListOfItems;

    @Mock
    private ListOfItems _privateListOfItems;

    @Mock
    private IGenreRepo _iGenreRepoDouble;

    @Mock
    private GenreId _genreIdDouble;

    @Mock
    private GenreId _otherGenreIdDouble;

    @Mock
    private Name _nameDouble;

    @InjectMocks
    private GetPublicListsByGenreController _controller;



    @Test
    void controllerShouldReturnAllGenreKeys() {

        // Arrange
        when(_iGenreRepoDouble.findAllKeys()).thenReturn(List.of(_genreIdDouble));

        // Act
        Iterable<GenreId> result = _controller.findAllKeys();

        // Assert
        List<GenreId> resultList = new java.util.ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(1, resultList.size());
        assertEquals(_genreIdDouble, resultList.get(0));

    }

    @Test
    void controllerShouldReturnPublicListsByGenre() {

        // Arrange
        when(_nameDouble.toString()).thenReturn("List A");
        when(_publicListOfItems.getName()).thenReturn(_nameDouble);

        when(_publicListOfItems.getGenreId()).thenReturn(_genreIdDouble);
        when(_publicListOfItems.isPrivate()).thenReturn(false);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(_publicListOfItems));

        // Act
        List<ListOfItems> result = _controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("List A", result.get(0).getName().toString());

    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {

        //Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> _controller.getPublicListsByGenre(null));

        //Assert
        assertEquals("Genre is mandatory", ex.getMessage());

    }

    @Test
    void controllerShouldReturnEmptyListWhenNoPublicListsOfGenreExists() {

        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<ListOfItems> result = _controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void controllerShouldIgnorePrivateLists() {

        // Arrange
        when(_publicListOfItems.getGenreId()).thenReturn(_genreIdDouble);
        when(_publicListOfItems.isPrivate()).thenReturn(false);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(_publicListOfItems, _privateListOfItems));

        // Act
        List<ListOfItems> result = _controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(_publicListOfItems, result.get(0));

    }

    @Test
    void controllerShouldIgnoreListsOfDifferentGenre() {

        // Arrange
        when(_publicListOfItems.getGenreId()).thenReturn(_otherGenreIdDouble);
        when(_publicListOfItems.isPrivate()).thenReturn(false);

        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(_publicListOfItems));

        // Act
        List<ListOfItems> result = _controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());

    }

}
