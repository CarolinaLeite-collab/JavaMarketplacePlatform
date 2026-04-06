package TOPSECRET.domain.PublicationType;

import java.util.Objects;

/**
 * Represents a classification that defines the category of a publication
 * (e.g. book, magazine) and allows the system to classify and organize publications.
 * <p>
 * A publication type is defined by a name that cannot be null, empty, or blank.
 * The name is normalized by trimming whitespace and converting it to uppercase.
 * </p>
 */

public class PublicationType {

    private final String _publicationType;

     PublicationType(String publicationTypeName) throws IllegalArgumentException {

        if (publicationTypeName == null || publicationTypeName.isBlank()) {

            throw new IllegalArgumentException("Publication type name is required!");

        }

        _publicationType = publicationTypeName.toUpperCase().trim();

    }

    public boolean isSamePublicationType (String publicationTypeName) {

        if  (publicationTypeName == null || publicationTypeName.isBlank()) {

            return false;

        }

        String publicationTypeNameNormalized = publicationTypeName.toUpperCase().trim();

        return publicationTypeNameNormalized.equals(_publicationType);

    }

    public String getPublicationType() {
        return _publicationType;
    }

    @Override
    public String toString() {
        return _publicationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationType)) return false;
        PublicationType pubType = (PublicationType) o;
        return _publicationType.equals(pubType._publicationType.toUpperCase().trim());
    }

    // Normalizing hash codes will prevent false negatives
    @Override
    public int hashCode() {
        return Objects.hash(_publicationType);
    }
}
