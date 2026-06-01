package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.dto.response.LibraryItemDetailsDTO;
import MITELOVERS.dto.response.LibraryItemSummaryDTO;
import MITELOVERS.dto.request.AddItemRequestDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(path ="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<LibraryItemSummaryDTO>> getMyLibrary(
            @RequestHeader("X-User-Id") String userId) {

        try {
            List<LibraryItemSummaryDTO> dtos = _libraryService.getListOfItemInfoInMyLibrary(userId);

            for (LibraryItemSummaryDTO dto : dtos) {
                Link link = linkTo(methodOn(LibraryRestController.class)
                        .getItemDetail(dto.getItemId())).withSelfRel();
                dto.add(link);
            }
            CollectionModel<LibraryItemSummaryDTO> result = CollectionModel.of(dtos,
                    linkTo(methodOn(LibraryRestController.class)
                            .getMyLibrary(userId)).withSelfRel());

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

        @GetMapping(path ="/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<LibraryItemDetailsDTO> getItemDetail(@PathVariable String itemId) {

            try {
                LibraryItemDetailsDTO dto = _libraryService.getItemDetail(itemId);

                Link link = linkTo(methodOn(LibraryRestController.class)
                        .getItemDetail(itemId)).withSelfRel();
                dto.add(link);

                return new ResponseEntity<>(dto, HttpStatus.OK);

            } catch (IllegalStateException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        }

    @PostMapping(path ="/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addItemToLibrary(
            @RequestBody AddItemRequestDTO request,
            @RequestHeader("X-User-Id") String userId) {

        try {
            _libraryService.addItemToLibrary(request.getItemId(), userId);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (IllegalStateException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}