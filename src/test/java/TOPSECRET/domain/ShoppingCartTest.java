package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void startsEmpty() {
        ShoppingCart cart = new ShoppingCart();
        assertEquals(0.0, cart.getTotalValue());
        assertNull(cart.getCurrency());
        assertTrue(cart.getListings().isEmpty());
    }

    @Test
    void addsListingsAndComputesTotal() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));
        cart.addListing("listing-2", new Price(5.5, Currency.EUR));

        assertEquals(15.5, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
        assertEquals(2, cart.getListings().size());
    }

    @Test
    void enforcesSingleCurrency() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));
        assertThrows(IllegalArgumentException.class,
                () -> cart.addListing("listing-2", new Price(1.0, Currency.USD)));
    }

    @Test
    void preventsDuplicateListingIds() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));
        assertThrows(IllegalArgumentException.class,
                () -> cart.addListing("listing-1", new Price(5.0, Currency.EUR)));
    }

    @Test
    void removesListingsAndResetsCurrencyWhenEmpty() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));
        assertTrue(cart.removeListing("listing-1"));
        assertEquals(0.0, cart.getTotalValue());
        assertNull(cart.getCurrency());
    }

    @Test
    void listingsListIsUnmodifiable() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));
        assertThrows(UnsupportedOperationException.class, () -> cart.getListings().add(null));
    }
}