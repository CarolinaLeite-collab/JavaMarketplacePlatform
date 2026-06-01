package MITELOVERS.mapper;

import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Maps a list of Direct Sale identifiers into a
 * {@link DSFilteredItemsResponseDTO} containing lightweight
 * {@link DSFilteredItemsResponseDTO.DirectSaleEntry} elements.
 *
 * <p>Used by controllers to convert service-layer results into
 * HATEOAS-ready DTOs.</p>
 */

@Component
public class DSFilteredItemsResponseMapper {

    public DSFilteredItemsResponseDTO toDTO(List<String> directSaleIds) {

        List<DSFilteredItemsResponseDTO.DirectSaleEntry> entries =
                directSaleIds.stream()
                        .map(DSFilteredItemsResponseDTO.DirectSaleEntry::new)
                        .toList();

        return new DSFilteredItemsResponseDTO(entries);
    }

}
