package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BidFactoryTest {
    @Test
    void shouldSuccessfullyCreateBid() throws IllegalArgumentException {
        // arrange
        User user = mock(User.class);
        Price price = mock(Price.class);
        try (MockedConstruction<Bid> mocked =
                     mockConstruction(Bid.class,
                             (mock, context) -> {
                                 when(mock.getOfferPrice()).thenReturn(price);
                             })) {
            BidFactory bidFactory = new BidFactory();
            // act
            Bid newBid = bidFactory.createBid(user, price);
            //assert
            assertEquals(price, newBid.getOfferPrice());
        }
    }

    @Test
    void shouldThrowExceptionWhenCreatingBidWithInvalidPrice() throws IllegalArgumentException {
        // arrange
        User user = mock(User.class);
        Price price = mock(Price.class);
        double value = 0;
        when(price.getValue()).thenReturn(value);
        try (MockedConstruction<Bid> mocked =
                     mockConstruction(Bid.class,
                             (mock, context) -> {
                                 when(mock.getOfferPrice()).thenReturn(price);
                             })) {
            BidFactory bidFactory = new BidFactory();
            // act
            Bid newBid = bidFactory.createBid(user, price);
            //assert
            assertEquals(price, newBid.getOfferPrice());
        }
    }
}