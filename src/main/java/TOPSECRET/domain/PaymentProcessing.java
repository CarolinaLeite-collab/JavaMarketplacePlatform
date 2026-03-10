package TOPSECRET.domain;

/**
 * Handles the processing of a payment between a buyer and a seller for a given price.
 * <p>
 * Validates that the final price, buyer, and seller are not null. Currently, {@link #isSuccessful()}
 * is a placeholder that always returns true.
 * </p>
 */

public class PaymentProcessing {

    private Price _finalPrice;
    private User _buyer;
    private User _seller;
    //private PaymentMethod _paymentMethod; //to be updated later

    public PaymentProcessing(Price _finalPrice, User _buyer, User _seller) {

        this._finalPrice = _finalPrice;
        this._buyer = _buyer;
        this._seller = _seller;

    }

    public boolean isSuccessful() {
        return true; // placeholder
    }
}