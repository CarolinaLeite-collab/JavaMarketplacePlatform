package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryItemDetails;
import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.LibraryLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.LibrarySort;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.response.LibraryItemResponseDTO;
import MITELOVERS.mapper.LibraryItemResponseDTOMapper;
import MITELOVERS.mapper.LibrarySortRequestMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing the authenticated user's library
 * via HTTP endpoints.
 */


@RestController
@RequestMapping("/my-library")
public class LibraryRestController {

    private final LibraryService _libraryService;
    private final LibraryLinkProvider _libraryLinkProvider;
    private final UserService _userService;
    private final LibrarySortRequestMapper _sortRequestMapper;
    private final LibraryItemResponseDTOMapper _libraryItemResponseDTOMapper;

    public LibraryRestController(LibraryService libraryService,
                                 LibraryLinkProvider libraryLinkProvider,
                                 UserService userService,
                                 LibrarySortRequestMapper sortRequestMapper,
                                 LibraryItemResponseDTOMapper libraryItemResponseDTOMapper) {
        _libraryService = libraryService;
        _libraryLinkProvider = libraryLinkProvider;
        _userService = userService;
        _sortRequestMapper = sortRequestMapper;
        _libraryItemResponseDTOMapper = libraryItemResponseDTOMapper;
    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> options(
            @RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(
                linkTo(methodOn(LibraryRestController.class)
                        .options(email))
                        .withSelfRel()
        );

        _libraryLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .body(model);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<LibraryItemResponseDTO>> getMyLibrary(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(value = "sort", required = false) String sort) {

        UserId uid = new UserId(new Email(userId));
        LibrarySort librarySort = _sortRequestMapper.toDomain(sort);

        List<LibraryItemDetails> itemDetailsList =
                _libraryService.getListOfItemInfoInMyLibrary(uid, librarySort);

        List<LibraryItemResponseDTO> dtos = itemDetailsList.stream()
                .map(this::toDTO)
                .toList();

        for (LibraryItemResponseDTO dto : dtos) {
            Link link = linkTo(methodOn(LibraryRestController.class)
                    .getItemDetail(dto.getItemId())).withSelfRel();
            dto.add(link);
        }

        CollectionModel<LibraryItemResponseDTO> result = CollectionModel.of(dtos,
                linkTo(methodOn(LibraryRestController.class)
                        .getMyLibrary(userId, sort)).withSelfRel());

        result.add(
                Link.of("/my-library{?sort}")
                        .withRel("sort")
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryItemResponseDTO> getItemDetail(@PathVariable String itemId) {

        LibraryItemDetails itemDetails = _libraryService.getItemDetail(itemId);
        LibraryItemResponseDTO dto = toDTO(itemDetails);

        Link link = linkTo(methodOn(LibraryRestController.class)
                .getItemDetail(itemId)).withSelfRel();
        dto.add(link);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LibraryItemResponseDTO> addItemToLibrary(
            @Valid @RequestBody AddItemRequestDTO request,
            @RequestHeader("X-User-Id") String userId) {

        LibraryItemDetails itemDetails = _libraryService.addItemToLibrary(request.getItemId(), userId);
        LibraryItemResponseDTO dto = toDTO(itemDetails);

        Link selfLink = linkTo(methodOn(LibraryRestController.class)
                .getItemDetail(dto.getItemId())).withSelfRel();
        dto.add(selfLink);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    private LibraryItemResponseDTO toDTO(LibraryItemDetails itemDetails) {
        return _libraryItemResponseDTOMapper.toDTO(
                itemDetails.item(),
                itemDetails.publication(),
                itemDetails.edition(),
                itemDetails.author(),
                itemDetails.publicationType());
    }
}
