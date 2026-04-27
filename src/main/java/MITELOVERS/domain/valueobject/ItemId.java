package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;

/**
 * Represents the unique identifier of an {@link MITELOVERS.domain.item.Item}.
 */
public final class ItemId implements DomainId {

    private final SKU _sku;

    public ItemId() {
        _sku = new SKU();
    }

    // Used by the assembler — reconstructs from an existing SKU string
    public ItemId(String sku) {
        _sku = new SKU(sku);
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