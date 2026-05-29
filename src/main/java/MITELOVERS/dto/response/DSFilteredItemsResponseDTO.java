package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@AllArgsConstructor
public class DSFilteredItemsResponseDTO extends RepresentationModel<DSFilteredItemsResponseDTO> {

    private final List<ItemEntry> items;

    @Getter
    @AllArgsConstructor
    public static class ItemEntry extends RepresentationModel<ItemEntry> {
        private final String itemId;
    }

}
