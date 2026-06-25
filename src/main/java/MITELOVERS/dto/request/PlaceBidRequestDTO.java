package MITELOVERS.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object used to receive bid data from client requests.
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceBidRequestDTO {

    @NotNull
    @Positive
    @JsonProperty("bidValue")
    private double offerPrice;

    @NotBlank
    @Schema(example = "EUR")
    private String currency;
}