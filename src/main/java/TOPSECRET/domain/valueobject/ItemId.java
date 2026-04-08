package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

import java.util.Objects;

public final class ItemId implements DomainId {

    private final SKU _sku;

    public ItemId(SKU sku) {

        _sku = Objects.requireNonNull(sku,"SKU is required.");
    }

    public static ItemId generate() {
        return new ItemId(SKU.generate());
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
