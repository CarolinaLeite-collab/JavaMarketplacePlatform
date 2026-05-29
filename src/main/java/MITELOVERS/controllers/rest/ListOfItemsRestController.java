package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("my-lists")
public class ListOfItemsRestController {

    private final ListOfItemsService _listService;

    public ListOfItemsRestController(ListOfItemsService listService) {
        _listService = listService;
    }

    @GetMapping (path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLists(@RequestHeader("X-User-Id") String userId) {
        List<ListOfItemsResponseDTO> result = _listService.getUserLists(userId);

        for(ListOfItemsResponseDTO listOfItemsDTO : result) {
            String listId = listOfItemsDTO.getListId();

            Link link = linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel();

            listOfItemsDTO.add(link);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path ="/{listId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListById(@PathVariable String listId) {
        try {

            ListOfItemsResponseDTO result = _listService.getListById(listId);

            Link link = linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel();

            result.add(link);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAndSaveList (@RequestHeader("X-User-Id") String userId, @RequestBody ListOfItemsRequestDTO dto) {
        try {

            ListOfItemsResponseDTO result = _listService.save(userId, dto);

            String listId = result.getListId();

            Link link = linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel();

            result.add(link);

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/{listId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addItemToList(@PathVariable String listId, @RequestBody AddItemRequestDTO itemId) {
        try {
            ListOfItemsResponseDTO result = _listService.addItemToList(listId, itemId);

            Link link = linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel();

            result.add(link);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path = "/{listId}/visibility", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> makeListPublic(@PathVariable String listId, @RequestBody MakeListPublicRequestDTO durationDays) {
        try {
            ListOfItemsResponseDTO result = _listService.makePublic(listId, durationDays);

            Link link = linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel();

            result.add(link);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
