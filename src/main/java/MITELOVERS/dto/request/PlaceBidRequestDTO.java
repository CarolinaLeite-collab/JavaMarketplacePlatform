package MITELOVERS.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object used to receive bid data from client requests.
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceBidRequestDTO {

    @Positive
    private double offerPrice;

    @NotEmpty
    @Schema(example = "EUR")
    private String currency;
}