package MITELOVERS.persistence.jpa.assembler;


import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartFactory;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartDataModel;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartLineDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler responsible for converting between {@link ShoppingCart} domain objects
 * and {@link ShoppingCartDataModel} persistence representations.
 */

@Component
@AllArgsConstructor
public class ShoppingCartAssembler {

    private ShoppingCartFactory _shoppingCartFactory;
    private ShoppingCartLineAssembler _shoppingCartLineAssembler;

    public ShoppingCart toDomain(ShoppingCartDataModel shoppingCartDM) {

        if (shoppingCartDM == null) {
            throw new IllegalArgumentException("ShoppingCart cannot be null");
        }

        ShoppingCartId shoppingCartId = new ShoppingCartId(shoppingCartDM.getShoppingCartId());
        UserId buyerId = new UserId(new Email(shoppingCartDM.getBuyerId()));

        PriceDataModel totalAmountDM = shoppingCartDM.getTotalAmount();
        Price totalAmount = (totalAmountDM == null)
                ? null
                : new Price(totalAmountDM.getNumericValue(), Currency.valueOf(totalAmountDM.getCurrency()));

        List<ShoppingCartLine> shoppingCartLines = new ArrayList<>();
        for (ShoppingCartLineDataModel cartLine : shoppingCartDM.getShoppingCartLines()) {
            shoppingCartLines.add(_shoppingCartLineAssembler.toDomain(cartLine));
        }

        return _shoppingCartFactory.createShoppingCart(
                shoppingCartId,
                buyerId,
                totalAmount,
                shoppingCartLines
        );
    }

    public ShoppingCartDataModel toDataModel(ShoppingCart shoppingCart) {

        if (shoppingCart == null) {
            throw new IllegalArgumentException("ShoppingCartDataModel cannot be null");
        }

        String shoppingCartId = shoppingCart.identity().toString();
        String buyerId = shoppingCart.getBuyerId().toString();

        Price totalAmountPrice = shoppingCart.getTotalAmount();
        PriceDataModel totalAmountDm = (totalAmountPrice == null)
                ? null
                : new PriceDataModel(totalAmountPrice.getValue(), totalAmountPrice.getCurrency().toString());

        List<ShoppingCartLineDataModel> cartLineDMs = new ArrayList<>();
        for (ShoppingCartLine cartLine : shoppingCart.getCartLines()) {
            cartLineDMs.add(_shoppingCartLineAssembler.toDataModel(cartLine));
        }

        return new ShoppingCartDataModel(
                shoppingCartId,
                buyerId,
                totalAmountDm,
                cartLineDMs
        );
    }


}
