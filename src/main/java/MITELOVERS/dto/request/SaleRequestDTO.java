package MITELOVERS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
