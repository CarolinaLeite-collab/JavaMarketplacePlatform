package TOPSECRET.domain;

import java.util.Objects;

/**
 * Represents the organization or company that formally releases the work (publication). Cannot be null, empty, or whitespace‑only.
 */

public class PublishingCompany {
    private final String _name;

    PublishingCompany(String name){
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Publisher name cannot be null, empty or blank");

        _name = name.trim().replaceAll("\\s+", " ");;
    }

    public String getName() {
        return _name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublishingCompany)) return false;
        PublishingCompany publisher = (PublishingCompany) o;
        return _name.equalsIgnoreCase(publisher._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name.toLowerCase());
    }
}
