package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import TOPSECRET.domain.author.Author;

import java.util.UUID;

/**
 * Represents the unique identifier of an {@link Author}.
 * <p>
 * The identifier is automatically generated from the entity's full name and
 * a random code. The format is:
 * <pre>
 * LastName + InitialsOfOtherNames + "-" + Random6CharCode
 * </p>
 *
 * <p><b>Validation:</b> The full name cannot be null or blank.</p>
 *
 * <p><b>Equality:</b> Two {@code Author} instances are equal if they
 * wrap the same underlying string value.</p>
 */

public class AuthorId implements DomainId {

    private final String _id;

    public AuthorId(String name) {

        if ( name == null || name.isBlank() ) {

            throw new IllegalArgumentException("AuthorId cannot be null or blank");

        }

        String[] parts = name.trim().split("\\s+");
        String lastName = parts[parts.length - 1];

        StringBuilder initials = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {

            initials.append(Character.toUpperCase(parts[i].charAt(0)));

            initials.append(".");

        }

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        _id = lastName + " " + initials + "-" + code;

    }

    @Override
    public boolean equals(Object o) {

        if ( this == o ) return true;

        if (!(o instanceof AuthorId other)) return false;

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