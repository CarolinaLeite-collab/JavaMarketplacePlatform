package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void startsEmpty() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();

        // Act
        double total = cart.getTotalValue();

        // Assert
        assertEquals(0.0, total);
        assertNull(cart.getCurrency());
        assertTrue(cart.getListings().isEmpty());
    }

    @Test
    void addsListingsAndComputesTotal() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();

        // Act
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));
        cart.addListing("listing-2", new Price(5.5, Currency.EUR));

        // Assert
        assertEquals(15.5, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
        assertEquals(2, cart.getListings().size());
    }

    @Test
    void enforcesSingleCurrency() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addListing("listing-2", new Price(1.0, Currency.USD)));
    }

    @Test
    void preventsDuplicateListingIds() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addListing("listing-1", new Price(5.0, Currency.EUR)));
    }

    @Test
    void removesListingsAndResetsCurrencyWhenEmpty() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));

        // Act
        boolean removed = cart.removeListing("listing-1");

        // Assert
        assertTrue(removed);
        assertEquals(0.0, cart.getTotalValue());
        assertNull(cart.getCurrency());
    }

    @Test
    void listingsListIsUnmodifiable() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("listing-1", new Price(10.0, Currency.EUR));

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> cart.getListings().add(null));
    }

    // --- New tests to improve mutation coverage ---

    @Test
    void removeNonExistingListingReturnsFalseAndDoesNotChangeState() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("a", new Price(10.0, Currency.EUR));

        // Act
        boolean removed = cart.removeListing("non-existent");

        // Assert
        assertFalse(removed);
        assertEquals(10.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
        assertEquals(1, cart.getListings().size());
    }

    @Test
    void removeOneOfMultipleListingsUpdatesTotalAndKeepsCurrency() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("l1", new Price(10.0, Currency.EUR));
        cart.addListing("l2", new Price(5.0, Currency.EUR));

        // Act
        boolean removed = cart.removeListing("l1");

        // Assert
        assertTrue(removed);
        assertEquals(5.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
        assertEquals(1, cart.getListings().size());
    }

    @Test
    void removingLastListingResetsCurrencyAndAllowsDifferentCurrencyAfterwards() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("only", new Price(7.0, Currency.EUR));

        // Act
        boolean removedOnly = cart.removeListing("only");

        // Assert
        assertTrue(removedOnly);
        assertEquals(0.0, cart.getTotalValue());
        assertNull(cart.getCurrency());

        // Arrange
        cart.addListing("new", new Price(3.0, Currency.USD));

        // Assert
        assertEquals(3.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.USD, cart.getCurrency());
        assertEquals(1, cart.getListings().size());
    }

    @Test
    void addingNullArgumentsThrows() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cart.addListing(null, new Price(1.0, Currency.EUR)));
        assertThrows(RuntimeException.class, () -> cart.addListing("id", null));
    }

    @Test
    void sequenceOperationsMaintainCorrectTotalsAndCurrencyTransitions() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("l1", new Price(10.0, Currency.EUR));
        cart.addListing("l2", new Price(19.99, Currency.EUR));
        cart.addListing("l3", new Price(0.01, Currency.EUR));

        // Act
        assertTrue(cart.removeListing("l2"));
        assertTrue(cart.removeListing("l3"));
        assertTrue(cart.removeListing("l1"));

        // Assert
        assertEquals(0.0, cart.getTotalValue(), 0.0001);
        assertNull(cart.getCurrency());
    }

    @Test
    void unmodifiableListRejectsAllMutations() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("a", new Price(1.0, Currency.EUR));

        // Act & Assert
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
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        cart.addListing("a", new Price(1.0, Currency.EUR));

        // Act
        try {
            boolean r = cart.removeListing(null);
            assertFalse(r);
        } catch (RuntimeException ex) {
            assertTrue(ex instanceof RuntimeException);
        }

        // Assert
        assertEquals(1, cart.getListings().size());
        assertEquals(1.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
    }

    @Test
    void addsMultipleSmallPricesAccurately() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();

        // Act
        cart.addListing("p1", new Price(0.1, Currency.USD));
        cart.addListing("p2", new Price(0.2, Currency.USD));
        cart.addListing("p3", new Price(0.3, Currency.USD));

        // Assert
        assertEquals(0.6, cart.getTotalValue(), 0.0001);
    }

    @Test
    void samePriceObjectAllowedForDifferentIds() {
        // Arrange
        ShoppingCart cart = new ShoppingCart();
        Price p = new Price(2.5, Currency.EUR);

        // Act
        cart.addListing("a", p);
        cart.addListing("b", p);

        // Assert
        assertEquals(5.0, cart.getTotalValue(), 0.0001);
        assertEquals(2, cart.getListings().size());
    }

    // New tests to exercise CartLine.equals / hashCode branches that were uncovered

    @Test
    void cartLineEqualsAndHashCodeConsistency() {
        // Arrange
        Price p = new Price(2.0, Currency.EUR);
        ShoppingCart.CartLine a = new ShoppingCart.CartLine("x", p);
        ShoppingCart.CartLine b = new ShoppingCart.CartLine("x", p);

        // Act
        boolean same = a.equals(b);

        // Assert
        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(java.util.Objects.hash("x", p), a.hashCode());
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("x", a.getListingId());
        assertEquals(p, a.getPrice());
    }

    @Test
    void cartLineNotEqualsDifferentIdOrPriceAndNonCartLine() {
        // Arrange
        Price p1 = new Price(2.0, Currency.EUR);
        Price p2 = new Price(3.0, Currency.EUR);
        ShoppingCart.CartLine base = new ShoppingCart.CartLine("x", p1);
        ShoppingCart.CartLine diffId = new ShoppingCart.CartLine("y", p1);
        ShoppingCart.CartLine diffPrice = new ShoppingCart.CartLine("x", p2);

        // Assert
        assertNotEquals(base, diffId);
        assertNotEquals(base, diffPrice);
        assertFalse(base.equals(null));
        assertFalse(base.equals("not-a-cartline"));
    }
}
