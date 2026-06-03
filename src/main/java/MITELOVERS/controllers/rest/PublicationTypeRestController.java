package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublicationTypeService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublicationTypeLinkProvider;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import MITELOVERS.mapper.PublicationTypeResponseDTOMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final PublicationTypeResponseDTOMapper _publicationTypeResponseDTOMapper;
    private final PublicationTypeLinkProvider _publicationTypeLinkProvider;
    private final UserService _userService;

    public PublicationTypeRestController (PublicationTypeService publicationTypeService, PublicationTypeResponseDTOMapper publicationTypeResponseDTOMapper,  PublicationTypeLinkProvider publicationTypeLinkProvider, UserService userService) {

        _publicationTypeService = publicationTypeService;
        _publicationTypeResponseDTOMapper = publicationTypeResponseDTOMapper;
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

        List<PublicationType> publicationTypes =
                _publicationTypeService.getAllPublicationTypes();

        if (publicationTypes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PublicationTypeResponseDTO> response = new ArrayList<>();

        for (PublicationType publicationType : publicationTypes) {
            PublicationTypeResponseDTO dto = _publicationTypeResponseDTOMapper.toModel(publicationType);
            dto.add(linkTo(methodOn(PublicationTypeRestController.class)
                    .getPublicationTypeById(dto.getPublicationTypeId())).withSelfRel());
            response.add(dto);
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicationTypeResponseDTO> getPublicationTypeById(
            @PathVariable String id){
        PublicationType publicationType = _publicationTypeService.getPublicationTypeById(id);

        PublicationTypeResponseDTO dto = _publicationTypeResponseDTOMapper.toModel(publicationType);
        dto.add(linkTo(methodOn(PublicationTypeRestController.class)
                .getPublicationTypeById(dto.getPublicationTypeId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

}
