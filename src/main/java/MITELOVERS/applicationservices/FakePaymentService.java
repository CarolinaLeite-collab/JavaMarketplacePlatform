package MITELOVERS.applicationservices;

import MITELOVERS.domain.valueobject.Price;
import org.springframework.stereotype.Service;

/**
 * Fake implementation of {@link PaymentService} for development and testing purposes.
 * Always approves payment regardless of the provided price, simulating a successful
 * transaction without any real payment processing.
 */

@Service
public class FakePaymentService implements PaymentService{

    @Override
    public boolean isPaymentSuccessful(Price price) {
        return true;
    }
}
