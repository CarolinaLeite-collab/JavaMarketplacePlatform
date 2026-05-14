package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.PublishingCompanyDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository responsible for managing
 * {@link PublishingCompanyDataModel} persistence operations.
 *
 * Provides CRUD operations and database access for
 * publishing company data models.
 */

public interface IPublishingCompanySpringDataRepo extends JpaRepository<PublishingCompanyDataModel, String> {
}
