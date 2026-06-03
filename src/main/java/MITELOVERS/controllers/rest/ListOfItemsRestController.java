package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing List of Items-related endpoints
 * via HTTP endpoints.
 */

@RestController
@RequestMapping("my-lists")
public class ListOfItemsRestController {

    private final ListOfItemsService _listService;
    private ListOfItemsResponseDTOMapper _mapper;
    private final ListOfItemsLinkProvider _listOfItemsLinkProvider;
    private final UserService _userService;

    public ListOfItemsRestController(ListOfItemsService listService,
                                     ListOfItemsResponseDTOMapper mapper,
                                     ListOfItemsLinkProvider listOfItemsLinkProvider,
                                     UserService userService) {

        _listService = listService;
        _mapper = mapper;
        _listOfItemsLinkProvider = listOfItemsLinkProvider;
        _userService = userService;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();

        _listOfItemsLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);
    }

    @GetMapping (path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLists(@RequestHeader("X-User-Id") String userId) {
        Email email = new Email(userId);
        UserId recUserId = new UserId(email);

        List<ListOfItems> listOfLists = _listService.getUserLists(recUserId);

        List<ListOfItemsResponseDTO> resultDTO = new ArrayList<>();

        for(ListOfItems listOfItems : listOfLists) {
            ListOfItemsResponseDTO listDTO = _mapper.toModel(listOfItems);
            String listId = listDTO.getListId();
            listDTO.add(linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel());
            listDTO.add(linkTo(methodOn(ListOfItemsRestController.class).addItemToList(listId, null)).withRel("add-item"));

            if (listOfItems.isPrivate()) {
                listDTO.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPublic(listId, null)).withRel("make-public"));
            }
            else {
                listDTO.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPrivate(listId)).withRel("make-private"));
            }
            listDTO.add(linkTo(methodOn(ListOfItemsRestController.class).deleteList(listId)).withRel("delete"));

            resultDTO.add(listDTO);
        }

        CollectionModel<ListOfItemsResponseDTO> result = CollectionModel.of(resultDTO);

        result.add(linkTo(methodOn(ListOfItemsRestController.class).createAndSaveList(userId, null)).withRel("create-list"));
        result.add(linkTo(methodOn(ListOfItemsRestController.class).getLists(userId)).withSelfRel());


        if (resultDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path ="/{listId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListById(@PathVariable String listId) {
        try {
            ListOfItemsId recListId = new ListOfItemsId(listId);

            ListOfItems list = _listService.getListById(recListId);

            ListOfItemsResponseDTO result = _mapper.toModel(list);

            result.add(linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel());
            result.add(linkTo(methodOn(ListOfItemsRestController.class).getLists(result.getUserId())).withRel("collection"));
            result.add(linkTo(methodOn(ListOfItemsRestController.class).addItemToList(listId, null)).withRel("add-item"));
            if (result.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPublic(listId, null)).withRel("make-public"));
            }
            else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPrivate(listId)).withRel("make-private"));
            }
            result.add(linkTo(methodOn(ListOfItemsRestController.class).deleteList(listId)).withRel("delete"));


            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAndSaveList (@RequestHeader("X-User-Id") String userId, @RequestBody ListOfItemsRequestDTO dto) {
        try {
            Email email = new Email(userId);
            UserId recUserId = new UserId(email);
            Name name =  new Name(dto.getName());
            GenreId genreId = new GenreId(dto.getGenreId());

            ListOfItems list = _listService.save(recUserId, name , genreId);

            ListOfItemsResponseDTO result = _mapper.toModel(list);
            String listId = result.getListId();

            result.add(linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel());
            result.add(linkTo(methodOn(ListOfItemsRestController.class).getLists(result.getUserId())).withRel("collection"));
            result.add(linkTo(methodOn(ListOfItemsRestController.class).addItemToList(listId, null)).withRel("add-item"));
            if (result.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPublic(listId, null)).withRel("make-public"));
            }
            else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPrivate(listId)).withRel("make-private"));
            }
            result.add(linkTo(methodOn(ListOfItemsRestController.class).deleteList(listId)).withRel("delete"));


            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/{listId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addItemToList(@PathVariable String listId, @RequestBody AddItemRequestDTO itemId) {
        try {
            ItemId recItemId = new ItemId(itemId.getItemId());
            ListOfItemsId recListId = new ListOfItemsId(listId);

            ListOfItems list = _listService.addItemToList(recListId, recItemId);

            ListOfItemsResponseDTO result = _mapper.toModel(list);

            result.add(linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel());
            result.add(linkTo(methodOn(ListOfItemsRestController.class).getLists(result.getUserId())).withRel("collection"));
            result.add(linkTo(methodOn(ListOfItemsRestController.class).addItemToList(listId, null)).withRel("add-item"));
            if (result.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPublic(listId, null)).withRel("make-public"));
            }
            else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPrivate(listId)).withRel("make-private"));
            }
            result.add(linkTo(methodOn(ListOfItemsRestController.class).deleteList(listId)).withRel("delete"));


            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path = "/{listId}/visibility", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> makeListPublic(@PathVariable String listId, @RequestBody MakeListPublicRequestDTO durationDays) {
        try {
            ListOfItemsId recListId = new ListOfItemsId(listId);
            SharedDuration sharedDuration = new SharedDuration(durationDays.getSharedUntil());

            ListOfItems list = _listService.makePublic(recListId, sharedDuration);

            ListOfItemsResponseDTO result = _mapper.toModel(list);

            result.add(linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel());
            result.add(linkTo(methodOn(ListOfItemsRestController.class).getLists(result.getUserId())).withRel("collection"));
            result.add(linkTo(methodOn(ListOfItemsRestController.class).addItemToList(listId, null)).withRel("add-item"));
            if (result.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPublic(listId, null)).withRel("make-public"));
            }
            else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPrivate(listId)).withRel("make-private"));
            }
            result.add(linkTo(methodOn(ListOfItemsRestController.class).deleteList(listId)).withRel("delete"));

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path = "/{listId}/visibility", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> makeListPrivate(@PathVariable String listId) {
        try {
            ListOfItemsId recListId = new ListOfItemsId(listId);

            ListOfItems list = _listService.makePrivate(recListId);

            ListOfItemsResponseDTO result = _mapper.toModel(list);

            result.add(linkTo(methodOn(ListOfItemsRestController.class).getListById(listId)).withSelfRel());
            result.add(linkTo(methodOn(ListOfItemsRestController.class).getLists(result.getUserId())).withRel("collection"));
            result.add(linkTo(methodOn(ListOfItemsRestController.class).addItemToList(listId, null)).withRel("add-item"));
            if (result.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPublic(listId, null)).withRel("make-public"));
            }
            else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class).makeListPrivate(listId)).withRel("make-private"));
            }
            result.add(linkTo(methodOn(ListOfItemsRestController.class).deleteList(listId)).withRel("delete"));



            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path ="/{listId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteList(@PathVariable String listId) {
        try {
            ListOfItemsId recListId = new ListOfItemsId(listId);

            _listService.deleteList(recListId);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
