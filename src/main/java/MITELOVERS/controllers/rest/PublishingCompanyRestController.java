package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublishingCompanyLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
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


    public PublishingCompanyRestController(PublishingCompanyService publishingCompanyService,
                                           PublishingCompanyLinkProvider publishingCompanyLinkProvider, UserService userService) {

        _publishingCompanyService = publishingCompanyService;
        _publishingCompanyLinkProvider = publishingCompanyLinkProvider;
        _userService = userService;
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

        try{

            PublishingCompanyResponseDTO result = _publishingCompanyService.registerPublishingCompany(dto);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllPublishingCompanies() {

        try{

            List<PublishingCompanyResponseDTO> result = _publishingCompanyService.getAllPublishingCompanies();

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "/{publishingCompanyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPublishingCompanyById(@PathVariable String publishingCompanyId) {

        try{

            PublishingCompanyResponseDTO dto = _publishingCompanyService.getPublishingCompanyById(publishingCompanyId);

            return new ResponseEntity<>(dto, HttpStatus.OK);

        }

        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
