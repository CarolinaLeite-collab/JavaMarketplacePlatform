package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.*;

import java.time.LocalDateTime;

public class ShoppingCartLineFactory {

    public ShoppingCartLine createNewShoppingCartLine(DirectSaleId directSaleId,
                                                      ItemId itemId,
                                                      UserId sellerId,
                                                      Price priceAtAddition) {

        return new ShoppingCartLine(directSaleId,itemId,sellerId,priceAtAddition);

    }

    public ShoppingCartLine createNewShoppingCartLine(ShoppingCartLineId shoppingCartLineId,
                                                      DirectSaleId directSaleId,
                                                      ItemId itemId,
                                                      UserId sellerId,
                                                      Price priceAtAddition,
                                                      LocalDateTime addetAt) {

        return new ShoppingCartLine(shoppingCartLineId, directSaleId, itemId, sellerId, priceAtAddition, addetAt);

    }

}
