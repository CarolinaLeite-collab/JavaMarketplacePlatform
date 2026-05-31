package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/direct-sales")
public class DirectSaleRestController {

    private final DirectSaleService _directSaleService;

    public DirectSaleRestController(DirectSaleService directSaleService) {
        _directSaleService = directSaleService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectSaleResponseDTO> createDirectSale(
            @RequestBody DirectSaleRequestDTO requestDTO) {

        DirectSaleResponseDTO responseDTO =
                _directSaleService.createDirectSale(requestDTO);

        responseDTO.add(
                linkTo(methodOn(DirectSaleRestController.class)
                        .getDirectSaleById(responseDTO.getDirectSaleId()))
                        .withSelfRel()
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DirectSaleResponseDTO>> getAllDirectSales() {

        List<DirectSaleResponseDTO> response =
                _directSaleService.getAllDirectSales();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

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

        DirectSaleResponseDTO responseDTO =
                _directSaleService.getDirectSaleById(id);

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

        DSFilteredItemsResponseDTO dto =
                _directSaleService.getDirectSaleItemsByGenreAsc(genreId);

        // 2. Add links to each entry
        dto.getDirectSales().forEach(entry ->
                entry.add(
                        linkTo(methodOn(DirectSaleRestController.class)
                                .getDirectSaleById(entry.getDirectSaleId()))
                                .withSelfRel()
                )
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
