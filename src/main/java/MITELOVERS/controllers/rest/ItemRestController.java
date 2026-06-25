package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ItemService;
import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ItemLinkProvider;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.ItemRequestDTO;
import MITELOVERS.dto.response.ItemResponseDTO;
import MITELOVERS.mapper.ItemResponseDTOMapper;
import jakarta.validation.Valid;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing item-related endpoints via HTTP.
 */
@RestController
@RequestMapping("/items")
@Validated
public class ItemRestController {

    private final ItemService _itemService;
    private final LibraryService _libraryService;
    private final ItemLinkProvider _itemLinkProvider;
    private final UserService _userService;
    private final ItemResponseDTOMapper _mapper;

    public ItemRestController(ItemService itemService,
                              LibraryService libraryService,
                              ItemLinkProvider itemLinkProvider,
                              UserService userService,
                              ItemResponseDTOMapper mapper) {
        _itemService      = itemService;
        _libraryService   = libraryService;
        _itemLinkProvider = itemLinkProvider;
        _userService      = userService;
        _mapper           = mapper;
    }


    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(new UserId(new Email(email)));

        RepresentationModel<?> model = new RepresentationModel<>();
        model.add(linkTo(methodOn(ItemRestController.class).options(email)).withSelfRel());
        _itemLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponseDTO> registerItem(@Valid @RequestBody ItemRequestDTO info) {

        Item item = _itemService.registerItem(
                new EditionId(info.getEditionId()),
                Condition.valueOf(info.getCondition().toUpperCase()),
                new Description(info.getDescription())
        );

        return new ResponseEntity<>(_mapper.toModel(item, _itemService.resolveRelated(item)), HttpStatus.CREATED);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {

        List<ItemResponseDTO> items = _itemService.getAllItems().stream()
                .map(item -> _mapper.toModel(item, _itemService.resolveRelated(item)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable String id) {

        Item item = _itemService.getItemById(id);
        return new ResponseEntity<>(_mapper.toModel(item, _itemService.resolveRelated(item)), HttpStatus.OK);
    }


    @GetMapping(value = "/my-library", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemResponseDTO>> getItemsIdsInLibrary(
            @RequestHeader("X-User-Id") String email) {

        List<ItemId> itemIds = _libraryService.getItemIdsInLibrary(email);

        List<ItemResponseDTO> items = itemIds.stream()
                .map(id -> {
                    Item item = _itemService.getItemById(id.getValue());
                    return _mapper.toModel(item, _itemService.resolveRelated(item));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }
}
