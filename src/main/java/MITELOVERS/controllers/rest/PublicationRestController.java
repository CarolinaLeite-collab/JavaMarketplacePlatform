package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublicationLinkProvider;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.Title;
import org.springframework.hateoas.MediaTypes;
import MITELOVERS.dto.request.PublicationRequestDTO;
import MITELOVERS.dto.response.PublicationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing publication-related endpoints
 * via HTTP endpoints.
 */

@RestController
@RequestMapping("/publications")
@Validated
public class PublicationRestController {

    private final PublicationService _publicationService;
    private final PublicationLinkProvider _publicationLinkProvider;
    private final UserService _userService;

    public PublicationRestController(PublicationService publicationService,
                                     PublicationLinkProvider publicationLinkProvider,
                                     UserService userService) {

        _publicationService = publicationService;
        _publicationLinkProvider = publicationLinkProvider;
        _userService = userService;

    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(
                linkTo(methodOn(PublicationRestController.class)
                        .options(email))
                        .withSelfRel()
        );

        _publicationLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicationResponseDTO> registerPublicationAndReturnDTO(
            @Valid @RequestBody PublicationRequestDTO info) {

        Publication publication =
                _publicationService.registerPublication(
                        new Title(info.getTitle()),
                        new AuthorId(info.getAuthorId()),
                        Year.of(info.getReleaseYear()),
                        new GenreId(info.getGenreId())
                );
        PublicationResponseDTO publicationResponseDTO =
                _publicationService.getPublicationResponseDTO(publication);

        publicationResponseDTO.add(
                linkTo(
                        methodOn(PublicationRestController.class)
                                .getPublicationById(
                                        publicationResponseDTO.getPublicationId()
                                )
                ).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publicationResponseDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicationResponseDTO>> getAllPublications() {

        List<PublicationResponseDTO> publications =
                _publicationService.getAllPublications();

        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        publications.forEach(publication ->
                publication.add(
                        linkTo(methodOn(PublicationRestController.class)
                                .getPublicationById(publication.getPublicationId()))
                                .withSelfRel()
                )
        );

        return ResponseEntity.ok(publications);
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicationResponseDTO> getPublicationById(
            @PathVariable String id) {

        PublicationResponseDTO publication =
                _publicationService.getPublicationById(id);

        publication.add(
                linkTo(methodOn(PublicationRestController.class)
                        .getPublicationById(publication.getPublicationId()))
                        .withSelfRel()
        );

        return ResponseEntity.ok(publication);
    }

}
