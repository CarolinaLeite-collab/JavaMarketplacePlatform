package TOPSECRET.domain;

public class PaymentProcessing {

    private Price _finalPrice;
    private User _buyer;
    private User _seller;
    private PaymentMethod _paymentMethod; //to be updated

    public PaymentProcessing(Price _finalPrice, User _buyer, User _seller, PaymentMethod _paymentMethod) {
        if (_finalPrice == null || _buyer == null || _seller == null || _paymentMethod == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        this._finalPrice = _finalPrice;
        this._buyer = _buyer;
        this._seller = _seller;
        this._paymentMethod = _paymentMethod;
    }

    public boolean isSuccessful() {
        return true; // placeholder
    }
}
