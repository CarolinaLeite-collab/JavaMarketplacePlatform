package MITELOVERS.controllers.rest;

import MITELOVERS.dto.EditionResponseDTO;
import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.dto.request.EditionRequestDTO;
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

    public EditionRestController(EditionService editionService) {

        _editionService = editionService;

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

        List<EditionResponseDTO> listOfEditionsDTO = _editionService.getAllEditions();

        if (listOfEditionsDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(listOfEditionsDTO);

    }

    @GetMapping(path = "/by-publication", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllEditionsByPublication(@RequestParam String publicationId){

        try {

            List<EditionResponseDTO> listOfEditionsDTO = _editionService.getAllEditionsByPublication(publicationId);

            if (listOfEditionsDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(listOfEditionsDTO, HttpStatus.OK);

        }

        catch (Exception ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping(path = "/{editionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EditionResponseDTO> getEditionById(@PathVariable String editionId){

        EditionResponseDTO dto = _editionService.getEditionById(editionId);

        return ResponseEntity.ok(dto);

    }

}


