package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.DirectSaleLinkProvider;
import MITELOVERS.domain.directsale.DirectSale;
import jakarta.validation.constraints.NotBlank;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleNoPriceResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.DSFilteredItemsResponseMapper;
import MITELOVERS.mapper.DirectSaleNoPriceResponseDTOMapper;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller exposing Direct Sale operations to API clients.
 *
 * <p>Provides endpoints for creating direct sales, retrieving all sales,
 * fetching a specific sale by its identifier, and obtaining filtered direct
 * sales based on genre. Responses are enriched with HATEOAS links to support
 * discoverability and navigability of the API.</p>
 *
 * <p>This controller delegates all business logic to
 * {@link DirectSaleService}, ensuring a clean separation between HTTP
 * concerns and domain/application logic.</p>
 */

@RestController
@RequestMapping("/direct-sales")
public class DirectSaleRestController {

    private final DirectSaleService _directSaleService;
    private final DirectSaleResponseDTOMapper _responseMapper;
    private final DSFilteredItemsResponseMapper _filteredResponseMapper;
    private final DirectSaleNoPriceResponseDTOMapper _noPriceMapper;
    private final DirectSaleLinkProvider _directSaleLinkProvider;
    private final UserService _userService;

    public DirectSaleRestController(DirectSaleService directSaleService,
                                    DirectSaleResponseDTOMapper responseMapper,
                                    DSFilteredItemsResponseMapper filteredResponseMapper,
                                    DirectSaleNoPriceResponseDTOMapper noPriceMapper,
                                    DirectSaleLinkProvider directSaleLinkProvider,
                                    UserService userService) {

        _directSaleService = directSaleService;
        _responseMapper = responseMapper;
        _filteredResponseMapper = filteredResponseMapper;
        _noPriceMapper = noPriceMapper;
        _directSaleLinkProvider = directSaleLinkProvider;
        _userService = userService;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<RepresentationModel<?>> options(@RequestParam("email") String email) {

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(
                linkTo(methodOn(DirectSaleRestController.class)
                        .options(email))
                        .withSelfRel()
        );

        _directSaleLinkProvider.getLinks(user).forEach(model::add);

        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectSaleResponseDTO> createDirectSale(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody DirectSaleRequestDTO requestDTO) {

        DirectSale created = _directSaleService.createDirectSale(requestDTO, userId);

        DirectSaleResponseDTO responseDTO = _responseMapper.toResponseDTO(created);

        responseDTO.add(
                linkTo(methodOn(DirectSaleRestController.class)
                        .getDirectSaleById(responseDTO.getDirectSaleId()))
                        .withSelfRel()
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DirectSaleResponseDTO>> getAllDirectSales() {

        List<DirectSale> sales = _directSaleService.getAllDirectSales();

        if (sales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DirectSaleResponseDTO> response = sales.stream()
                .map(_responseMapper::toResponseDTO)
                .toList();

        response.forEach(dto ->
                dto.add(
                        linkTo(methodOn(DirectSaleRestController.class)
                                .getDirectSaleById(dto.getDirectSaleId()))
                                .withSelfRel()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<DirectSaleResponseDTO>> getAllActiveDirectSales(
            @RequestHeader("X-User-Id") @NotBlank String userId) {

        List<DirectSale> sales = _directSaleService.getAllActiveDirectSales();

        if (sales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DirectSaleResponseDTO> response = sales.stream()
                .map(_responseMapper::toResponseDTO)
                .toList();

        response.forEach(dto ->
                _directSaleLinkProvider.addResourceLinks(dto, userId)
        );

        CollectionModel<DirectSaleResponseDTO> result = CollectionModel.of(response);

        _directSaleLinkProvider.addCollectionLinks(result, userId);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectSaleResponseDTO> getDirectSaleById(
            @PathVariable String id) {

        DirectSale directSale = _directSaleService.getDirectSaleById(id);

        DirectSaleResponseDTO responseDTO = _responseMapper.toResponseDTO(directSale);

        responseDTO.add(
                linkTo(methodOn(DirectSaleRestController.class)
                        .getDirectSaleById(id))
                        .withSelfRel()
        );

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = "/genre/{genreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DSFilteredItemsResponseDTO> getDirectSaleItemsByGenre(
            @PathVariable String genreId) {

        List<DirectSaleId> ids = _directSaleService.getDirectSaleItemsByGenreAsc(genreId);

        DSFilteredItemsResponseDTO dto =
                _filteredResponseMapper.toDTO(ids.stream().map(DirectSaleId::toString).toList());

        // 2. Add links to each entry
        dto.getDirectSales().forEach(entry ->
                entry.add(linkTo(methodOn(DirectSaleRestController.class)
                        .getDirectSaleById(entry.getDirectSaleId()))
                        .withSelfRel())
        );

        // 3. Add collection self link
        dto.add(
                linkTo(methodOn(DirectSaleRestController.class)
                        .getDirectSaleItemsByGenre(genreId))
                        .withSelfRel()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/without-price", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DirectSaleNoPriceResponseDTO>> getDirectSalesWithoutPrice() {

        List<DirectSale> sales = _directSaleService.getAllDirectSales();

        if (sales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DirectSaleNoPriceResponseDTO> response = sales.stream()
                .map(_noPriceMapper::toModel)
                .toList();

        response.forEach(dto ->
                dto.add(
                        linkTo(methodOn(DirectSaleRestController.class)
                                .getDirectSalesWithoutPrice())
                                .withSelfRel()
                )
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDirectSale(@PathVariable String id) {

        _directSaleService.deleteDirectSale(new DirectSaleId(id));

        return ResponseEntity.noContent().build();
    }

}
