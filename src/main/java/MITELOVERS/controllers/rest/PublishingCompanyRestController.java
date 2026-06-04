package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublishingCompanyLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import MITELOVERS.mapper.PublicationTypeResponseDTOMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for exposing publication-related endpoints
 * via HTTP endpoints.
 */

@Validated
@RequestMapping("/publishingCompanies")
@RestController
public class PublishingCompanyRestController {

    private final PublishingCompanyService _publishingCompanyService;
    private final PublishingCompanyLinkProvider _publishingCompanyLinkProvider;
    private final UserService _userService;
    private final PublicationTypeResponseDTOMapper _publicationTypeResponseDTOMapper;


    public PublishingCompanyRestController(PublishingCompanyService publishingCompanyService,
                                           PublishingCompanyLinkProvider publishingCompanyLinkProvider,
                                           UserService userService,
                                           PublicationTypeResponseDTOMapper publicationTypeResponseDTOMapper) {

        _publishingCompanyService = publishingCompanyService;
        _publishingCompanyLinkProvider = publishingCompanyLinkProvider;
        _userService = userService;
        _publicationTypeResponseDTOMapper = publicationTypeResponseDTOMapper;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();

        _publishingCompanyLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerPublishingCompany(@RequestBody PublishingCompanyRequestDTO dto) {

        PublishingCompanyResponseDTO result = _publishingCompanyService.registerPublishingCompany(dto);

            return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllPublishingCompanies() {

            List<PublishingCompanyResponseDTO> result = _publishingCompanyService.getAllPublishingCompanies();

            return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(path = "/{publishingCompanyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPublishingCompanyById(@PathVariable String publishingCompanyId) {

            PublishingCompanyResponseDTO dto = _publishingCompanyService.getPublishingCompanyById(publishingCompanyId);

            return new ResponseEntity<>(dto, HttpStatus.OK);

    }

}
