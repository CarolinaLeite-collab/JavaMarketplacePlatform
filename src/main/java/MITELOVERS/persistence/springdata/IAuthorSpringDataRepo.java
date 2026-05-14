package MITELOVERS.persistence.springdata;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAuthorSpringDataRepo extends JpaRepository<AuthorDataModel, String> {
    List<AuthorDataModel> findByName(String name);
}