package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.persistence.jpa.assembler.LibraryAssembler;
import MITELOVERS.persistence.jpa.datamodel.LibraryDataModel;
import MITELOVERS.persistence.springdata.ILibrarySpringDataRepo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaLibraryRepo {

    private final ILibrarySpringDataRepo _iSpringDataRepo;
    private final LibraryAssembler _libraryAssembler;

    public JpaLibraryRepo(ILibrarySpringDataRepo springRepo, LibraryAssembler libraryAssembler) {

        _iSpringDataRepo = springRepo;
        _libraryAssembler = libraryAssembler;
    }

    @Override
    public Library save(Library entity) {

        // convert to dm
        LibraryDataModel dm = _libraryAssembler.toDataModel(entity);

        // persist
        LibraryDataModel saved = _iSpringDataRepo.save(dm);

        // convert back to domain
        return _libraryAssembler.toDomain(saved);
    }

    @Override
    public Iterable<LibraryId> findAllKeys() {

        return _iSpringDataRepo.findAll().stream()
                .map(dm -> new LibraryId(new Email(dm.getLibraryId())))
                .toList();
    }

    @Override
    public Iterable<Library> findAll() {

        List<LibraryDataModel> dms = _iSpringDataRepo.findAll();

        return _libraryAssembler.listToDomain(dms);

    }

    @Override
    public Optional<Library> ofIdentity(LibraryId id) {

        return _iSpringDataRepo.findById(id.toString())
                .map(_libraryAssembler::toDomain);
    }

    @Override
    public boolean containsOfIdentity(LibraryId id) {

        return _iSpringDataRepo.existsById(id.toString());
    }

}
