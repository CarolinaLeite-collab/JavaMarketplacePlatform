package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@AllArgsConstructor
public class DSFilteredItemsResponseDTO extends RepresentationModel<DSFilteredItemsResponseDTO> {

    private final List<String> itemsId;
}
