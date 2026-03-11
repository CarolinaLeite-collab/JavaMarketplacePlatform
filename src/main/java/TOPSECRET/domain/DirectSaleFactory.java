package TOPSECRET.domain;

import java.time.Period;

/**
 * Factory responsible for creating {@link DirectSale } instances.
 * <p>
 * @throws IllegalArgumentException if item or price is null, or if timeLimit is negative
 */

public class DirectSaleFactory {

    public DirectSale createDirectSale(Item item, Price price, Period timeLimit) {
        return new DirectSale(item, price, timeLimit);
    }
}