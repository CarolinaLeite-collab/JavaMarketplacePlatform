package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@AllArgsConstructor
public class FilteredDSItemsResponseDTO extends RepresentationModel<FilteredDSItemsResponseDTO> {

    private final List<String> itemIds;
}
