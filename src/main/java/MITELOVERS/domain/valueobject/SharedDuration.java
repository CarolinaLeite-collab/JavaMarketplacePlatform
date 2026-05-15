package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

import java.util.Objects;

/**
 * Value Object representing the duration for which a {@link MITELOVERS.domain.listofitems.ListOfItems}
 * will be publicly shared.
 */
public class SharedDuration implements ValueObject {

    private final int _days;


    public SharedDuration(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Duration must be a positive number of days");
        }
        _days = days;
    }

    public int getDays() {
        return _days;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SharedDuration other)) return false;
        return _days == other._days;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_days);
    }

    @Override
    public String toString() {
        return String.valueOf(_days);
    }
}