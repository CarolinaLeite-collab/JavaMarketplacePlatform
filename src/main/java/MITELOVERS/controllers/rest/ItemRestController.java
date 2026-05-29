package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ItemService;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.dto.ItemRequestDTO;
import MITELOVERS.dto.ItemResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST controller responsible for exposing item-related endpoints via HTTP.
 */

@RestController
@RequestMapping("/items")
public class ItemRestController {

    private final ItemService _itemService;

    public ItemRestController(ItemService itemService) {
        _itemService = itemService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponseDTO> registerItem(@Valid @RequestBody ItemRequestDTO info) {

        try {

            ItemResponseDTO itemResponseDTO = _itemService.registerItem(
                    new EditionId(info.getEditionId()),
                    Condition.valueOf(info.getCondition().toUpperCase()),
                    new Description(info.getDescription())
            );

            return new ResponseEntity<>(itemResponseDTO, HttpStatus.CREATED);

        } catch (IllegalStateException ex) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());

        } catch (IllegalArgumentException ex) {

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {

        List<ItemResponseDTO> items = _itemService.getAllItems();

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable String id) {

        try {

            ItemResponseDTO itemResponseDTO = _itemService.getItemById(id);

            return new ResponseEntity<>(itemResponseDTO, HttpStatus.OK);

        } catch (NoSuchElementException ex) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());

        } catch (IllegalArgumentException ex) {

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }
}
