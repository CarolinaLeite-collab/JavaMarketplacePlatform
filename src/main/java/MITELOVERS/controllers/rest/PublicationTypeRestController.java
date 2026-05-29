package MITELOVERS.controllers.rest;

import MITELOVERS.dto.request.PublicationTypeResponseDTO;
import MITELOVERS.applicationservices.PublicationTypeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller responsible for exposing publicationType-related endpoints
 * via HTTP endpoints.
 */

@RestController
@RequestMapping("/publicationTypes")
public class PublicationTypeRestController {

    private final PublicationTypeService _publicationTypeService;

    public PublicationTypeRestController (PublicationTypeService publicationTypeService){

        _publicationTypeService = publicationTypeService;

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PublicationTypeResponseDTO>> getAllPublicationTypes() {
        return null;
    }

}
