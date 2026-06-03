package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Generated
@AllArgsConstructor
@Getter
public class ListOfItemsResponseDTO extends RepresentationModel<ListOfItemsResponseDTO> {
    private String listId;
    private String userId;
    private String name;
    private String genreId;
    private boolean isPrivate;
    private LocalDateTime sharedUntil;
    private List<String> itemsId;

}

