package TOPSECRET.domain;

public class PublicationType {

    private final String type;

    public PublicationType(String typeName) {
        if (typeName == null || typeName.isBlank())
            throw new IllegalArgumentException("Publication type name is required!");

        this.type = typeName;
    }

    public String getPublicationType() {
        return type;
    }
}
