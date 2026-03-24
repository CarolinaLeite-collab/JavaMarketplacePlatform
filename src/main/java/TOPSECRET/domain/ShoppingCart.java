package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Currency;
import TOPSECRET.domain.valueobject.Price;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shopping cart holding listings (by id) for direct purchase.
 * Enforces a single currency across all entries and unique listing IDs.
 */
public class ShoppingCart {

    private final List<CartLine> _listings = new ArrayList<>();
    private Currency _currency; // currency of the first added listing

    /**
     * Adds a listing priced in a given currency. All entries must share the same currency.
     *
     * @param listingId external identifier of the listing
     * @param price price of the listing, used to enforce currency consistency
     * @throws IllegalArgumentException when inputs are invalid or duplicate is found
     */
    public void addListing(String listingId, Price price) {
        if (listingId == null || listingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Listing id cannot be null or blank");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (_currency == null) {
            _currency = price.getCurrency();
        } else if (_currency != price.getCurrency()) {
            throw new IllegalArgumentException("All listings must use the same currency");
        }

        String normalizedId = listingId.trim();
        boolean exists = _listings.stream().anyMatch(l -> l.getListingId().equals(normalizedId));
        if (exists) {
            throw new IllegalArgumentException("Listing already in cart");
        }

        _listings.add(new CartLine(normalizedId, price));
    }

    /**
     * Removes a listing by id. Returns true if it was present.
     *
     * @param listingId identifier of the listing to remove
     * @return {@code true} when the listing was removed, {@code false} otherwise
     */
    public boolean removeListing(String listingId) {
        if (listingId == null) return false;
        boolean removed = _listings.removeIf(l -> l.getListingId().equals(listingId.trim()));
        if (removed && _listings.isEmpty()) {
            _currency = null;
        }
        return removed;
    }

    /**
     * Unmodifiable view of cart listings.
     */
    public List<CartLine> getListings() {
        return Collections.unmodifiableList(_listings);
    }

    /**
     * Sum of all listing prices. Returns 0 for an empty cart.
     */
    public double getTotalValue() {
        return _listings.stream().mapToDouble(l -> l.getPrice().getValue()).sum();
    }

    /**
     * Currency of the cart, or null if empty.
     */
    public Currency getCurrency() {
        return _currency;
    }

    /**
     * Immutable cart line: listing id + price.
     */
    public static class CartLine {
        private final String _listingId;
        private final Price _price;

        /**
         * @param listingId id of the listing
         * @param price price attached to the listing
         */
        public CartLine(String listingId, Price price) {
            _listingId = listingId;
            _price = price;
        }

        /**
         * Listing identifier normalized by the cart.
         */
        public String getListingId() {
            return _listingId;
        }

        /**
         * Price assigned when the listing was added.
         */
        public Price getPrice() {
            return _price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CartLine)) return false;
            CartLine cartLine = (CartLine) o;
            return Objects.equals(_listingId, cartLine._listingId) &&
                    Objects.equals(_price, cartLine._price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(_listingId, _price);
        }
    }
}