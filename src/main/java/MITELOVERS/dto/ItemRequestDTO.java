package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object used to receive item data from client requests.
 */

@Getter
@AllArgsConstructor
public class ItemRequestDTO {

    private final String editionId;

    private final String condition;

    private final String description;


}
