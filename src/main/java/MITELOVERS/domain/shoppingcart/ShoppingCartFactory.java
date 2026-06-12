package MITELOVERS.domain.shoppingcart;

import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.List;

public class ShoppingCartFactory {

    public ShoppingCart createShoppingCart(UserId buyerId) {

        return new ShoppingCart(buyerId);

    }

    public ShoppingCart createShoppingCart(ShoppingCartId cartId,
                                           UserId buyerId,
                                           Price totalAmount,
                                           List<ShoppingCartLine> cartItems) {

        return new ShoppingCart(cartId, buyerId, totalAmount, cartItems);

    }

}
