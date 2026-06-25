package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.DirectSaleLinkProvider;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleNoPriceResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.mapper.DSFilteredItemsResponseMapper;
import MITELOVERS.mapper.DirectSaleNoPriceResponseDTOMapper;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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
    public ResponseEntity<RepresentationModel<?>> options(@RequestHeader("X-User-Id") String email) {

        User user = _userService.getUserByEmail(new UserId(new Email(email)));

        RepresentationModel<?> model = new RepresentationModel<>();

        _directSaleLinkProvider.getLinks(user)
                .forEach(model::add);

        return ResponseEntity.ok(model);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectSaleResponseDTO> createDirectSale(
            @RequestHeader("X-User-Id") String email,
            @RequestBody DirectSaleRequestDTO requestDTO) {

        List<ItemId> itemsId = requestDTO.getItemsId().stream().map(ItemId::new).toList();
        UserId sellerId = new UserId(new Email(email));
        User user = _userService.getUserByEmail(new UserId(new Email(email)));

        Price price = new Price(requestDTO.getPriceValue(), Currency.valueOf(requestDTO.getPriceCurrency()));
        Duration timeLimit = requestDTO.getTimeLimitSeconds() != null
                ? Duration.ofSeconds(requestDTO.getTimeLimitSeconds())
                : null;

        DirectSale created = _directSaleService.createDirectSale(itemsId, sellerId, price, timeLimit);

        DirectSaleResponseDTO responseDTO = _responseMapper.toResponseDTO(created);

        _directSaleLinkProvider.addResourceLinks(responseDTO, user);

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

        response.forEach(_directSaleLinkProvider::addResourceLinks);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<DirectSaleResponseDTO>> getAllActiveDirectSales(
            @RequestHeader("X-User-Id") @NotBlank String email) {

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        List<DirectSale> sales = _directSaleService.getAllActiveDirectSales();

        if (sales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DirectSaleResponseDTO> response = sales.stream()
                .map(_responseMapper::toResponseDTO)
                .toList();

        response.forEach(dto ->
                _directSaleLinkProvider.addResourceLinks(dto, user)
        );

        CollectionModel<DirectSaleResponseDTO> result = CollectionModel.of(response);

        _directSaleLinkProvider.addCollectionLinks(result, email);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectSaleResponseDTO> getDirectSaleById(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String id) {

        DirectSale directSale = _directSaleService.getDirectSaleById(new DirectSaleId(id));

        DirectSaleResponseDTO responseDTO = _responseMapper.toResponseDTO(directSale);

        User user = _userService.getUserByEmail(new UserId(new Email(userId)));

        _directSaleLinkProvider.addResourceLinks(responseDTO, user);

        for (ItemId itemId : directSale.getItemsId()) {
            responseDTO.add(
                    linkTo(methodOn(ItemRestController.class)
                            .getItemById(itemId.toString()))
                            .withRel("item"));
        }

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = "/genre/{genreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DSFilteredItemsResponseDTO> getDirectSaleItemsByGenre(
            @PathVariable String genreId) {

        List<DirectSaleId> ids = _directSaleService.getDirectSaleItemsByGenreAsc(new GenreId(genreId));

        DSFilteredItemsResponseDTO dto =
                _filteredResponseMapper.toDTO(ids.stream().map(DirectSaleId::toString).toList());

        _directSaleLinkProvider.addCollectionLinks(dto, genreId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/without-price", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DirectSaleNoPriceResponseDTO>> getDirectSalesWithoutPrice() {

        List<DirectSale> sales = _directSaleService.getAllActiveDirectSales();

        if (sales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DirectSaleNoPriceResponseDTO> response = sales.stream()
                .map(_noPriceMapper::toModel)
                .toList();

        response.forEach(_directSaleLinkProvider::addResourceLinks);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}/without-price", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectSaleNoPriceResponseDTO> getDirectSaleWithoutPrice(@PathVariable String id){

        DirectSale directSale = _directSaleService.getDirectSaleById(new DirectSaleId(id));

        DirectSaleNoPriceResponseDTO dto = _noPriceMapper.toModel(directSale);

        dto = _directSaleLinkProvider.addNoPriceResourceLinks(dto);

        for (ItemId itemId : directSale.getItemsId()) {
            dto.add(
                    linkTo(methodOn(ItemRestController.class)
                            .getItemById(itemId.toString()))
                            .withRel("item")
            );
        }

        return ResponseEntity.ok(dto);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDirectSale(@PathVariable String id) {

        _directSaleService.deleteDirectSale(new DirectSaleId(id));

        return ResponseEntity.noContent().build();
    }
}
