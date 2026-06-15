package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.shoppingcart.ShoppingCartLineFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartLineDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Assembler responsible for converting between {@link ShoppingCartLine} domain objects
 * and {@link ShoppingCartLineDataModel} persistence representations.
 */

@Component
@AllArgsConstructor
public class ShoppingCartLineAssembler {

    private ShoppingCartLineFactory _shoppingCartLineFactory;

    public ShoppingCartLine toDomain(ShoppingCartLineDataModel shoppingCartLineDM) {

        if(shoppingCartLineDM == null) {
            throw new IllegalArgumentException("ShoppingCartLineDataModel cannot be null!");
        }

        ShoppingCartLineId shoppingCartLineId = new ShoppingCartLineId(shoppingCartLineDM.getShoppingCartLineId());
        DirectSaleId directSaleId = new DirectSaleId(shoppingCartLineDM.getDirectSaleId());
        UserId sellerId = new UserId(new Email(shoppingCartLineDM.getSellerId()));
        Price priceAtAddition = new Price(
                shoppingCartLineDM.getPriceAtAddition().getNumericValue(),
                Currency.valueOf(shoppingCartLineDM.getPriceAtAddition().getCurrency())
        );
        LocalDateTime addedAt = shoppingCartLineDM.getAddedAt();

        return _shoppingCartLineFactory.createNewShoppingCartLine(shoppingCartLineId, directSaleId, sellerId, priceAtAddition, addedAt);

    }

    public ShoppingCartLineDataModel toDataModel(ShoppingCartLine shoppingCartLine) {

        if(shoppingCartLine == null) {
            throw new IllegalArgumentException("ShoppingCartLine cannot be null!");
        }

        String shoppingCartLineId = shoppingCartLine.identity().toString();
        String directSaleId = shoppingCartLine.getDirectSaleId().toString();
        String sellerId = shoppingCartLine.getSellerId().toString();
        PriceDataModel priceAtAddition = new PriceDataModel(
                shoppingCartLine.getPriceAtAddition().getValue(),
                shoppingCartLine.getPriceAtAddition().getCurrency().toString()
        );
        LocalDateTime addetAt = shoppingCartLine.getAddedAt();

        return new ShoppingCartLineDataModel(shoppingCartLineId,directSaleId,sellerId,priceAtAddition,addetAt);

    }

}
