package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link PublicationType} instances.
 * <p>
 * @throws IllegalArgumentException if publicationTypeName is invalid (as defined by {@link PublicationType}'s constructor)
 * </p>
 */

public class PublicationTypeFactory {

    public PublicationType createPublicationType(String publicationTypeName) throws InstantiationException {

        try {

            return new PublicationType(publicationTypeName);

        } catch  (Exception e) {

            throw new InstantiationException ("Publication Type not successfully created!" +  e.getMessage());

        }

    }

}
