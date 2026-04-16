package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.valueobject.PublicationTypeId;

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
