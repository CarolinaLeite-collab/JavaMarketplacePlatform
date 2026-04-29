package MITELOVERS.persistence.jpa.springdata;

import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link GenreDataModel}.
 */

public interface IGenreSpringDataRepo extends JpaRepository<GenreDataModel, String> {
}


