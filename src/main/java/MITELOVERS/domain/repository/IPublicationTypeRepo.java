package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.PublicationTypeId;

/**
 * Repository interface for managing {@link PublicationType} aggregate roots.
 * <p>
 * Extends {@link IRepository} with {@link PublicationTypeId} as the identity type
 * and {@link PublicationType} as the aggregate root type.
 * </p>
 */

public interface IPublicationTypeRepo extends IRepository<PublicationTypeId, PublicationType> {

    PublicationType addPublicationType(String publicationTypeName);

}
