package MITELOVERS.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object used to receive genre data from client requests.
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenreRequestDTO {

    private String genreName;
}
