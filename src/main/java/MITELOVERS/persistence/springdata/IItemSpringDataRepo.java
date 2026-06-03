package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface IItemSpringDataRepo extends JpaRepository<ItemDataModel, String> {

    List<ItemDataModel> findByIdInOrderByDescriptionAsc(Collection<String> ids);

    @Query(value = """
        SELECT i.id
        FROM items i
        JOIN editions e ON e.id = i.edition_id
        JOIN publications p ON p.id = e.publication_id
        WHERE p.genre_id = :genreId
        """, nativeQuery = true)
    List<String> findItemIdsByGenre(@Param("genreId") String genreId);

}