package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOfItemsRestControllerTest {
    //SUT
    @InjectMocks
    private ListOfItemsRestController controller;

    @Mock
    private ListOfItemsService service;

    @Test
    void getListsReturnsListByIdOfItemsResponseDTOsByUserId() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);
        List<ListOfItemsResponseDTO> collection = List.of(dtoDouble);

        when(service.getUserLists("user@cenas.com")).thenReturn(collection);

        //act
        ResponseEntity<Object> result = controller.getLists("user@cenas.com");

        //assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(collection, result.getBody());
    }

    @Test
    void getListReturnsListOfItemsResponseDTOsByListByIdId() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);

        when(service.getListById("LOI-1234")).thenReturn(dtoDouble);

        //act
        ResponseEntity<Object> result = controller.getListById("LOI-1234");

        //assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dtoDouble, result.getBody());
    }

    @Test
    void createAndSaveListReturnsNewListOfItemsResponseDTO() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);
        ListOfItemsRequestDTO requestDTODouble = mock(ListOfItemsRequestDTO.class);

        when(service.save(eq("user@cenas.com"), any(ListOfItemsRequestDTO.class))).thenReturn(dtoDouble);

        //act
        ResponseEntity<Object> result = controller.createAndSaveList("user@cenas.com", requestDTODouble);

        //assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(dtoDouble, result.getBody());
    }

    @Test
    void addItemsToListReturnsUpdatedListOfItemResponseDTO() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);
        AddItemRequestDTO requestDTODouble = mock(AddItemRequestDTO.class);

        when(service.addItemToList(eq("LOI-1234"), any(AddItemRequestDTO.class))).thenReturn(dtoDouble);

        //act
        ResponseEntity<Object> result = controller.addItemToList("LOI-1234", requestDTODouble);

        //assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dtoDouble, result.getBody());
    }


    @Test
    void makeListPublicReturnsUpdatedListOfItemsResponseDTO() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);
        MakeListPublicRequestDTO sharedUntil = mock(MakeListPublicRequestDTO.class);

        when(service.makePublic("LOI-1234", sharedUntil)).thenReturn(dtoDouble);

        //act
        ResponseEntity<Object> result = controller.makeListPublic("LOI-1234", sharedUntil);

        //assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dtoDouble, result.getBody());
    }
}