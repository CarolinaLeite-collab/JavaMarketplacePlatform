package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.LibraryItemDetailsDTO;
import MITELOVERS.dto.LibraryItemSummaryDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    public LibraryRestController(LibraryService libraryService) {
        _libraryService = libraryService;
    }

    @GetMapping("/publications")
    public ResponseEntity<CollectionModel<LibraryItemSummaryDTO>> getMyLibrary(
            @RequestHeader("X-User-Id") String userId) {

        try {
            UserId uid = new UserId(new Email(userId));
            List<LibraryItemSummaryDTO> dtos = _libraryService.getListOfItemInfoInMyLibrary(uid);

            CollectionModel<LibraryItemSummaryDTO> result = CollectionModel.of(dtos,
                    linkTo(methodOn(LibraryRestController.class)
                            .getMyLibrary(userId)).withSelfRel());

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

        @GetMapping("/publications/{itemId}")
        public ResponseEntity<LibraryItemDetailsDTO> getItemDetail(@PathVariable String itemId) {

            try {
                LibraryItemDetailsDTO dto = _libraryService.getItemDetail(itemId);
                return new ResponseEntity<>(dto, HttpStatus.OK);

            } catch (IllegalStateException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        }

    @PostMapping("/publications")
    public ResponseEntity<Void> addItemToLibrary(
            @RequestBody AddItemRequestDTO request,
            @RequestHeader("X-User-Id") String userId) {

        try {
            UserId uid = new UserId(new Email(userId));
            ItemId itemId = new ItemId(request.getItemId());
            _libraryService.addItemToLibrary(itemId, uid);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (IllegalStateException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}