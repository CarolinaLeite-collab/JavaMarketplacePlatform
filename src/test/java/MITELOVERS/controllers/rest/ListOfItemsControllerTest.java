package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.ListOfItemsRequestDTO;
import MITELOVERS.dto.ListOfItemsResponseDTO;
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
class ListOfItemsControllerTest {
    //SUT
    @InjectMocks
    private ListOfItemsRestController controller;

    @Mock
    private ListOfItemsService service;

    @Test
    void getListsReturnsListOfItemsResponseDTOsByUserId() {
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
    void getListReturnsListOfItemsResponseDTOsByListId() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);

        when(service.getList("LOI-1234")).thenReturn(dtoDouble);

        //act
        ResponseEntity<Object> result = controller.getList("LOI-1234");

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
    void addItemsToListReturnsUpdatedListOfItemsResponseDTO() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);

        when(service.addItemToList("LOI-1234", "ABCDE1234")).thenReturn(dtoDouble);

        //act
        ResponseEntity<Object> result = controller.addItemsToList("LOI-1234", "ABCDE1234");

        //assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dtoDouble, result.getBody());
    }


    @Test
    void makeListPublicReturnsUpdatedListOfItemsResponseDTO() {
        //arrange
        ListOfItemsResponseDTO dtoDouble = mock(ListOfItemsResponseDTO.class);

        when(service.makePublic("LOI-1234", 2)).thenReturn(dtoDouble);

        //act
        ResponseEntity<Object> result = controller.makeListPublic("LOI-1234", 2);

        //assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dtoDouble, result.getBody());
    }
}