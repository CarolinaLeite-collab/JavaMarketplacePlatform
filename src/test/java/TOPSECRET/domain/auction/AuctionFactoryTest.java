package TOPSECRET.domain.auction;

import TOPSECRET.domain.Item;
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
        List<Item> items = new ArrayList<>();
        Item itemDouble = mock(Item.class);
        items.add(itemDouble);
        Price startingPrice = mock(Price.class);
        Price reservePrice = mock(Price.class);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = start.plusDays(1);
        List<List<Object>> capturedArguments = new ArrayList<>();

        // SUT
        AuctionFactory factory = new AuctionFactory();


        try (MockedConstruction<Auction> mocked = mockConstruction(Auction.class,
                (mock, context) -> capturedArguments.add(new ArrayList<>(context.arguments())))) {

            // Act
            Auction created = factory.createAuction(items, startingPrice, reservePrice,start, end);

            // Assert
            assertSame(mocked.constructed().get(0), created);
            assertEquals(1, capturedArguments.size());
            List<Object> params = capturedArguments.get(0);
            assertSame(items, params.get(0));
            assertSame(startingPrice, params.get(1));
            assertSame(reservePrice, params.get(2));
            assertSame(start, params.get(3));
            assertSame(end, params.get(4));
        }
    }

    @Test
    void shouldConstructAuctionWithOutright() {
        // Arrange
        List<Item> items = new ArrayList<>();
        Item itemDouble = mock(Item.class);
        items.add(itemDouble);
        Price startingPrice = mock(Price.class);
        Price reservePrice = mock(Price.class);
        Price outrightPrice = mock(Price.class);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = start.plusDays(1);
        List<List<Object>> capturedArguments = new ArrayList<>();

        // SUT
        AuctionFactory factory = new AuctionFactory();

        try (MockedConstruction<Auction> mocked = mockConstruction(Auction.class,
                (mock, context) -> capturedArguments.add(new ArrayList<>(context.arguments())))) {

            // Act
            Auction created = factory.createAuction(items, startingPrice, reservePrice,outrightPrice, start, end);

            // Assert
            assertSame(mocked.constructed().get(0), created);
            assertEquals(1, capturedArguments.size());
            List<Object> params = capturedArguments.get(0);
            assertSame(items, params.get(0));
            assertSame(startingPrice, params.get(1));
            assertSame(reservePrice, params.get(2));
            assertSame(outrightPrice, params.get(3));
            assertSame(start, params.get(4));
            assertSame(end, params.get(5));
        }
    }
}
