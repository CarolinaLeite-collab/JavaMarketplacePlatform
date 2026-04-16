package TOPSECRET.domain.directsale;

import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

/**
 * Factory responsible for creating {@link DirectSale } instances.
 */

public class DirectSaleFactory {

    public DirectSale createDirectSale(List<ItemId> itemsId, Price price, Period timeLimit) {

        DirectSale newDirectSale = new DirectSale(itemsId, price, timeLimit);

        return newDirectSale;
    }
}