package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import TOPSECRET.domain.appraisalentity.AppraisalEntity;


/**
 * Value object that represents the unique identity of an {@link AppraisalEntity}.
 * <p>
 * The identifier is automatically generated from a given name by normalizing and formatting it
 * into a structured string with an acronym prefix and a readable normalized form.
 * </p>
 * <p>
 * Format: {@code entity:ACRONYM-NormalizedName}
 * <ul>
 *   <li>Acronym is built from the first letter of each word (uppercase).</li>
 *   <li>Normalized name capitalizes each word and removes extra spaces or hyphens.</li>
 * </ul>
 * </p>
 * <p>
 * This class ensures identity consistency by overriding {@code equals}, {@code hashCode},
 * and {@code toString} based on the generated identifier.
 * </p>
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

        _id = "entity:" + acronym + "-" + normalized;

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