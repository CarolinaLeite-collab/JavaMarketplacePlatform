package TOPSECRET.domain;

import java.time.Period;

public class DirectSaleFactory {

    public DirectSale createDirectSale(Item item, Price price, Period timeLimit) {
        return new DirectSale(item, price, timeLimit);
    }
}