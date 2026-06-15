package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Factory responsible for creating {@link ShoppingCartLine} instances.
 */

@Component
public class ShoppingCartLineFactory {

    public ShoppingCartLine createNewShoppingCartLine(DirectSaleId directSaleId,
                                                      UserId sellerId,
                                                      Price priceAtAddition) {

        return new ShoppingCartLine(directSaleId,sellerId,priceAtAddition);

    }

    public ShoppingCartLine createNewShoppingCartLine(ShoppingCartLineId shoppingCartLineId,
                                                      DirectSaleId directSaleId,
                                                      UserId sellerId,
                                                      Price priceAtAddition,
                                                      LocalDateTime addedAt) {

        return new ShoppingCartLine(shoppingCartLineId, directSaleId, sellerId, priceAtAddition, addedAt);

    }

}
