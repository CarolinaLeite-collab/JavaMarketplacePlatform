package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.valueobject.PublishingCompanyId;

import java.util.List;

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
