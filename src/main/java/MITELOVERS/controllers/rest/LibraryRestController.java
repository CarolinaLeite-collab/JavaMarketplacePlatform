package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.ItemDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private LibraryService libraryService;

//    @Autowired
//    private PublicationService publicationService;

    @GetMapping("/publications")
    public ResponseEntity<CollectionModel<ItemDetailsDTO>> getMyLibrary(
            @RequestHeader("X-User-Id") String userId) {

        try {
            UserId uid = new UserId(new Email(userId));
            List<ItemDetailsDTO> dtos = libraryService.getListOfItemInfoInMyLibrary(uid);

            for (ItemDetailsDTO dto : dtos) {
                Link link = linkTo(methodOn(LibraryRestController.class)
                        .getMyLibrary(userId))
                        .withSelfRel();
                dto.add(link);
            }

            CollectionModel<ItemDetailsDTO> result = CollectionModel.of(dtos,
                    linkTo(methodOn(LibraryRestController.class)
                            .getMyLibrary(userId)).withSelfRel());

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
//    @PostMapping("/items")
//    public ResponseEntity<Void> registerAndAddToLibrary(
//            @RequestBody RegisterPublicationRequest req,
//            @RequestHeader("X-User-Id") String userId) {
//
//        ItemId itemId = publicationService.registerPublication(req);
//        libraryService.addItemToLibrary(itemId, new UserId(new Email(userId)));
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
}