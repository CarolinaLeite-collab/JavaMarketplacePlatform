package MITELOVERS.applicationservices;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private IUserRepo _userRepoDouble;
    @Mock
    private ListOfItemsResponseDTOMapper _mapperDouble;

    @Test
    void getUserListsReturnsListsByUserId() {
        //arrange
        ListOfItems listOfItemsDouble1 = mock(ListOfItems.class);
        ListOfItems listOfItemsDouble2 = mock(ListOfItems.class);

        ListOfItemsResponseDTO dto1 = mock(ListOfItemsResponseDTO.class);
        ListOfItemsResponseDTO dto2 = mock(ListOfItemsResponseDTO.class);

        when(_listOfItemsRepoDouble.findListOfItemsByUserId(any(UserId.class))).thenReturn(List.of(listOfItemsDouble1, listOfItemsDouble2));
        when(_mapperDouble.toModel(listOfItemsDouble1)).thenReturn(dto1);
        when(_mapperDouble.toModel(listOfItemsDouble2)).thenReturn(dto2);

        //act
        List<ListOfItemsResponseDTO> result = _service.getUserLists("user@cenas.com");

        //assert
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void getListReturnsListByListByIdId() {
        //arrange
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        ListOfItemsResponseDTO dto = mock(ListOfItemsResponseDTO.class);

        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));
        when(_mapperDouble.toModel(listOfItemsDouble)).thenReturn(dto);

        //act
        ListOfItemsResponseDTO result = _service.getListById("LOI-1234");

        //assert
        assertEquals(dto, result);
    }

    @Test
    void saveReturnsNewlyCreatedAndSavedList() {
        //arrange
        ListOfItemsResponseDTO responseDTODouble = mock(ListOfItemsResponseDTO.class);
        ListOfItemsRequestDTO requestDTODouble = mock(ListOfItemsRequestDTO.class);
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);

        when(requestDTODouble.getName()).thenReturn("nameDouble");
        when(requestDTODouble.getGenreId()).thenReturn("FICTION");
        when(_genreRepoDouble.containsOfIdentity(any(GenreId.class))).thenReturn(true);
        when(requestDTODouble.getName()).thenReturn("nameDouble");
        when(_factoryDouble.createListOfItems(any(UserId.class), any(Name.class), any(GenreId.class))).thenReturn(listOfItemsDouble);
        when(_listOfItemsRepoDouble.save(any(ListOfItems.class))).thenReturn(listOfItemsDouble);
        when(_mapperDouble.toModel(listOfItemsDouble)).thenReturn(responseDTODouble);

        //act
        ListOfItemsResponseDTO result = _service.save("user@cenas.com", requestDTODouble);

        //assert
        assertEquals(responseDTODouble, result);
    }

    @Test
    void addItemToListReturnsListWithAddedItem() {
        //arrange
        ListOfItemsResponseDTO responseDTODouble = mock(ListOfItemsResponseDTO.class);
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        AddItemRequestDTO requestDTODouble = mock(AddItemRequestDTO.class);

        when(requestDTODouble.getItemId()).thenReturn("ABCDEF1234");
        when(_itemRepoDouble.containsOfIdentity(any(ItemId.class))).thenReturn(true);
        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));
        when(_mapperDouble.toModel(listOfItemsDouble)).thenReturn(responseDTODouble);

        //act
        ListOfItemsResponseDTO result = _service.addItemToList("LOI-1234", requestDTODouble);

        //assert
        assertEquals(responseDTODouble, result);

    }

    @Test
    void makePublicReturnsPublicList() {
        //arrange
        ListOfItemsResponseDTO responseDTODouble = mock(ListOfItemsResponseDTO.class);
        ListOfItems listOfItemsDouble = mock(ListOfItems.class);
        MakeListPublicRequestDTO sharedUntil = mock(MakeListPublicRequestDTO.class);

        when(sharedUntil.getSharedUntil()).thenReturn(2);
        when(_listOfItemsRepoDouble.ofIdentity(any(ListOfItemsId.class))).thenReturn(Optional.of(listOfItemsDouble));
        when(_mapperDouble.toModel(listOfItemsDouble)).thenReturn(responseDTODouble);

        //act
        ListOfItemsResponseDTO result = _service.makePublic("LOI-1234", sharedUntil);

        //assert
        assertEquals(responseDTODouble, result);
    }

    @Test
    void findListsByGenreReturnsList() {
        // arrange
        ListOfItemsResponseDTO responseDTODouble1 = mock(ListOfItemsResponseDTO.class);
        ListOfItemsResponseDTO responseDTODouble2 = mock(ListOfItemsResponseDTO.class);
        ListOfItems listOfItemsDouble1 = mock(ListOfItems.class);
        ListOfItems listOfItemsDouble2 = mock(ListOfItems.class);

        when(listOfItemsDouble1.getGenreId()).thenReturn(new GenreId("FICTION"));
        when(listOfItemsDouble2.getGenreId()).thenReturn(new GenreId("FICTION"));

        when(_listOfItemsRepoDouble.findAll()).thenReturn(List.of(listOfItemsDouble1, listOfItemsDouble2));

        when(_mapperDouble.toModel(listOfItemsDouble1)).thenReturn(responseDTODouble1);
        when(_mapperDouble.toModel(listOfItemsDouble2)).thenReturn(responseDTODouble2);

        // act
        List<ListOfItemsResponseDTO> result = _service.findByGenre("FICTION");

        // assert
        assertEquals(2, result.size());
        assertEquals(responseDTODouble1, result.get(0));
        assertEquals(responseDTODouble2, result.get(1));
    }
}