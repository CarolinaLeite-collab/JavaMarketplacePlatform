package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ShareListPubliclyControllerTest {

    @MockBean
    IListOfItemsRepo _iListOfItemsRepoDouble;

    @MockBean
    UserId _userIdDouble;

    @InjectMocks
    ShareListPubliclyController _controller;

    @BeforeEach
    void setUp() throws InstantiationException {
        MockitoAnnotations.openMocks(this);
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

        // SUT
        ShareListPubliclyController controller =
                new ShareListPubliclyController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getListOfLists(_userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
    }

    @Test
    void findListsByUserIdShouldThrowWhenUserIdIsNull() {
        // SUT
        ShareListPubliclyController controller =
                new ShareListPubliclyController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findListsByUserId(null));
    }

    @Test
    void returnEmptyListWhenUserHasNoLists() {
        // Arrange
        when(_iListOfItemsRepoDouble.findAll()).thenReturn(List.of());

        // SUT
        ShareListPubliclyController controller =
                new ShareListPubliclyController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        List<ListOfItems> result = controller.getListOfLists(_userIdDouble);

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

        // SUT
        ShareListPubliclyController controller =
                new ShareListPubliclyController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act
        boolean result = controller.shareListPublicly(listIdDouble);

        // Assert
        assertTrue(result);
        verify(listDouble).makePublic();
        verify(_iListOfItemsRepoDouble).save(listDouble);
    }

    @Test
    void shareListPubliclyShouldThrowWhenListNotFound() {
        // Arrange
        ListOfItemsId listIdDouble = mock(ListOfItemsId.class);

        when(_iListOfItemsRepoDouble.ofIdentity(listIdDouble))
                .thenReturn(Optional.empty());

        // SUT
        ShareListPubliclyController controller =
                new ShareListPubliclyController(_iListOfItemsRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.shareListPublicly(listIdDouble));
    }

}