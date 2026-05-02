package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;

/**
 * Represents the unique identifier of an {@link MITELOVERS.domain.item.Item}.
 *
 * <p>
 * An {@code ItemId} is a value object that encapsulates a {@link SKU},
 * ensuring that each Item in the system has a unique and immutable identity.
 * </p>
 *
 * <p>
 * The identity is generated upon creation and is based on a unique SKU value.
 * Two {@code ItemId} instances are considered equal if their underlying
 * {@link SKU} values are equal.
 * </p>
 *
 * <p>
 * This class follows value object semantics:
 * <ul>
 *   <li>it is immutable</li>
 *   <li>equality is based on its internal state ({@link SKU})</li>
 *   <li>it provides consistent {@code equals} and {@code hashCode} implementations</li>
 * </ul>
 * </p>
 */
public final class ItemId implements DomainId {

    private final SKU _sku;

    public ItemId() {
        _sku = new SKU();
    }

    // Constructor used when reconstructing from persistence
    public ItemId(String skuValue) {
        _sku = new SKU(skuValue);
    }

    public SKU getSku() { return _sku; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemId other)) return false;
        return _sku.equals(other._sku);
    }

    @Override
    public String toString() {
        return _sku.toString();
    }

    @Override
    public int hashCode() {
        return _sku.hashCode();
    }
}
