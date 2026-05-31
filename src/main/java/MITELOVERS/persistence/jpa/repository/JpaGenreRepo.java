package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.assembler.GenreAssembler;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import MITELOVERS.persistence.springdata.IGenreSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaGenreRepo implements IGenreRepo {

    @Autowired
    private IGenreSpringDataRepo _genreSpringDataRepo;

    @Autowired
    private GenreAssembler _genreAssembler;

    @Override
    public Genre save(Genre genre) {
        GenreDataModel dmToSave = _genreAssembler.toDataModel(genre);
        GenreDataModel savedDm = _genreSpringDataRepo.save(dmToSave);
        return _genreAssembler.toDomain(savedDm);
    }

    @Override
    public Iterable<GenreId> findAllKeys() {
        Iterable<GenreDataModel> genreDms = _genreSpringDataRepo.findAll();
        List<GenreId> genreIds = new ArrayList<>();

        for (GenreDataModel genreDm : genreDms) {
            genreIds.add(new GenreId(genreDm.getId()));
        }

        return genreIds;
    }

    @Override
    public Iterable<Genre> findAll() {
        Iterable<GenreDataModel> genreDms = _genreSpringDataRepo.findAll();
        List<Genre> genres = new ArrayList<>();

        for (GenreDataModel genreDm : genreDms) {
            genres.add(_genreAssembler.toDomain(genreDm));
        }

        return genres;
    }

    @Override
    public Optional<Genre> ofIdentity(GenreId id) {
        return _genreSpringDataRepo.findById(id.toString())
                .map(_genreAssembler::toDomain);
    }

    @Override
    public boolean containsOfIdentity(GenreId id) {
        return _genreSpringDataRepo.existsById(id.toString());
    }
}