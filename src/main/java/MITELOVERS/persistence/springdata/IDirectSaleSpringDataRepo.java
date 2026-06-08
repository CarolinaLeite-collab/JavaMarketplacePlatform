package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDirectSaleSpringDataRepo extends JpaRepository<DirectSaleDataModel, String> {

    List<DirectSaleDataModel> findByItemsIdOrderByCreationDateAsc(List<String> itemsId);

    List<DirectSaleDataModel> findByItemsIdOrderByCreationDateDesc(List<String> itemsId);

    @Query("""
    SELECT ds
    FROM DirectSaleDataModel ds
    WHERE ds.timeLimit IS NOT NULL
      AND FUNCTION('DATEADD', 'SECOND', ds.timeLimit, ds.creationDate) < CURRENT_TIMESTAMP
""")
    List<DirectSaleDataModel> findExpiredRaw();

}
