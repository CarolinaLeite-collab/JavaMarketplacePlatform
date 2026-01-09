package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

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

    // --- New tests to improve mutation coverage ---

    @Test
    void removeNonExistingListingReturnsFalseAndDoesNotChangeState() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("a", new Price(10.0, Currency.EUR));

        boolean removed = cart.removeListing("non-existent");
        assertFalse(removed);
        // state should remain unchanged
        assertEquals(10.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
        assertEquals(1, cart.getListings().size());
    }

    @Test
    void removeOneOfMultipleListingsUpdatesTotalAndKeepsCurrency() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("l1", new Price(10.0, Currency.EUR));
        cart.addListing("l2", new Price(5.0, Currency.EUR));

        assertTrue(cart.removeListing("l1"));
        assertEquals(5.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
        assertEquals(1, cart.getListings().size());
    }

    @Test
    void removingLastListingResetsCurrencyAndAllowsDifferentCurrencyAfterwards() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("only", new Price(7.0, Currency.EUR));
        assertTrue(cart.removeListing("only"));
        assertEquals(0.0, cart.getTotalValue());
        assertNull(cart.getCurrency());

        // after reset we should be able to add a different currency
        cart.addListing("new", new Price(3.0, Currency.USD));
        assertEquals(3.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.USD, cart.getCurrency());
        assertEquals(1, cart.getListings().size());
    }

    @Test
    void addingNullArgumentsThrows() {
        ShoppingCart cart = new ShoppingCart();
        // Accept any RuntimeException to be robust against implementation choice (NPE or IAE)
        assertThrows(RuntimeException.class, () -> cart.addListing(null, new Price(1.0, Currency.EUR)));
        assertThrows(RuntimeException.class, () -> cart.addListing("id", null));
    }

    @Test
    void sequenceOperationsMaintainCorrectTotalsAndCurrencyTransitions() {
        ShoppingCart cart = new ShoppingCart();
        // adjust to avoid zero-priced item (Price disallows zero) while keeping totals equivalent
        cart.addListing("l1", new Price(10.0, Currency.EUR));
        cart.addListing("l2", new Price(19.99, Currency.EUR));
        cart.addListing("l3", new Price(0.01, Currency.EUR));
        assertEquals(30.0, cart.getTotalValue(), 0.0001);

        // remove middle
        assertTrue(cart.removeListing("l2"));
        // remaining: l1 + l3 = 10.01
        assertEquals(10.01, cart.getTotalValue(), 0.0001);

        // remove small-priced item
        assertTrue(cart.removeListing("l3"));
        assertEquals(10.0, cart.getTotalValue(), 0.0001);

        // remove last -> cart becomes empty and currency resets
        assertTrue(cart.removeListing("l1"));
        assertEquals(0.0, cart.getTotalValue(), 0.0001);
        assertNull(cart.getCurrency());
    }

    @Test
    void unmodifiableListRejectsAllMutations() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("a", new Price(1.0, Currency.EUR));

        // use null (compatible with CartLine) instead of a String to match the list generic type
        assertThrows(UnsupportedOperationException.class, () -> cart.getListings().add(null));
        assertThrows(UnsupportedOperationException.class, () -> cart.getListings().remove(0));
        assertThrows(UnsupportedOperationException.class, () -> cart.getListings().clear());

        assertThrows(UnsupportedOperationException.class, () -> {
            Iterator<?> it = cart.getListings().iterator();
            it.next();
            it.remove();
        });
    }

    @Test
    void removeNullIdEitherReturnsFalseOrThrowsButLeavesStateUnchanged() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("a", new Price(1.0, Currency.EUR));

        try {
            boolean r = cart.removeListing(null);
            // if implementation returns a boolean, expect false for null id
            assertFalse(r);
        } catch (RuntimeException ex) {
            // some implementations may throw on null, that's acceptable
        }

        // state must remain intact
        assertEquals(1, cart.getListings().size());
        assertEquals(1.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
    }

    @Test
    void addsMultipleSmallPricesAccurately() {
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("p1", new Price(0.1, Currency.USD));
        cart.addListing("p2", new Price(0.2, Currency.USD));
        cart.addListing("p3", new Price(0.3, Currency.USD));

        assertEquals(0.6, cart.getTotalValue(), 0.0001);
    }

    @Test
    void samePriceObjectAllowedForDifferentIds() {
        ShoppingCart cart = new ShoppingCart();
        Price p = new Price(2.5, Currency.EUR);
        cart.addListing("a", p);
        cart.addListing("b", p);

        assertEquals(5.0, cart.getTotalValue(), 0.0001);
        assertEquals(2, cart.getListings().size());
    }

    // New tests to exercise CartLine.equals / hashCode branches that were uncovered

    @Test
    void cartLineEqualsAndHashCodeConsistency() {
        Price p = new Price(2.0, Currency.EUR);
        ShoppingCart.CartLine a = new ShoppingCart.CartLine("x", p);
        ShoppingCart.CartLine b = new ShoppingCart.CartLine("x", p);

        // same-reference case (this == o)
        assertEquals(a, a);

        // equal content case
        assertEquals(a, b);

        // ensure hashCode uses the same Objects.hash combination (detects mutants returning 0)
        assertEquals(java.util.Objects.hash("x", p), a.hashCode());

        // equal objects must have equal hash codes
        assertEquals(a.hashCode(), b.hashCode());

        // accessors
        assertEquals("x", a.getListingId());
        assertEquals(p, a.getPrice());
    }

    @Test
    void cartLineNotEqualsDifferentIdOrPriceAndNonCartLine() {
        Price p1 = new Price(2.0, Currency.EUR);
        Price p2 = new Price(3.0, Currency.EUR);
        ShoppingCart.CartLine base = new ShoppingCart.CartLine("x", p1);
        ShoppingCart.CartLine diffId = new ShoppingCart.CartLine("y", p1);
        ShoppingCart.CartLine diffPrice = new ShoppingCart.CartLine("x", p2);

        // different id -> not equal
        assertNotEquals(base, diffId);

        // different price -> not equal
        assertNotEquals(base, diffPrice);

        // null and unrelated types -> not equal / false
        assertFalse(base.equals(null));
        assertFalse(base.equals("not-a-cartline"));
    }
}
