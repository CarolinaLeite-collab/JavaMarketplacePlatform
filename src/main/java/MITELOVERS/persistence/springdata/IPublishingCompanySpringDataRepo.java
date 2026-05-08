package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.PublishingCompanyDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPublishingCompanySpringDataRepo extends JpaRepository<PublishingCompanyDataModel, String> {
}
