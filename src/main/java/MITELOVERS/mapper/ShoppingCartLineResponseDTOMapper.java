package MITELOVERS.mapper;

import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.dto.response.ShoppingCartLineResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembler that maps a {@link ShoppingCartLine} domain object to a
 * {@link ShoppingCartLineResponseDTO}, extracting line identity, associated
 * direct sale, seller, price at addition, currency, and timestamp.
 */

@Component
@AllArgsConstructor
public class ShoppingCartLineResponseDTOMapper implements RepresentationModelAssembler<ShoppingCartLine, ShoppingCartLineResponseDTO> {

    @Override
    public ShoppingCartLineResponseDTO toModel(ShoppingCartLine shoppingCartLine) {

        String shoppingCartLineId = shoppingCartLine.identity().toString();
        String directSaleId = shoppingCartLine.getDirectSaleId().toString();
        String sellerId = shoppingCartLine.getSellerId().toString();
        double priceAtAddition = shoppingCartLine.getPriceAtAddition().getValue();
        String currency = shoppingCartLine.getPriceAtAddition().getCurrency().toString();
        String addedAt = shoppingCartLine.getAddedAt().toString();

        return new ShoppingCartLineResponseDTO(
                shoppingCartLineId,
                directSaleId,
                sellerId,
                priceAtAddition,
                currency,
                addedAt
                );

    }

}
