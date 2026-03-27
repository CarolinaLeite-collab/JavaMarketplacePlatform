package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;

import java.time.Period;

/**
 * Factory responsible for creating {@link DirectSale } instances.
 * <p>
 * @throws IllegalArgumentException if item or price is null, or if timeLimit is negative
 */

public class DirectSaleFactory {

    public DirectSale createDirectSale(Item item, Price price, Period timeLimit) {

        DirectSale newDirectSale = new DirectSale(item, price, timeLimit);
        item.setDirectSale(newDirectSale);
        return newDirectSale;
    }
}