package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.DirectSaleService;
import MITELOVERS.dto.DirectSaleRequestDTO;
import MITELOVERS.dto.DirectSaleResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direct-sales")
public class DirectSaleRestController {

    private final DirectSaleService _directSaleService;

    public DirectSaleRestController(DirectSaleService directSaleService) {
        _directSaleService = directSaleService;
    }

    @PostMapping
    public ResponseEntity<DirectSaleResponseDTO> createDirectSale(
            @RequestBody DirectSaleRequestDTO requestDTO) {

        DirectSaleResponseDTO responseDTO =
                _directSaleService.createDirectSale(requestDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DirectSaleResponseDTO>> getAllDirectSales() {

        List<DirectSaleResponseDTO> response =
                _directSaleService.getAllDirectSales();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectSaleResponseDTO> getDirectSaleById(
            @PathVariable String id) {

        DirectSaleResponseDTO responseDTO =
                _directSaleService.getDirectSaleById(id);

        return ResponseEntity.ok(responseDTO);
    }

}
