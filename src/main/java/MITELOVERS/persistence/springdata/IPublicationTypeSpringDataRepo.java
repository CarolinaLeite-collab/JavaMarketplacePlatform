package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository for {@link PublicationTypeDataModel}.
 * <p>
 */

public interface IPublicationTypeSpringDataRepo extends CrudRepository<PublicationTypeDataModel, String> {

}
