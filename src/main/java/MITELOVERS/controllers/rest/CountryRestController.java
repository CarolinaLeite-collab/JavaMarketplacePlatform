package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.CountryService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.CountryLinkProvider;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.CountryRequestDTO;
import MITELOVERS.dto.response.CountryResponseDTO;
import MITELOVERS.mapper.CountryResponseDTOMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller exposing Country operations to API clients.
 *
 * <p>Provides endpoints for creating countries, retrieving all countries,
 * fetching a specific country by its identifier. Responses are enriched with HATEOAS links to support
 * discoverability and navigability of the API.</p>
 *
 * <p>This controller delegates all business logic to
 * {@link CountryService}, ensuring a clean separation between HTTP
 * concerns and domain/application logic.</p>
 */

@RestController
@RequestMapping("/countries")
public class CountryRestController {

    private final CountryService _service;
    private final CountryResponseDTOMapper _mapper;
    private final UserService _userService;
    private final CountryLinkProvider _countryLinkProvider;

    public CountryRestController(CountryService service,
                                 CountryResponseDTOMapper mapper,
                                 UserService userService,
                                 CountryLinkProvider countryLinkProvider) {
        _service = service;
        _mapper = mapper;
        _userService = userService;
        _countryLinkProvider = countryLinkProvider;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(new UserId(new Email(email)));

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(
                linkTo(methodOn(CountryRestController.class)
                        .options(email))
                        .withSelfRel()
        );

        _countryLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryResponseDTO> create(@RequestBody CountryRequestDTO request) {

        Country created = _service.createCountry(request.getCountryName());
        CountryResponseDTO dto = _mapper.toModel(created);

        dto.add(linkTo(methodOn(CountryRestController.class)
                .findById(dto.getCountryId())).withSelfRel());

        dto.add(linkTo(methodOn(CountryRestController.class)
                .listAll()).withRel("list"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<CountryResponseDTO>> listAll() {

        List<CountryResponseDTO> items = StreamSupport
                .stream(_service.listAllCountries().spliterator(), false)
                .map(_mapper::toModel)
                .toList();

        CollectionModel<CountryResponseDTO> model = CollectionModel.of(items);

        model.add(linkTo(methodOn(CountryRestController.class)
                .listAll()).withSelfRel());

        model.add(linkTo(methodOn(CountryRestController.class)
                .create(null)).withRel("create"));

        return ResponseEntity.ok(model);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryResponseDTO> findById(@PathVariable String id) {

        Country country = _service.findById(id);
        CountryResponseDTO dto = _mapper.toModel(country);

        dto.add(linkTo(methodOn(CountryRestController.class)
                .findById(id)).withSelfRel());

        dto.add(linkTo(methodOn(CountryRestController.class)
                .listAll()).withRel("list"));

        return ResponseEntity.ok(dto);
    }

}
