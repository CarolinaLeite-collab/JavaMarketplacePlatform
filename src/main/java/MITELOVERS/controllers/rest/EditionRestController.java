package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.EditionService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.EditionLinkProvider;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.EditionRequestDTO;
import MITELOVERS.dto.response.EditionResponseDTO;
import MITELOVERS.mapper.EditionRequestDTOMapper;
import MITELOVERS.mapper.EditionResponseDTOMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller exposing edition-related endpoints under {@code /editions}.
 * Supports OPTIONS/GET on the editions collection, POST for edition registration,
 * OPTIONS/GET for retrieving a single edition, and OPTIONS/GET for listing
 * editions associated with a specific publication. Converts request DTO data
 * into domain value objects before delegating to the application service and
 * relies on {@link EditionLinkProvider} to enrich responses with HATEOAS links.
 */


@RestController
@RequestMapping("/editions")
public class EditionRestController {

    private final EditionService _editionService;
    private final UserService _userService;
    private final EditionResponseDTOMapper _editionResponseDTOMapper;
    private final EditionRequestDTOMapper _editionRequestDTOMapper;
    private final EditionLinkProvider _editionLinkProvider;

    public EditionRestController(EditionService editionService,
                                 EditionLinkProvider editionLinkProvider,
                                 UserService userService,
                                 EditionResponseDTOMapper editionResponseDTOMapper,
                                 EditionRequestDTOMapper editionRequestDTOMapper) {

        _editionService = editionService;
        _editionLinkProvider = editionLinkProvider;
        _userService = userService;
        _editionResponseDTOMapper = editionResponseDTOMapper;
        _editionRequestDTOMapper = editionRequestDTOMapper;

    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options(
            @RequestHeader("X-User-Id") String email) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            allowedMethods = _editionLinkProvider.getAllowedMethods(user);
        }

        return ResponseEntity.ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerEdition(@RequestParam String pubId, @RequestBody EditionRequestDTO dto) {


        PublicationTypeId typeId = new PublicationTypeId(dto.getPublicationTypeId());
        Identifier identifier = _editionRequestDTOMapper.toIdentifier(dto);
        PublicationId publicationId = new PublicationId (pubId);
        PublishingCompanyId publishingCompanyId = new PublishingCompanyId(dto.getPublishingCompanyId());
        Year publishingYear = Year.of(dto.getPublishingYear());
        Language editionLanguage = Language.valueOf(dto.getLanguage());

        // optional fields
        Dimension dimension = _editionRequestDTOMapper.toDimension(dto);
        Weight weight = _editionRequestDTOMapper.toWeight(dto);
        NumberOfPages numberOfPages = _editionRequestDTOMapper.toNumberOfPages(dto);
        EditionNumber editionNumber = _editionRequestDTOMapper.toEditionNumber(dto);
        Binding binding = _editionRequestDTOMapper.toBinding(dto);

        Edition edition = _editionService.registerEdition(
                typeId,
                identifier,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage,
                dimension,
                weight,
                numberOfPages,
                editionNumber,
                binding);

        EditionResponseDTO result = _editionResponseDTOMapper.toModel(edition);

        _editionLinkProvider.addLinkForEdition(result, edition.getEditionId());

        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllEditions(){

        List<Edition> editions = _editionService.getAllEditions();

        if (editions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EditionResponseDTO> response = new ArrayList<>();

        for (Edition edition : editions) {
            EditionResponseDTO dto = _editionResponseDTOMapper.toModel(edition);
            response.add(dto);
        }
        CollectionModel<EditionResponseDTO> collection =
                _editionLinkProvider.addLinksForAllEditions(response);

        return new ResponseEntity<>(collection, HttpStatus.OK);

    }

    @RequestMapping(path = "/by-publication", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForPublication(
            @RequestHeader("X-User-Id") String email) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            allowedMethods = _editionLinkProvider.getAllowedMethodsForPublication(user);
        }

        return ResponseEntity.ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }


    @GetMapping(path = "/by-publication", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllEditionsByPublication(@RequestParam String publicationId){

        PublicationId pubId = new PublicationId(publicationId);

        List<Edition> editions = _editionService.getAllEditionsByPublication(pubId);

            if (editions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EditionResponseDTO> response = new ArrayList<>();

            for (Edition edition : editions) {
                EditionResponseDTO dto = _editionResponseDTOMapper.toModel(edition);
                response.add(dto);
            }

        CollectionModel<EditionResponseDTO> collection =
                _editionLinkProvider.addLinksForEditionsByPublication(response, publicationId);

        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @RequestMapping(path = "/{editionId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForEditionId(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String editionId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(new UserId(new Email(email)));

            EditionId id = new EditionId(editionId);
            _editionService.getEditionById(id);

            allowedMethods = _editionLinkProvider.getAllowedMethodsForEditionId(user);
        }

        return ResponseEntity.ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }

    @GetMapping(path = "/{editionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getEditionById(@PathVariable String editionId) {

        EditionId newEditionId = new EditionId(editionId);

        Edition edition = _editionService.getEditionById(newEditionId);

        EditionResponseDTO result = _editionResponseDTOMapper.toModel(edition);

        _editionLinkProvider.addLinkForEdition(result, newEditionId);

        return ResponseEntity.ok(result);

    }

}


