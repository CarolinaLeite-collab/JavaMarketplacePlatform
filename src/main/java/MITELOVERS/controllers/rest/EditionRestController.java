package MITELOVERS.controllers.rest;

import MITELOVERS.dto.EditionRequestDTO;
import MITELOVERS.dto.EditionResponseDTO;
import MITELOVERS.applicationservices.EditionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<Object> registerEdition(@RequestHeader String pubId, @RequestBody EditionRequestDTO dto) {

        try {

            EditionResponseDTO result = _editionService.registerEdition(pubId, dto);

            return new ResponseEntity<>(result, HttpStatus.CREATED);

        }

        catch (Exception ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping
    public ResponseEntity<Object> getAllEditionsByPublication(@RequestHeader String publicationId){

        try{
        List<EditionResponseDTO> listOfEditionsDTO = _editionService.getAllEditionsByPublication(publicationId);

            return new ResponseEntity<>(listOfEditionsDTO, HttpStatus.OK);

        }

        catch (Exception ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{editionId}")
    public ResponseEntity<Object> getEditionById(@PathVariable String editionId){

        try {

            EditionResponseDTO result = _editionService.getEditionById(editionId);

            return new ResponseEntity<>(result, HttpStatus.OK);

        }

        catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

    }

}


