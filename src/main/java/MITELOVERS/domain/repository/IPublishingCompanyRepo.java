package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.valueobject.PublishingCompanyId;

/**
 * Repository interface for managing {@link PublishingCompany} aggregate roots.
 * <p>
 * Extends {@link IRepository} with {@link PublishingCompanyId} as the identity type
 * and {@link PublishingCompany} as the aggregate root type.
 * </p>
 */

public interface IPublishingCompanyRepo extends IRepository<PublishingCompanyId, PublishingCompany>{

    PublishingCompany registerPublishingCompany(String publishingCompanyName);

}
