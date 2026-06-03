package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublicationTypeLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final PublicationTypeLinkProvider _publicationTypeLinkProvider;
    private final UserService _userService;

    public PublicationTypeRestController (PublicationTypeService publicationTypeService,  PublicationTypeLinkProvider publicationTypeLinkProvider, UserService userService) {

        _publicationTypeService = publicationTypeService;
        _publicationTypeLinkProvider = publicationTypeLinkProvider;
        _userService = userService;

    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(
                linkTo(methodOn(PublicationTypeRestController.class)
                        .options(email))
                        .withSelfRel()
        );

        _publicationTypeLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);
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
