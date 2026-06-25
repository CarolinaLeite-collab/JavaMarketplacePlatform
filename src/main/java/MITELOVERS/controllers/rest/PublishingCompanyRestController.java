package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.PublishingCompanyService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.PublishingCompanyLinkProvider;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import MITELOVERS.mapper.PublishingCompanyResponseDTOMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing publishing-company-related endpoints
 * via HTTP endpoints.
 */
@Validated
@RequestMapping("/publishingCompanies")
@RestController
public class PublishingCompanyRestController {

    private final PublishingCompanyService _publishingCompanyService;
    private final PublishingCompanyLinkProvider _publishingCompanyLinkProvider;
    private final UserService _userService;
    private final PublishingCompanyResponseDTOMapper _publishingCompanyResponseDTOMapper;

    public PublishingCompanyRestController(PublishingCompanyService publishingCompanyService,
                                           PublishingCompanyLinkProvider publishingCompanyLinkProvider,
                                           UserService userService,
                                           PublishingCompanyResponseDTOMapper publishingCompanyResponseDTOMapper) {

        _publishingCompanyService = publishingCompanyService;
        _publishingCompanyLinkProvider = publishingCompanyLinkProvider;
        _userService = userService;
        _publishingCompanyResponseDTOMapper = publishingCompanyResponseDTOMapper;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(new UserId(new Email(email)));

        RepresentationModel<?> model = new RepresentationModel<>();

        _publishingCompanyLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerPublishingCompany(@RequestBody PublishingCompanyRequestDTO dto) {


        PublishingCompany publishingCompany = _publishingCompanyService.registerPublishingCompany(dto);

        PublishingCompanyResponseDTO response = _publishingCompanyResponseDTOMapper.toModel(publishingCompany);

        response.add(linkTo(methodOn(PublishingCompanyRestController.class)
                .getPublishingCompanyById(response.getPublishingCompanyId()))
                .withSelfRel());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllPublishingCompanies() {


        List<PublishingCompany> publishingCompanies = _publishingCompanyService.getAllPublishingCompanies();

        List<PublishingCompanyResponseDTO> response = publishingCompanies.stream()
                .map(_publishingCompanyResponseDTOMapper::toModel)
                .toList();

        response.forEach(item -> item.add(linkTo(methodOn(PublishingCompanyRestController.class)
                .getPublishingCompanyById(item.getPublishingCompanyId()))
                .withSelfRel()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{publishingCompanyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublishingCompanyResponseDTO> getPublishingCompanyById(
            @PathVariable String publishingCompanyId) {

        PublishingCompany publishingCompany =
                _publishingCompanyService.getPublishingCompanyById(publishingCompanyId);

        PublishingCompanyResponseDTO dto =
                _publishingCompanyResponseDTOMapper.toModel(publishingCompany);

        dto.add(linkTo(methodOn(PublishingCompanyRestController.class)
                .getPublishingCompanyById(dto.getPublishingCompanyId()))
                .withSelfRel());

        return ResponseEntity.ok(dto);
    }
}