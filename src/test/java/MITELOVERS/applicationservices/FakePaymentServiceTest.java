package MITELOVERS.applicationservices;

import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FakePaymentServiceTest {

    @Test
    void testAConstructor() {

        new FakePaymentService();

    }

    @Test
    void testIsPaymentSuccessful() {

        //Arrange
        Price price = mock(Price.class);

        //SUT
        FakePaymentService service = new FakePaymentService();

        //Act
        boolean result = service.isPaymentSuccessful(price);

        //Assert
        assertTrue(result);

    }
}