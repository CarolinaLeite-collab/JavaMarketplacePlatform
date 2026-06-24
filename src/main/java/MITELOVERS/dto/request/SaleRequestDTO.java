package MITELOVERS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a sale from a shopping cart.
 * Validates that the provided {@code shoppingCartId} is non-blank and
 * matches the expected {@code SC-XXXXXXXX} format.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequestDTO {

    @NotBlank
    @Size(min=11, max=11)
    @Pattern(regexp = "SC-[A-F0-9]{8}", message = "Invalid ShoppingCart ID format")
    private String shoppingCartId;

}
