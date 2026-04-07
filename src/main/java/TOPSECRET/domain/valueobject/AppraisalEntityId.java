package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

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

        if ( name == null || name.isBlank() ) {

            throw new IllegalArgumentException("AppraisalEntityId cannot be null or blank");

        }

        String[] parts = name.trim().split("\\s+");
        String lastName = parts[parts.length - 1];

        StringBuilder initials = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {

            initials.append(Character.toUpperCase(parts[i].charAt(0)));

            initials.append(".");

        }

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        _id = lastName + initials + "-" + code;

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