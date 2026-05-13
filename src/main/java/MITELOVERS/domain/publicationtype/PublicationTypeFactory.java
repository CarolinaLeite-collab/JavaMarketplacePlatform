package MITELOVERS.domain.publicationtype;

import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link PublicationType} instances.
 * <p>
 * Provides two creation strategies: one from a name string (used during creation)
 * and one from an existing {@link PublicationTypeId} (used during reconstruction from persistence).
 * </p>
 */

@Component
public class PublicationTypeFactory {

    public PublicationType createPublicationType(String publicationTypeName) {

            return new PublicationType(publicationTypeName);

    }

    public PublicationType createPublicationType(PublicationTypeId id) {

        return new PublicationType(id);

    }

}
