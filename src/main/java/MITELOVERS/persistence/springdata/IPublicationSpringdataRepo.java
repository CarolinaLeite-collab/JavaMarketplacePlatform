package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.PublicationDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPublicationSpringdataRepo extends JpaRepository<PublicationDataModel, String> {
}
