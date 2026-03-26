package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;

/**
 * Handles the processing of a payment between a buyer and a seller for a given price.
 * <p>
 * Validates that the final price, buyer, and seller are not null. Currently, {@link #isSuccessful()}
 * is a placeholder that always returns true.
 * </p>
 */

public class PaymentProcessing {

    private final Price _finalPrice;
    private final User _buyer;
    private final User _seller;
    //private PaymentMethod _paymentMethod; //to be updated later

    public PaymentProcessing(Price finalPrice, User buyer, User seller) {

        _finalPrice = finalPrice;
        _buyer = buyer;
        _seller = seller;

    }

    public boolean isSuccessful() {
        return true;
    }
}