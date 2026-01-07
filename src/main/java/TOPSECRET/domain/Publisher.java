package TOPSECRET.domain;

import java.util.Objects;

/**
 * The entity responsible for publication. Cannot be null, empty, or whitespace‑only.
 */

public class Publisher {
    private final String _name;

    public Publisher(String name){
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Publisher name cannot be null, empty or blank");

        _name = name.trim();
    }

    public String getName() {
        return this._name;
    }

    // Avoid publisher duplication
    /** This code doesn't prevent someone from writing it differently. It prevents the system from treating them as different entities.*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher)) return false;
        Publisher publisher = (Publisher) o;
        return _name.equalsIgnoreCase(publisher._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name.toLowerCase());
    }
}
