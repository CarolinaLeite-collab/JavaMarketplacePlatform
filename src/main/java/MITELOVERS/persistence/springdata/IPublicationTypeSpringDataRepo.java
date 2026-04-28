package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.PublicationTypeDataModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPublicationTypeSpringDataRepo extends CrudRepository<PublicationTypeDataModel, String> {

}
