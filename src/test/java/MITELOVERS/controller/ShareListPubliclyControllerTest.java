package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.SharedDuration;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class ShareListPubliclyControllerTest {

    @Mock
    IListOfItemsRepo _iListOfItemsRepoDouble;

    @Mock
    UserId _userIdDouble;

    @InjectMocks
    ShareListPubliclyController _controller;

    private SharedDuration _durationDouble;

    @BeforeEach
    void setUp() {
        _durationDouble = new SharedDuration(7);
    }

    @Test
    void returnListFromRepo() {
        // Arrange
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItemsId listIdDouble1 = mock(ListOfItemsId.class);

        when(_iListOfItemsRepoDouble.findListOfItemsByUserId(_userIdDouble)).thenReturn(List.of(list1));
        when(list1.identity()).thenReturn(listIdDouble1);

        // Act
        List<ListOfItemsId> result = _controller.findListsByUserId(_userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(listIdDouble1, result.get(0));
    }

    @Test
    void findListsByUserIdShouldThrowWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> _controller.findListsByUserId(null));
    }

    @Test
    void returnEmptyListWhenUserHasNoLists() {
        // Arrange
        when(_iListOfItemsRepoDouble.findListOfItemsByUserId(_userIdDouble)).thenReturn(List.of());

        // Act
        List<ListOfItemsId> result = _controller.findListsByUserId(_userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shareListPubliclyShouldCallMakePublicAndSave() {
        // Arrange
        ListOfItemsId listIdDouble = mock(ListOfItemsId.class);
        ListOfItems listDouble = mock(ListOfItems.class);

        when(_iListOfItemsRepoDouble.ofIdentity(listIdDouble)).thenReturn(Optional.of(listDouble));
        when(listDouble.isPrivate()).thenReturn(true);

        // Act
        boolean result = _controller.shareListPublicly(listIdDouble, _durationDouble);


        // Assert
        assertTrue(result);
        verify(listDouble).makePublic(_durationDouble);
        verify(_iListOfItemsRepoDouble).save(listDouble);
    }

    @Test
    void shareListPubliclyShouldThrowWhenAlreadyPublic() {
        // Arrange
        ListOfItemsId listIdDouble = mock(ListOfItemsId.class);
        ListOfItems listDouble = mock(ListOfItems.class);

        when(_iListOfItemsRepoDouble.ofIdentity(listIdDouble))
                .thenReturn(Optional.of(listDouble));

        when(listDouble.isPrivate()).thenReturn(false);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.shareListPublicly(listIdDouble, _durationDouble));
    }

    @Test
    void shareListPubliclyShouldThrowWhenListNotFound() {
        // Arrange
        ListOfItemsId listIdDouble = mock(ListOfItemsId.class);

        when(_iListOfItemsRepoDouble.ofIdentity(listIdDouble))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.shareListPublicly(listIdDouble, _durationDouble));
    }
}