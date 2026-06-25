package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublicationLinkProvider;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.PublicationRequestDTO;
import MITELOVERS.dto.response.PublicationResponseDTO;
import MITELOVERS.mapper.PublicationResponseDTOMapper;
import jakarta.validation.Valid;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.ArrayList;
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
    private final PublicationResponseDTOMapper _publicationResponseDTOMapper;
    private final PublicationLinkProvider _publicationLinkProvider;
    private final UserService _userService;

    public PublicationRestController(PublicationService publicationService,
                                     PublicationResponseDTOMapper publicationResponseDTOMapper,
                                     PublicationLinkProvider publicationLinkProvider,
                                     UserService userService) {

        _publicationService = publicationService;
        _publicationResponseDTOMapper = publicationResponseDTOMapper;
        _publicationLinkProvider = publicationLinkProvider;
        _userService = userService;

    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(new UserId(new Email(email)));

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

            Publication publication = _publicationService.registerPublication(
                    new Title(info.getTitle()),
                    new AuthorId(info.getAuthorId()),
                    Year.of(info.getReleaseYear()),
                    new GenreId(info.getGenreId()),
                    info.getSynopsis()
            );

            PublicationResponseDTO result = _publicationResponseDTOMapper.toModel(publication);

            result.add(linkTo(methodOn(PublicationRestController.class)
                    .getPublicationById(result.getPublicationId())).withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicationResponseDTO>> getAllPublications() {

        List<Publication> publications =
                _publicationService.getAllPublications();

        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PublicationResponseDTO> response = new ArrayList<>();

        for (Publication publication : publications) {
            PublicationResponseDTO dto = _publicationResponseDTOMapper.toModel(publication);
            dto.add(linkTo(methodOn(PublicationRestController.class)
                    .getPublicationById(dto.getPublicationId())).withSelfRel());
            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicationResponseDTO> getPublicationById(
            @PathVariable String id) {

            Publication publication = _publicationService.getPublicationById(id);

            PublicationResponseDTO result = _publicationResponseDTOMapper.toModel(publication);

            result.add(linkTo(methodOn(PublicationRestController.class)
                    .getPublicationById(result.getPublicationId())).withSelfRel());

            return ResponseEntity.ok(result);

    }

}
