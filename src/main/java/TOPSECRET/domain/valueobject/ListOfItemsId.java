package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

import java.util.Objects;
import java.util.UUID;

public class ListOfItemsId implements DomainId {

    private final UUID _value;

    public ListOfItemsId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ListOfItemsId cannot be null");
        }
        _value = value;
    }

    public static ListOfItemsId newId() {
        return new ListOfItemsId(UUID.randomUUID());
    }

    public UUID getValue() {
        return _value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListOfItemsId other)) return false;
        return Objects.equals(_value, other._value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_value);
    }

    @Override
    public String toString() {
        return _value.toString();
    }
}
