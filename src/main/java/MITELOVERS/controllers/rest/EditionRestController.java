package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.EditionLinkProvider;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.EditionRequestDTO;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.mapper.EditionResponseDTOMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
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
 * REST controller responsible for exposing publication-related endpoints
 * via HTTP endpoints.
 */

@RestController
@RequestMapping("/editions")
public class EditionRestController {

    private final EditionService _editionService;
    private final EditionLinkProvider _editionLinkProvider;
    private final UserService _userService;
    private final EditionResponseDTOMapper _editionResponseDTOMapper;

    public EditionRestController(EditionService editionService, EditionLinkProvider editionLinkProvider,
                                 UserService userService, EditionResponseDTOMapper editionResponseDTOMapper) {

        _editionService = editionService;
        _editionLinkProvider = editionLinkProvider;
        _userService = userService;
        _editionResponseDTOMapper = editionResponseDTOMapper;

    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(new UserId(new Email(email)));

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(
                linkTo(methodOn(EditionRestController.class)
                        .options(email))
                        .withSelfRel()
        );

        _editionLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerEdition(@RequestParam String pubId, @RequestBody EditionRequestDTO dto) {

            Edition edition = _editionService.registerEdition(pubId, dto);

            EditionResponseDTO result = _editionResponseDTOMapper.toModel(edition);

            result.add(linkTo(methodOn(EditionRestController.class)
                    .getEditionById(result.getEditionId()))
                    .withSelfRel());

            return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllEditions(){

        List<Edition> editions = _editionService.getAllEditions();

        if (editions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EditionResponseDTO> response = new ArrayList<>();

        for (Edition edition : editions) {
            EditionResponseDTO dto = _editionResponseDTOMapper.toModel(edition);
            dto.add(linkTo(methodOn(EditionRestController.class)
                    .getEditionById(dto.getEditionId()))
                    .withSelfRel());
            response.add(dto);
        }

        CollectionModel<EditionResponseDTO> collection = CollectionModel.of(
                response, linkTo(methodOn(EditionRestController.class)
                        .getAllEditions())
                        .withSelfRel()
        );

        return new ResponseEntity<>(collection, HttpStatus.OK);

    }

    @GetMapping(path = "/by-publication", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllEditionsByPublication(@RequestParam String publicationId){

            List<Edition> editions = _editionService.getAllEditionsByPublication(publicationId);

            if (editions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EditionResponseDTO> response = new ArrayList<>();

            for (Edition edition : editions) {
                EditionResponseDTO dto = _editionResponseDTOMapper.toModel(edition);
                dto.add(linkTo(methodOn(EditionRestController.class)
                        .getEditionById(dto.getEditionId()))
                        .withSelfRel());
                response.add(dto);
            }

            CollectionModel<EditionResponseDTO> collection = CollectionModel.of(
                    response, linkTo(methodOn(EditionRestController.class)
                            .getAllEditionsByPublication(publicationId))
                            .withSelfRel());

            return new ResponseEntity<>(collection, HttpStatus.OK);


    }

    @GetMapping(path = "/{editionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getEditionById(@PathVariable String editionId) {
            Edition edition = _editionService.getEditionById(editionId);

            EditionResponseDTO result = _editionResponseDTOMapper.toModel(edition);

            result.add(linkTo(methodOn(EditionRestController.class)
                    .getEditionById(result.getEditionId()))
                    .withSelfRel());

            return ResponseEntity.ok(result);

    }

}


