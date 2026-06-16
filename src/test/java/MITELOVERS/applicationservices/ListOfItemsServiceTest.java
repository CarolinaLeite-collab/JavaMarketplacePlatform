package MITELOVERS.applicationservices;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOfItemsServiceTest {

    //SUT
    @InjectMocks
    private ListOfItemsService _service;

    @Mock
    private IListOfItemsRepo _listOfItemsRepoDouble;
    @Mock
    private ListOfItemsFactory _factoryDouble;
    @Mock
    private IItemRepo _itemRepoDouble;
    @Mock
    private IGenreRepo _genreRepoDouble;

    @Test
    void getUserListsReturnsListsByUserId() {
        //arrange
        ListOfItems listOfItemsDouble1 = mock(ListOfItems.class);
        ListOfItems listOfItemsDouble2 = mock(ListOfItems.class);
        UserId userIdDouble =  mock(UserId.class);

        when(_listOfItemsRepoDouble.findListOfItemsByUserId(any(UserId.class))).thenReturn(List.of(listOfItemsDouble1, listOfItemsDouble2));

        //act
        List<ListOfItems> result = _service.getUserLists(userIdDouble);

        //assert
        assertEquals(2, result.size());
        assertEquals(listOfItemsDouble1, result.get(0));
        assertEquals(listOfItemsDouble2, result.get(1));
    }

    @Test
    void getListReturnsListByListByIdId() {
        //arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);

        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));
        //act
        ListOfItems result = _service.getListById(listOfItemsIdDouble);

        //assert
        assertEquals(listOfItemsDouble, result);
    }

    @Test
    void saveReturnsNewlyCreatedAndSavedList() {
        //arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        UserId userIdDouble =  mock(UserId.class);
        Name nameDouble = mock(Name.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreRepoDouble.containsOfIdentity(any(GenreId.class))).thenReturn(true);
        when(_factoryDouble.createListOfItems(any(UserId.class), any(Name.class), any(GenreId.class))).thenReturn(listOfItemsDouble);
        when(_listOfItemsRepoDouble.save(any(ListOfItems.class))).thenReturn(listOfItemsDouble);

        //act
        ListOfItems result = _service.save(userIdDouble, nameDouble, genreIdDouble);

        //assert
        assertEquals(listOfItemsDouble, result);
    }

    @Test
    void addItemToListReturnsListWithAddedItem() {
        //arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);
        ItemId itemIdDouble = mock(ItemId.class);

        when(_itemRepoDouble.containsOfIdentity(any(ItemId.class))).thenReturn(true);
        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));

        //act
        ListOfItems result = _service.addItemToList(listOfItemsIdDouble, itemIdDouble);

        //assert
        assertEquals(listOfItemsDouble, result);

    }

    @Test
    void changeVisibilityShouldMakeListPublicIfPrivate() {
        //arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);
        SharedDuration sharedDurationDouble = mock(SharedDuration.class);

        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));

        //act
        ListOfItems result = _service.makePublic(listOfItemsIdDouble, sharedDurationDouble);

        //assert
        assertEquals(listOfItemsDouble, result);
    }

    @Test
    void changeVisibilityShouldMakeListPrivateIfPublic() {
        //arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);

        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));

        //act
        ListOfItems result = _service.makePrivate(listOfItemsIdDouble);

        //assert
        assertEquals(listOfItemsDouble, result);
    }

    @Test
    void findListsByGenreReturnsList() {
        // arrange
        ListOfItems listOfItemsDouble1 = mock(ListOfItems.class);
        ListOfItems listOfItemsDouble2 = mock(ListOfItems.class);
        ListOfItemsId listOfItemsIdDouble1 = mock(ListOfItemsId.class);
        ListOfItemsId listOfItemsIdDouble2 = mock(ListOfItemsId.class);

        GenreId genreIdDouble = mock(GenreId.class);

        when(listOfItemsDouble1.getGenreId()).thenReturn(genreIdDouble);
        when(listOfItemsDouble2.getGenreId()).thenReturn(genreIdDouble);

        when(_listOfItemsRepoDouble.findAll()).thenReturn(List.of(listOfItemsDouble1, listOfItemsDouble2));

        // act
        List<ListOfItems> result = _service.findByGenre(genreIdDouble);

        // assert
        assertEquals(2, result.size());
        assertEquals(listOfItemsDouble1, result.get(0));
        assertEquals(listOfItemsDouble2, result.get(1));
    }

    @Test
    void getPublicListsReturnsOnlyNonPrivateLists() {
        // arrange
        ListOfItems publicListDouble = mock(ListOfItems.class);
        ListOfItems privateListDouble = mock(ListOfItems.class);
        ListOfItems secondPublicListDouble = mock(ListOfItems.class);

        when(publicListDouble.isPrivate()).thenReturn(false);
        when(privateListDouble.isPrivate()).thenReturn(true);
        when(secondPublicListDouble.isPrivate()).thenReturn(false);
        when(_listOfItemsRepoDouble.findAll()).thenReturn(List.of(publicListDouble, privateListDouble, secondPublicListDouble));

        // act
        List<ListOfItems> result = _service.getPublicLists();

        // assert
        assertEquals(2, result.size());
        assertEquals(publicListDouble, result.get(0));
        assertEquals(secondPublicListDouble, result.get(1));
    }

    @Test
    void getItemsInPublicListReturnsItemIdsWhenListIsPublic() {
        // arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);
        ItemId itemIdDouble1 = mock(ItemId.class);
        ItemId itemIdDouble2 = mock(ItemId.class);

        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));
        when(listOfItemsDouble.isPrivate()).thenReturn(false);
        when(listOfItemsDouble.getItemIds()).thenReturn(List.of(itemIdDouble1, itemIdDouble2));

        // act
        List<ItemId> result = _service.getItemsInPublicList(listOfItemsIdDouble);

        // assert
        assertEquals(2, result.size());
        assertEquals(itemIdDouble1, result.get(0));
        assertEquals(itemIdDouble2, result.get(1));
    }

    @Test
    void getItemsInPublicListThrowsWhenListIsPrivate() {
        // arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);

        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));
        when(listOfItemsDouble.isPrivate()).thenReturn(true);

        // act + assert
        assertThrows(IllegalStateException.class, () -> _service.getItemsInPublicList(listOfItemsIdDouble));
    }

    @Test
    void getItemsInPublicListThrowsWhenListDoesNotExist() {
        // arrange
        ListOfItemsId listOfItemsIdDouble = mock(ListOfItemsId.class);

        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.empty());

        // act + assert
        assertThrows(IllegalArgumentException.class, () -> _service.getItemsInPublicList(listOfItemsIdDouble));
    }
}
