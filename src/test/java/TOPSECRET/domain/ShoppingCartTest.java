package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Currency;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(10.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(5.5);
        when(priceDouble2.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        // Act
        cart.addListing("listing-1", priceDouble1);
        cart.addListing("listing-2", priceDouble2);

        // Assert
        assertEquals(15.5, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.EUR, cart.getCurrency());
        assertEquals(2, cart.getListings().size());
    }

    @Test
    void enforcesSingleCurrency() {
        // Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(30.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(5.5);
        when(priceDouble2.getCurrency()).thenReturn(Currency.USD);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("listing-1", priceDouble1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addListing("listing-2", priceDouble2));
    }

    @Test
    void preventsDuplicateListingIds() {
        // Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(30.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(5.5);
        when(priceDouble2.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("listing-1", priceDouble1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addListing("listing-1", priceDouble2));
    }

    @Test
    void removesListingsAndResetsCurrencyWhenEmpty() {
        // Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(30.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("listing-1", priceDouble1);

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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(30.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("listing-1", priceDouble1);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> cart.getListings().add(null));
    }

    @Test
    void removeNonExistingListingReturnsFalseAndDoesNotChangeState() {
        // Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(10.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        // Act
        cart.addListing("a", priceDouble1);
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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(30.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(5.0);
        when(priceDouble2.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("l1", priceDouble1);
        cart.addListing("l2", priceDouble2);
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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(30.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.USD);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(3.0);
        when(priceDouble2.getCurrency()).thenReturn(Currency.USD);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("only", priceDouble1);
        boolean removedOnly = cart.removeListing("only");

        // Assert
        assertTrue(removedOnly);
        assertEquals(0.0, cart.getTotalValue());
        assertNull(cart.getCurrency());

        // Arrange
        cart.addListing("new", priceDouble2);

        // Assert
        assertEquals(3.0, cart.getTotalValue(), 0.0001);
        assertEquals(Currency.USD, cart.getCurrency());
        assertEquals(1, cart.getListings().size());
    }

    @Test
    void addingNullArgumentsThrows() {
        //Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(30.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        // SUT
        ShoppingCart cart = new ShoppingCart();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cart.addListing(null, priceDouble1));
        assertThrows(RuntimeException.class, () -> cart.addListing("id", null));
    }

    @Test
    void sequenceOperationsMaintainCorrectTotalsAndCurrencyTransitions() {
        // Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(10.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(19.99);
        when(priceDouble2.getCurrency()).thenReturn(Currency.EUR);

        Price priceDouble3 = mock(Price.class);
        when(priceDouble3.getValue()).thenReturn(0.01);
        when(priceDouble3.getCurrency()).thenReturn(Currency.EUR);

        ShoppingCart cart = new ShoppingCart();
        cart.addListing("l1", priceDouble1);
        cart.addListing("l2", priceDouble2);
        cart.addListing("l3", priceDouble3);

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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(5.5);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("a", priceDouble1);

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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(1.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        //Act
        cart.addListing("a", priceDouble1);

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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(0.1);
        when(priceDouble1.getCurrency()).thenReturn(Currency.USD);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(0.2);
        when(priceDouble2.getCurrency()).thenReturn(Currency.USD);

        Price priceDouble3 = mock(Price.class);
        when(priceDouble3.getValue()).thenReturn(0.3);
        when(priceDouble3.getCurrency()).thenReturn(Currency.USD);

        //SUT
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
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(2.5);
        when(priceDouble1.getCurrency()).thenReturn(Currency.USD);

        //SUT
        ShoppingCart cart = new ShoppingCart();

        // Act
        cart.addListing("a", priceDouble1);
        cart.addListing("b", priceDouble1);

        // Assert
        assertEquals(5.0, cart.getTotalValue(), 0.0001);
        assertEquals(2, cart.getListings().size());
    }

    // New tests to exercise CartLine.equals / hashCode branches that were uncovered

    @Test
    void cartLineEqualsAndHashCodeConsistency() {
        // Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(5.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.USD);

        //SUT
        ShoppingCart.CartLine a = new ShoppingCart.CartLine("x", priceDouble1);
        ShoppingCart.CartLine b = new ShoppingCart.CartLine("x", priceDouble1);

        // Act
        boolean same = a.equals(b);

        // Assert
        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(java.util.Objects.hash("x", priceDouble1), a.hashCode());
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("x", a.getListingId());
        assertEquals(priceDouble1, a.getPrice());
    }

    @Test
    void cartLineNotEqualsDifferentIdOrPriceAndNonCartLine() {
        // Arrange
        Price priceDouble1 = mock(Price.class);
        when(priceDouble1.getValue()).thenReturn(2.0);
        when(priceDouble1.getCurrency()).thenReturn(Currency.EUR);

        Price priceDouble2 = mock(Price.class);
        when(priceDouble2.getValue()).thenReturn(3.0);
        when(priceDouble2.getCurrency()).thenReturn(Currency.EUR);

        //SUT
        ShoppingCart.CartLine base = new ShoppingCart.CartLine("x", priceDouble1);
        ShoppingCart.CartLine diffId = new ShoppingCart.CartLine("y", priceDouble1);
        ShoppingCart.CartLine diffPrice = new ShoppingCart.CartLine("x", priceDouble2);

        // Assert
        assertNotEquals(base, diffId);
        assertNotEquals(base, diffPrice);
        assertFalse(base.equals(null));
        assertFalse(base.equals("not-a-cartline"));
    }
}
