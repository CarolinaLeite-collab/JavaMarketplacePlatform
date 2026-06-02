package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        List<PublicationTypeResponseDTO> publicationTypes =
                _publicationTypeService.getAllPublicationTypes();

        if (publicationTypes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (PublicationTypeResponseDTO publicationTypeDTO : publicationTypes) {

            publicationTypeDTO.add(
                    linkTo(
                            methodOn(PublicationTypeRestController.class)
                                    .getPublicationTypeById(
                                            publicationTypeDTO.getPublicationTypeId()
                                    )
                    ).withSelfRel()
            );
        }

        return ResponseEntity.ok(publicationTypes);

    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicationTypeResponseDTO> getPublicationTypeById(
            @PathVariable String id){

        PublicationTypeResponseDTO publicationType =
                _publicationTypeService.getPublicationTypeById(id);

        return ResponseEntity.ok(publicationType);

    }

}
