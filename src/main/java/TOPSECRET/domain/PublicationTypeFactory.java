package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link PublicationType} instances.
 * <p>
 * @throws IllegalArgumentException if publicationTypeName is invalid (as defined by {@link PublicationType}'s constructor)
 * </p>
 */

public class PublicationTypeFactory {

    public PublicationType createPublicationType(String publicationTypeName) {

            return new PublicationType(publicationTypeName);

    }

}
