package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response DTO representing a shopping cart, including the buyer, total amount,
 * and currency. {@code totalAmount} and {@code currency} are null when the cart
 * is empty. Extends {@link RepresentationModel} to support HATEOAS links.
 */

@Getter
@Generated
@AllArgsConstructor
public class ShoppingCartResponseDTO extends RepresentationModel<ShoppingCartResponseDTO> {

    private String shoppingCartId;
    private String buyerId;
    private Double totalAmount;
    private String currency;

}
