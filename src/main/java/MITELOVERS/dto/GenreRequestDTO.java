package MITELOVERS.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object used to receive genre data from client requests.
 */

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class GenreRequestDTO {

    @JsonProperty("_genreName")
    private final String _genreName;
}
