package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDirectSaleSpringDataRepo extends JpaRepository<DirectSaleDataModel, String> {

    List<DirectSaleDataModel> findByItemsIdOrderByCreationDateAsc(List<String> itemsId);

    List<DirectSaleDataModel> findByItemsIdOrderByCreationDateDesc(List<String> itemsId);

    @Query( value = """
    SELECT ds
    FROM DirectSaleDataModel ds
    WHERE ds.timeLimit IS NOT NULL
      AND DATEADD('SECOND', ds.timeLimit, ds.creationDate) < CURRENT_TIMESTAMP
    """,
            nativeQuery = true
    )
    List<DirectSaleDataModel> findExpiredRaw();

}
