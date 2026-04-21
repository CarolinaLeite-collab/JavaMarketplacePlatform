package MITELOVERS.domain.directsale;

import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;

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
