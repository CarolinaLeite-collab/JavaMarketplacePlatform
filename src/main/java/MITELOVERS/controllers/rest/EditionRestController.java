package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.EditionLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.dto.request.EditionRequestDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public EditionRestController(EditionService editionService, EditionLinkProvider editionLinkProvider, UserService userService) {

        _editionService = editionService;
        _editionLinkProvider = editionLinkProvider;
        _userService = userService;

    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

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

        try {

            EditionResponseDTO result = _editionService.registerEdition(pubId, dto);

            result.add(linkTo(methodOn(EditionRestController.class)
                    .getEditionById(result.getEditionId()))
                    .withSelfRel());

            return new ResponseEntity<>(result, HttpStatus.CREATED);

        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllEditions(){

        List<EditionResponseDTO> result = _editionService.getAllEditions();

        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        result.forEach(EditionResponseDTO ->
                EditionResponseDTO.add(
                        linkTo(methodOn(EditionRestController.class)
                                .getEditionById(EditionResponseDTO.getEditionId()))
                                .withSelfRel()
                )
        );

        CollectionModel<EditionResponseDTO> collection = CollectionModel.of(
                result, linkTo(methodOn(EditionRestController.class)
                            .getAllEditions())
                            .withSelfRel()
        );

        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @GetMapping(path = "/by-publication", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllEditionsByPublication(@RequestParam String publicationId){

        try {

            List<EditionResponseDTO> listOfEditionsDTO = _editionService.getAllEditionsByPublication(publicationId);

            if (listOfEditionsDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            listOfEditionsDTO.forEach(EditionResponseDTO ->
                    EditionResponseDTO.add(
                            linkTo(methodOn(EditionRestController.class)
                                    .getEditionById(EditionResponseDTO.getEditionId()))
                                    .withSelfRel()
                    )
            );

            CollectionModel<EditionResponseDTO> collection = CollectionModel.of(
                    listOfEditionsDTO, linkTo(methodOn(EditionRestController.class)
                            .getAllEditions())
                            .withSelfRel()
            );

            return new ResponseEntity<>(listOfEditionsDTO, HttpStatus.OK);

        }

        catch (Exception ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping(path = "/{editionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EditionResponseDTO> getEditionById(@PathVariable String editionId){

        EditionResponseDTO dto = _editionService.getEditionById(editionId);

        dto.add(linkTo(methodOn(EditionRestController.class)
                .getEditionById(dto.getEditionId()))
                .withSelfRel());

        return ResponseEntity.ok(dto);

    }

}


