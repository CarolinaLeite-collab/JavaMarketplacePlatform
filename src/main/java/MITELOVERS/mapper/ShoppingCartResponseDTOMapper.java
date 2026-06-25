package MITELOVERS.mapper;

import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.dto.response.ShoppingCartResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembler that maps a {@link ShoppingCart} domain object to a
 * {@link ShoppingCartResponseDTO}, extracting cart identity, buyer, total amount,
 * and currency. Handles null {@code totalAmount} for empty carts.
 */

@Component
@AllArgsConstructor
public class ShoppingCartResponseDTOMapper implements RepresentationModelAssembler<ShoppingCart, ShoppingCartResponseDTO> {

    @Override
    public ShoppingCartResponseDTO toModel(ShoppingCart shoppingCart) {

        String shoppingCartId = shoppingCart.identity().toString();
        String buyerId = shoppingCart.getBuyerId().toString();

        Price totalAmountPrice = shoppingCart.getTotalAmount();
        Double totalAmount = (totalAmountPrice == null) ? null : totalAmountPrice.getValue();
        String currency = (totalAmountPrice == null) ? null : totalAmountPrice.getCurrency().toString();

        return new ShoppingCartResponseDTO(
                shoppingCartId,
                buyerId,
                totalAmount,
                currency
        );

    }

}
