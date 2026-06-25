package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
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
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final AuthorizationPolicy _auth;

    public ListOfItemsRestController(ListOfItemsService listService,
                                     ListOfItemsResponseDTOMapper mapper,
                                     ListOfItemsLinkProvider listOfItemsLinkProvider,
                                     UserService userService,
                                     AuthorizationPolicy auth) {

        _listService = listService;
        _mapper = mapper;
        _listOfItemsLinkProvider = listOfItemsLinkProvider;
        _userService = userService;
        _auth = auth;
    }

    // OPTIONS — COLLECTION /my-lists
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        RepresentationModel<?> model = new RepresentationModel<>();

        if (userId != null) {
            User user = _userService.getUserByEmail(new UserId(new Email(userId)));
            _listOfItemsLinkProvider.getLinks(user).forEach(model::add);

        } else {

            model.add(
                    linkTo(methodOn(ListOfItemsRestController.class)
                            .getPublicLists())
                            .withRel("public-lists")
            );

        }
        return ResponseEntity.ok(model);
    }

    // OPTIONS — SINGLE LIST /lists/{listId}
    @RequestMapping(path = "/{listId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> optionsList(
            @PathVariable String listId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        User user = userId == null ? null : _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItems list = _listService.getListById(new ListOfItemsId(listId));

        RepresentationModel<?> model = new RepresentationModel<>();

        if (_auth.canSeeList(user, list)) {
            model.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .getListById(listId, null)).withRel("self"));
        }

        if (_auth.canAddItemTo(user, list)) {
            model.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .addItemToList(listId, null, null)).withRel("add-item"));
        }

        if (_auth.canChangeVisibility(user, list)) {
            if (list.isPrivate()) {
                model.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPublic(listId, null, null)).withRel("make-public"));
            } else {
                model.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPrivate(listId, null)).withRel("make-private"));
            }
        }

        if (_auth.canDeleteList(user, list)) {
            model.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .deleteList(listId, null)).withRel("delete"));
        }

        return ResponseEntity.ok(model);
    }

    // OPTIONS — ITEMS OF LIST /lists/{listId}/items
    @RequestMapping(path = "/{listId}/items", method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> optionsItems(
            @PathVariable String listId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        User user = userId == null ? null : _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItems list = _listService.getListById(new ListOfItemsId(listId));

        RepresentationModel<?> model = new RepresentationModel<>();

        if (_auth.canSeeList(user, list)) {
            model.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .getItemsInList(listId, null)).withRel("self"));
        }

        return ResponseEntity.ok(model);
    }

    // GET — COLLECTION
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLists(
            @RequestHeader("X-User-Id") String userId) {

        Email email = new Email(userId);
        UserId recUserId = new UserId(email);
        User user = _userService.getUserByEmail(new UserId(new Email(userId)));

        List<ListOfItems> listOfLists = _listService.getUserLists(recUserId);

        List<ListOfItemsResponseDTO> resultDTO = new ArrayList<>();

        for (ListOfItems listOfItems : listOfLists) {
            ListOfItemsResponseDTO listDTO = _mapper.toModel(listOfItems);
            String listId = listDTO.getListId();

            listDTO.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .getListById(listId, null)).withSelfRel());

            if (_auth.canAddItemTo(user, listOfItems)) {
                listDTO.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .addItemToList(listId, null, null)).withRel("add-item"));
            }

            if (_auth.canChangeVisibility(user, listOfItems)) {
                if (listOfItems.isPrivate()) {
                    listDTO.add(linkTo(methodOn(ListOfItemsRestController.class)
                            .makeListPublic(listId, null, null)).withRel("make-public"));
                } else {
                    listDTO.add(linkTo(methodOn(ListOfItemsRestController.class)
                            .makeListPrivate(listId, null)).withRel("make-private"));
                }
            }

            if (_auth.canDeleteList(user, listOfItems)) {
                listDTO.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .deleteList(listId, null)).withRel("delete"));
            }

            resultDTO.add(listDTO);
        }

        if (resultDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        CollectionModel<ListOfItemsResponseDTO> result = CollectionModel.of(resultDTO);

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .createAndSaveList(userId, null)).withRel("create-list"));

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getLists(userId)).withSelfRel());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // GET — SINGLE LIST
    @GetMapping(path = "/{listId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListById(
            @PathVariable String listId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        User user = userId == null ? null : _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItemsId recListId = new ListOfItemsId(listId);
        ListOfItems list = _listService.getListById(recListId);

        if (!_auth.canSeeList(user, list)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getListById(listId, null)).withSelfRel());

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getLists(result.getUserId())).withRel("collection"));

        if (_auth.canAddItemTo(user, list)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .addItemToList(listId, null, null)).withRel("add-item"));
        }

        if (_auth.canChangeVisibility(user, list)) {
            if (list.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPublic(listId, null, null)).withRel("make-public"));
            } else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPrivate(listId, null)).withRel("make-private"));
            }
        }

        if (_auth.canDeleteList(user, list)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .deleteList(listId, null)).withRel("delete"));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    // POST — CREATE LIST
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAndSaveList(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody ListOfItemsRequestDTO dto) {

        Email email = new Email(userId);
        UserId recUserId = new UserId(email);
        Name name = new Name(dto.getName());
        GenreId genreId = new GenreId(dto.getGenreId());

        ListOfItems list = _listService.save(recUserId, name, genreId);

        ListOfItemsResponseDTO result = _mapper.toModel(list);
        String listId = result.getListId();

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getListById(listId, null)).withSelfRel());

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getLists(result.getUserId())).withRel("collection"));

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .addItemToList(listId, null, null)).withRel("add-item"));

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .makeListPublic(listId, null, null)).withRel("make-public"));

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .deleteList(listId, null)).withRel("delete"));

        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

    // POST — ADD ITEM
    @PostMapping(path = "/{listId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addItemToList(
            @PathVariable String listId,
            @RequestBody AddItemRequestDTO itemId,
            @RequestHeader("X-User-Id") String userId) {

        User user = _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItems list = _listService.getListById(new ListOfItemsId(listId));

        if (!_auth.canAddItemTo(user, list)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ItemId recItemId = new ItemId(itemId.getItemId());
        ListOfItems updated = _listService.addItemToList(new ListOfItemsId(listId), recItemId);

        ListOfItemsResponseDTO result = _mapper.toModel(updated);

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getListById(listId, null)).withSelfRel());

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getLists(result.getUserId())).withRel("collection"));

        if (_auth.canAddItemTo(user, updated)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .addItemToList(listId, null, null)).withRel("add-item"));
        }

        if (_auth.canChangeVisibility(user, updated)) {
            if (updated.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPublic(listId, null, null)).withRel("make-public"));
            } else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPrivate(listId, null)).withRel("make-private"));
            }
        }

        if (_auth.canDeleteList(user, updated)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .deleteList(listId, null)).withRel("delete"));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    // PATCH — MAKE PUBLIC
    @PatchMapping(path = "/{listId}/visibility", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> makeListPublic(
            @PathVariable String listId,
            @RequestBody MakeListPublicRequestDTO durationDays,
            @RequestHeader("X-User-Id") String userId) {

        User user = _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItems list = _listService.getListById(new ListOfItemsId(listId));

        if (!_auth.canChangeVisibility(user, list)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        SharedDuration sharedDuration = new SharedDuration(durationDays.getSharedUntil());
        ListOfItems updated = _listService.makePublic(new ListOfItemsId(listId), sharedDuration);

        ListOfItemsResponseDTO result = _mapper.toModel(updated);

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getListById(listId, null)).withSelfRel());

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getLists(result.getUserId())).withRel("collection"));

        if (_auth.canAddItemTo(user, updated)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .addItemToList(listId, null, null)).withRel("add-item"));
        }

        if (_auth.canChangeVisibility(user, updated)) {
            if (updated.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPublic(listId, null, null)).withRel("make-public"));
            } else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPrivate(listId, null)).withRel("make-private"));
            }
        }

        if (_auth.canDeleteList(user, updated)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .deleteList(listId, null)).withRel("delete"));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // PATCH — MAKE PRIVATE
    @PatchMapping(path = "/{listId}/visibility", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> makeListPrivate(
            @PathVariable String listId,
            @RequestHeader("X-User-Id") String userId) {

        User user = _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItems list = _listService.getListById(new ListOfItemsId(listId));

        if (!_auth.canChangeVisibility(user, list)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ListOfItems updated = _listService.makePrivate(new ListOfItemsId(listId));

        ListOfItemsResponseDTO result = _mapper.toModel(updated);

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getListById(listId, null)).withSelfRel());

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getLists(result.getUserId())).withRel("collection"));

        if (_auth.canAddItemTo(user, updated)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .addItemToList(listId, null, null)).withRel("add-item"));
        }

        if (_auth.canChangeVisibility(user, updated)) {
            if (updated.isPrivate()) {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPublic(listId, null, null)).withRel("make-public"));
            } else {
                result.add(linkTo(methodOn(ListOfItemsRestController.class)
                        .makeListPrivate(listId, null)).withRel("make-private"));
            }
        }

        if (_auth.canDeleteList(user, updated)) {
            result.add(linkTo(methodOn(ListOfItemsRestController.class)
                    .deleteList(listId, null)).withRel("delete"));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    // DELETE — LIST
    @DeleteMapping(path = "/{listId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteList(
            @PathVariable String listId,
            @RequestHeader("X-User-Id") String userId) {

        User user = _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItems list = _listService.getListById(new ListOfItemsId(listId));

        if (!_auth.canDeleteList(user, list)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        _listService.deleteList(new ListOfItemsId(listId));

        return new ResponseEntity<>(HttpStatus.OK);

    }

    // GET — PUBLIC LISTS
    @GetMapping(path = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPublicLists() {

        List<ListOfItems> publicLists = _listService.getPublicLists();

        List<ListOfItemsResponseDTO> resultDTO = publicLists.stream()
                .map(list -> {
                    ListOfItemsResponseDTO dto = _mapper.toModel(list);
                    String listId = dto.getListId();

                    dto.add(linkTo(methodOn(ListOfItemsRestController.class)
                            .getListById(listId, null)).withSelfRel());

                    dto.add(linkTo(methodOn(ListOfItemsRestController.class)
                            .getItemsInList(listId, null)).withRel("items"));

                    return dto;
                })
                .collect(Collectors.toList());

        if (resultDTO.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        CollectionModel<ListOfItemsResponseDTO> result = CollectionModel.of(resultDTO);

        result.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getPublicLists()).withSelfRel());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // GET - items in list
    @GetMapping(path = "/{listId}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getItemsInList(
            @PathVariable String listId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        User user = userId == null ? null : _userService.getUserByEmail(new UserId(new Email(userId)));
        ListOfItems list = _listService.getListById(new ListOfItemsId(listId));

        // Authorization check
        if (!_auth.canSeeList(user, list)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<String> itemIds = list.getItemIds()
                .stream()
                .map(ItemId::toString)
                .toList();

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getItemsInList(listId, null)).withSelfRel());

        model.add(linkTo(methodOn(ListOfItemsRestController.class)
                .getListById(listId, null)).withRel("list"));

        return new ResponseEntity<>(new Object() {
            public final List<String> items = itemIds;
            public final RepresentationModel<?> links = model;
        }, HttpStatus.OK);

    }

}
