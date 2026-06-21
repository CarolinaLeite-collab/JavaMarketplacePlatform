package MITELOVERS.applicationservices;

import MITELOVERS.domain.valueobject.Price;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class FakePaymentService implements PaymentService{

    @Override
    public boolean isPaymentSuccessful(Price price) {
        return true;
    }
}
