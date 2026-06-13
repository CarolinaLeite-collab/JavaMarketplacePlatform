package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;
import MITELOVERS.domain.shoppingcart.ShoppingCart;

import java.util.UUID;

/**
 * Value object representing the unique identifier of a {@link ShoppingCart}.
 * <p>
 * Identifiers follow the format {@code SC-XXXXXXXX}, where {@code X} is an
 * uppercase letter or digit.
 * </p>
 */

public class ShoppingCartId implements DomainId {

    private String _id;

    public ShoppingCartId() {

        _id = "SC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    }

    public ShoppingCartId(String id) {

        if (id == null || !id.matches("SC-[A-Z0-9]{8}")) {
            throw new IllegalArgumentException("Invalid ShoppingCartId format!");
        }

        _id = id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartId that = (ShoppingCartId) o;
        return _id.equals(that._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    @Override
    public String toString() {
        return _id;
    }
}
