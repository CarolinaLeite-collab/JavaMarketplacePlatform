package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;

import java.util.Objects;
import java.util.UUID;

public class ListOfItemsId implements DomainId {

    private final String _loiId;

    public ListOfItemsId(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("ListOfItemsId cannot be null or empty");

        this._loiId = value;
    }

    public static ListOfItemsId newId() {

        String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return new ListOfItemsId("LOI-" + uuidPart);
    }

    public String getValue() {
        return _loiId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ListOfItemsId that)) return false;
        return Objects.equals(_loiId, that._loiId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_loiId);
    }

    @Override
    public String toString() {
        return _loiId;
    }

}
