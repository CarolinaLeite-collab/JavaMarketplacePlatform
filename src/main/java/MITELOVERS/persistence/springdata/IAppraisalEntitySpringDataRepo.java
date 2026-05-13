package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.AppraisalEntityDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppraisalEntitySpringDataRepo extends JpaRepository<AppraisalEntityDataModel, String> {

}
