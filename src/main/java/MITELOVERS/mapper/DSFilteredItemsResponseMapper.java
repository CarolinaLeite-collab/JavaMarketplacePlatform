package MITELOVERS.mapper;

import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import org.springframework.stereotype.Component;
import java.util.List;

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
