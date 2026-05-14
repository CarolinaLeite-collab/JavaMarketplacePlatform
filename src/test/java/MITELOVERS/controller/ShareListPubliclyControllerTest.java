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
        UserId otherUser = mock(UserId.class);
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        when(list1.getUserId()).thenReturn(_userIdDouble);
        when(list2.getUserId()).thenReturn(otherUser);
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of(list1, list2));

        // Act
        List<ListOfItems> result = _controller.getListOfLists(_userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
    }

    @Test
    void findListsByUserIdShouldThrowWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> _controller.findListsByUserId(null));
    }

    @Test
    void returnEmptyListWhenUserHasNoLists() {
        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<ListOfItems> result = _controller.getListOfLists(_userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shareListPubliclyShouldCallMakePublicAndSave() {
        // Arrange
        ListOfItemsId listIdDouble = mock(ListOfItemsId.class);
        ListOfItems listDouble = mock(ListOfItems.class);

        when(_iListOfItemsRepoDouble.ofIdentity(listIdDouble))
                .thenReturn(Optional.of(listDouble));

        // Act
        boolean result = _controller.shareListPublicly(listIdDouble, _durationDouble);

        // Assert
        assertTrue(result);
        verify(listDouble).makePublic(_durationDouble);
        verify(_iListOfItemsRepoDouble).save(listDouble);
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