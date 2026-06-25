package MITELOVERS.applicationservices;

import MITELOVERS.domain.valueobject.Price;

/**
 * Contract for payment processing.
 * Implementations determine whether a payment for a given {@link Price} is approved,
 * allowing the checkout flow to proceed or cancel accordingly.
 */

public interface PaymentService {

    boolean isPaymentSuccessful(Price price);

}
