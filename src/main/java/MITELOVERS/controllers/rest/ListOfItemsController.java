package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.ListOfItemsRequestDTO;
import MITELOVERS.dto.ListOfItemsResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("my-lists")
public class ListOfItemsController {

    @Autowired
    private ListOfItemsService _listService;

    @GetMapping ("/")
    public ResponseEntity<Object> getLists(@RequestHeader String userId) {
        List<ListOfItemsResponseDTO> result = _listService.getUserLists(userId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{listId}")
    public ResponseEntity<Object> getList(@PathVariable String listId) {
        try {

            ListOfItemsResponseDTO result = _listService.getList(listId);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> createAndSaveList (@RequestHeader String userId, @RequestBody ListOfItemsRequestDTO dto) {
        try {

            ListOfItemsResponseDTO result = _listService.save(userId, dto);

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<Object> addItemsToList (@PathVariable String listId, @RequestBody String itemId) {
        try {
            ListOfItemsResponseDTO result = _listService.addItemToList(listId, itemId);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{listId}/visibility")
    public ResponseEntity<Object> makeListPublic(@PathVariable String listId, @RequestBody int sharedUntil) {
        try {
            ListOfItemsResponseDTO result = _listService.makePublic(listId, sharedUntil);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
