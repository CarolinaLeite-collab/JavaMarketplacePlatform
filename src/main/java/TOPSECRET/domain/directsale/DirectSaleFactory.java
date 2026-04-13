package TOPSECRET.domain.directsale;

import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

/**
 * Factory responsible for creating {@link DirectSale } instances.
 */

public class DirectSaleFactory {

    public DirectSale createDirectSale(List<Item> items, Price price, Period timeLimit) {

        DirectSale newDirectSale = new DirectSale(items, price, timeLimit);

        for(Item item : items) {
            item.markAsDirectSale();
        }

        return newDirectSale;
    }
}