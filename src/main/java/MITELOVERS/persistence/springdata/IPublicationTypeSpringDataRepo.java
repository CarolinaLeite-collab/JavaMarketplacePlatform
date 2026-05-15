package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link PublicationTypeDataModel}.
 * <p>
 */

public interface IPublicationTypeSpringDataRepo extends JpaRepository<PublicationTypeDataModel, String> {

}
