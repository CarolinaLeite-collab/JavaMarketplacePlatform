package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;

import java.util.UUID;

public class ShoppingCartLineId implements DomainId {

    private String _id;

    public ShoppingCartLineId() {

        _id = "SCL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    }

    public ShoppingCartLineId(String id) {

        if (id == null || !id.matches("SCL-[A-Z0-9]{8}")) {
            throw new IllegalArgumentException("Invalid ShoppingCartLineId format!");
        }

        _id = id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartLineId that = (ShoppingCartLineId) o;
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
