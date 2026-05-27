package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Generated
@AllArgsConstructor
@Getter
public class ListOfItemsRequestDTO extends RepresentationModel<ListOfItemsRequestDTO> {
    private String name;
    private String genreId;

}
