package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class GetPublicListsByGenreControllerTest {

    @Mock
    IListOfItemsRepo _iListOfItemsRepoDouble;

    @Mock
    UserId _userIdDouble;

    @InjectMocks
    GetPublicListsByGenreController _controller;

    private GenreId _genreIdDouble;

    @BeforeEach
    void setUp() throws InstantiationException {
        _genreIdDouble = mock(GenreId.class);
    }

    @Test
    void controllerShouldReturnPublicListsByGenre() {
        // Arrange
        ListOfItems listA = mock(ListOfItems.class);
        when(listA.getName()).thenReturn(new Name("List A"));
        when(listA.getUserId()).thenReturn(_userIdDouble);
        when(listA.getGenreId()).thenReturn(_genreIdDouble);
        when(listA.isPrivate()).thenReturn(false);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(listA));

        // Act
        List<ListOfItems> result = _controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals(new Name("List A"), result.get(0).getName());
        assertEquals(_userIdDouble, result.get(0).getUserId());
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.getPublicListsByGenre(null)
        );
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
        verify(_iListOfItemsRepoDouble).findAll();
    }

    @Test
    void controllerShouldIgnorePrivateLists() {
        // Arrange
        ListOfItems pub = mock(ListOfItems.class);
        ListOfItems priv = mock(ListOfItems.class);

        when(pub.getGenreId()).thenReturn(_genreIdDouble);
        when(pub.isPrivate()).thenReturn(false);
        when(priv.isPrivate()).thenReturn(true);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(pub, priv));

        // Act
        List<ListOfItems> result = _controller.getPublicListsByGenre(_genreIdDouble);

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

        // Act
        List<ListOfItems> result = _controller.getPublicListsByGenre(_genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
        verify(_iListOfItemsRepoDouble).findAll();
    }

}