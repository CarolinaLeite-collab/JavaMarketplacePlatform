package TOPSECRET.domain.publicationtype;

import TOPSECRET.domain.valueobject.PublicationTypeId;

/**
 * Factory responsible for creating {@link PublicationType} instances.
 * <p>
 * IllegalArgumentException is thrown if publicationTypeName is invalid (as defined by {@link PublicationTypeId}'s constructor)
 * </p>
 */

public class PublicationTypeFactory {

    public PublicationType createPublicationType(String publicationTypeName) {

            return new PublicationType(publicationTypeName);

    }

}
