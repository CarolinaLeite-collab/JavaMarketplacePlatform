package TOPSECRET.domain;

import java.util.Objects;

/**
 * Represents the description of the morphology of the {@link Publication}.
 * <p>
 * Ensures that the type name is not null or blank, and provides a getter for the type name.
 * </p>
 */

public class PublicationType {

    private final String _publicationType;

    public PublicationType(String publicationTypeName) throws IllegalArgumentException {

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
