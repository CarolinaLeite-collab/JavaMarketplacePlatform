package TOPSECRET.domain;

/**
 * Represents the description of the morphology of the {@link Publication}.
 * <p>
 * Ensures that the type name is not null or blank, and provides a getter for the type name.
 * </p>
 */

public class PublicationType {

    private final String typeName;

    public PublicationType(String typeName) {
        if (typeName == null || typeName.isBlank())
            throw new IllegalArgumentException("Publication type name is required!");

        this.typeName = typeName;
    }
    public String getPublicationType() {
        return typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
