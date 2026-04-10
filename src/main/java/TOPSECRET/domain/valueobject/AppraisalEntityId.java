package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

import java.util.Arrays;
import java.util.UUID;

/**
 * Represents the unique identifier of an {@link AppraisalEntityId}.
 * <p>
 * The identifier is automatically generated from the entity's full name and
 * a random code. The format is:
 * <pre>
 * LastName + InitialsOfOtherNames + "-" + Random6CharCode
 * </pre>
 * Example: "Marcelo Pedro Rocha Junior" → "JuniorM.P.R-4F7A1B"
 * </p>
 *
 * <p><b>Validation:</b> The full name cannot be null or blank.</p>
 *
 * <p><b>Equality:</b> Two {@code AppraisalEntityId} instances are equal if they
 * wrap the same underlying string value.</p>
 */

public class AppraisalEntityId implements DomainId {

    private final String _id;

    public AppraisalEntityId(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("AppraisalEntityId cannot be null or blank");
        }

        String trimmed = name.trim();
        String[] words = trimmed.split("[\\s-]+");

        String acronym = "";
        String normalized = "";

        for (String word : words) {

            acronym += Character.toUpperCase(word.charAt(0));

            normalized += Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
        }

        this._id = "entity:" + acronym + "-" + normalized;

    }

    @Override
    public boolean equals(Object o) {

        if ( this == o ) return true;

        if (!(o instanceof AppraisalEntityId other)) return false;

        return _id.equals(other._id);

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