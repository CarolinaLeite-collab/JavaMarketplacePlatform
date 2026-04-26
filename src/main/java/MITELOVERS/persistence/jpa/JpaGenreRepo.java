package MITELOVERS.persistence.jpa;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.assembler.GenreAssembler;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import MITELOVERS.persistence.jpa.springdata.IGenreSpringDataRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA implementation of {@link IGenreRepo}.
 * <p>
 * Active when the {@code mem} Spring profile is not enabled.
 * </p>
 */

@Repository
@Profile("!mem")
@AllArgsConstructor
public class JpaGenreRepo implements IGenreRepo {

    private final IGenreSpringDataRepo _iGenreSpringDataRepo;
    private final GenreAssembler _genreAssembler;

    @Override
    public Genre save(Genre genre) {
        GenreDataModel saved = _iGenreSpringDataRepo.save(_genreAssembler.domain2DM(genre));
        return _genreAssembler.DM2Domain(saved);
    }

    @Override
    public Iterable<GenreId> findAllKeys() {
        return _iGenreSpringDataRepo.findAll()
                .stream()
                .map(dm -> new GenreId(dm.getId()))
                .toList();
    }

    @Override
    public Iterable<Genre> findAll() {
        return _iGenreSpringDataRepo.findAll()
                .stream()
                .map(_genreAssembler::DM2Domain)
                .toList();
    }

    @Override
    public Optional<Genre> ofIdentity(GenreId id) {
        return _iGenreSpringDataRepo.findById(id.toString())
                .map(_genreAssembler::DM2Domain);
    }

    @Override
    public boolean containsOfIdentity(GenreId id) {
        return _iGenreSpringDataRepo.existsById(id.toString());
    }
}
