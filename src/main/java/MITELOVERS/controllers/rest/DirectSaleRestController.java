package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.mapper.DSFilteredItemsResponseMapper;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
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

    public DirectSaleRestController(DirectSaleService directSaleService,
                                    DirectSaleResponseDTOMapper responseMapper,
                                    DSFilteredItemsResponseMapper filteredResponseMapper) {

        _directSaleService = directSaleService;
        _responseMapper = responseMapper;
        _filteredResponseMapper = filteredResponseMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectSaleResponseDTO> createDirectSale(
            @RequestBody DirectSaleRequestDTO requestDTO) {

        DirectSale created = _directSaleService.createDirectSale(requestDTO);

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

}
