package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddItemToListControllerTest {

    static final String ITEM_ALREADY_IN_LIST = "Item already in list";
    static final String LIST_DOES_NOT_EXIST = "List does not exist";
    static final String VALID_USER_ID = "user@cenas.com";
    static final String VALID_LIST_ID = "LOI-12345";
    static final String VALID_ITEM_ID = "3F9F4BFAB5";
    static final String VALID_GENRE_ID = "FICTION";

    @Mock
    ListOfItemsService _service;

    @Mock
    ListOfItemsResponseDTOMapper _mapper;

    @InjectMocks
    AddItemToListController _controller;

    @Test
    void getMyListsShouldReturnOnlyListsBelongingToUser() {
        // Arrange
        ListOfItems list1 = mock(ListOfItems.class);
        ListOfItems list2 = mock(ListOfItems.class);

        when(_service.getUserLists(any(UserId.class))).thenReturn(List.of(list1, list2));

        // Act
        List<ListOfItems> result = _controller.getMyLists(VALID_USER_ID);

        // Assert
        assertEquals(2, result.size());
        assertSame(list1, result.get(0));
        assertSame(list2, result.get(1));
    }

    @Test
    void getMyListsShouldReturnEmptyListWhenUserHasNoLists() {
        // Arrange
        when(_service.getUserLists(any(UserId.class))).thenReturn(List.of());

        // Act
        List<ListOfItems> result = _controller.getMyLists(VALID_USER_ID);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addItemToListShouldSucceedWithValidArguments() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);
        ListOfItems expected = mock(ListOfItems.class);

        when(dto.getItemId()).thenReturn(VALID_ITEM_ID);
        when(_service.addItemToList(any(ListOfItemsId.class), any(ItemId.class))).thenReturn(expected);

        // Act
        ListOfItems result = _controller.addItemToList(VALID_LIST_ID, dto);

        // Assert
        assertNotNull(result);
        assertSame(expected, result);
    }

    @Test
    void addItemToListShouldThrowWhenItemIdIsNull() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);
        when(dto.getItemId()).thenReturn(null);

        // Act + Assert
        assertThrows(Exception.class, () -> _controller.addItemToList(VALID_LIST_ID, dto));
    }

    @Test
    void addItemToListShouldThrowWhenItemAlreadyInList() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);
        when(dto.getItemId()).thenReturn(VALID_ITEM_ID);

        when(_service.addItemToList(any(ListOfItemsId.class), any(ItemId.class)))
                .thenThrow(new IllegalStateException(ITEM_ALREADY_IN_LIST));

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList(VALID_LIST_ID, dto));

        // Assert
        assertEquals(ITEM_ALREADY_IN_LIST, ex.getMessage());
    }

    @Test
    void addItemToListShouldThrowWhenListDoesNotExist() {
        // Arrange
        AddItemRequestDTO dto = mock(AddItemRequestDTO.class);

        when(dto.getItemId()).thenReturn(VALID_ITEM_ID);
        when(_service.addItemToList(any(ListOfItemsId.class), any(ItemId.class)))
                .thenThrow(new IllegalStateException(LIST_DOES_NOT_EXIST));

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> _controller.addItemToList(VALID_LIST_ID, dto));

        // Assert
        assertEquals(LIST_DOES_NOT_EXIST, ex.getMessage());
    }

    @Test
    void findByGenreShouldReturnListsWhenGenreHasResults() {
        // Arrange
        ListOfItems list1 = mock(ListOfItems.class);

        when(_service.findByGenre(any(GenreId.class))).thenReturn(List.of(list1));

        // Act
        List<ListOfItems> result = _controller.findByGenre(VALID_GENRE_ID);

        // Assert
        assertEquals(1, result.size());
        assertSame(list1, result.get(0));
    }

    @Test
    void findByGenreShouldReturnEmptyWhenThereIsNoListByGenreId() {
        // Arrange
        when(_service.findByGenre(any(GenreId.class))).thenReturn(List.of());

        // Act
        List<ListOfItems> result = _controller.findByGenre("NON-FICTION");

        // Assert
        assertTrue(result.isEmpty());
    }
}