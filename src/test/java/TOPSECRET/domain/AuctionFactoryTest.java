package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class AuctionFactoryTest {

    @Test
    void shouldConstructAuctionWithoutOutright() {
        // Arrange
        Item item = mock(Item.class);
        Price startingPrice = mock(Price.class);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = start.plusDays(1);
        List<List<Object>> capturedArguments = new ArrayList<>();

        // SUT
        AuctionFactory factory = new AuctionFactory();


        try (MockedConstruction<Auction> mocked = mockConstruction(Auction.class,
                (mock, context) -> capturedArguments.add(new ArrayList<>(context.arguments())))) {

            // Act
            Auction created = factory.createAuction(item, startingPrice, start, end);

            // Assert
            assertSame(mocked.constructed().get(0), created);
            assertEquals(1, capturedArguments.size());
            List<Object> params = capturedArguments.get(0);
            assertSame(item, params.get(0));
            assertSame(startingPrice, params.get(1));
            assertSame(start, params.get(2));
            assertSame(end, params.get(3));
        }
    }

    @Test
    void shouldConstructAuctionWithOutright() {
        // Arrange
        Item item = mock(Item.class);
        Price startingPrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = start.plusDays(1);
        List<List<Object>> capturedArguments = new ArrayList<>();

        // SUT
        AuctionFactory factory = new AuctionFactory();

        try (MockedConstruction<Auction> mocked = mockConstruction(Auction.class,
                (mock, context) -> capturedArguments.add(new ArrayList<>(context.arguments())))) {

            // Act
            Auction created = factory.createAuction(item, startingPrice, outrightPrice, start, end);

            // Assert
            assertSame(mocked.constructed().get(0), created);
            assertEquals(1, capturedArguments.size());
            List<Object> params = capturedArguments.get(0);
            assertSame(item, params.get(0));
            assertSame(startingPrice, params.get(1));
            assertSame(outrightPrice, params.get(2));
            assertSame(start, params.get(3));
            assertSame(end, params.get(4));
        }
    }
}
