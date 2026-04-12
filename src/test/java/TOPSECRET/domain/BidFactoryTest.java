package TOPSECRET.domain;

import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BidFactoryTest {
    @Test
    void shouldSuccessfullyCreateBid() throws IllegalArgumentException {
        // arrange
        UserId userIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);

        //SUT
        BidFactory bidFactory = new BidFactory();

        try (MockedConstruction<Bid> mocked =
                     mockConstruction(Bid.class,
                             (mock, context) -> {
                                 when(mock.getOfferPrice()).thenReturn(priceDouble);
                             })) {
            // act
            Bid newBid = bidFactory.createBid(userIdDouble, priceDouble);
            //assert
            assertEquals(priceDouble, newBid.getOfferPrice());
        }
    }

    @Test
    void shouldThrowExceptionWhenCreatingBidWithInvalidPrice() throws IllegalArgumentException {
        // arrange
        UserId userIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);

        //SUT
        BidFactory bidFactory = new BidFactory();

        double value = 0;
        when(priceDouble.getValue()).thenReturn(value);
        try (MockedConstruction<Bid> mocked =
                     mockConstruction(Bid.class,
                             (mock, context) -> {
                                 when(mock.getOfferPrice()).thenReturn(priceDouble);
                             })) {
            // act
            Bid newBid = bidFactory.createBid(userIdDouble, priceDouble);
            //assert
            assertEquals(priceDouble, newBid.getOfferPrice());
        }
    }
}