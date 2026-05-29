package MITELOVERS.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to receive publication data from client requests.
 * </p>
 */

@Getter
@AllArgsConstructor
public class PublicationRequestDTO {

    @NotBlank
    private final String title;

    @NotBlank
    private final String authorId;

    @Min(1000)
    @Max(2100)
    private final int releaseYear;

    @NotBlank
    private final String genreId;

}
