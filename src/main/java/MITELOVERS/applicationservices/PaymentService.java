package MITELOVERS.applicationservices;

import MITELOVERS.domain.valueobject.Price;
import org.springframework.stereotype.Service;

public interface PaymentService {

    boolean isPaymentSuccessful(Price price);

}
