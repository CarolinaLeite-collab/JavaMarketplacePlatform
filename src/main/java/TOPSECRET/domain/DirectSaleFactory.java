package TOPSECRET.domain;

import java.time.Period;

public class DirectSaleFactory {

    public DirectSale create(Item item, Price price, Period timeLimit)
            throws InstantiationException {
        try {
            return new DirectSale(item, price, timeLimit);
        } catch (Exception e) {
            throw new InstantiationException(
                    "Unable to instantiate DirectSale: " + e.getMessage()
            );
        }
    }
}