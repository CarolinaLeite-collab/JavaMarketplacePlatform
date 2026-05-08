package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.ILibraryRepo;
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

public class JpaLibraryRepo implements ILibraryRepo {

    private final ILibrarySpringDataRepo _iSpringRepo;
    private final LibraryAssembler _libraryAssembler;

    public JpaLibraryRepo(ILibrarySpringDataRepo springRepo, LibraryAssembler libraryAssembler) {

        _iSpringRepo = springRepo;
        _libraryAssembler = libraryAssembler;
    }

    @Override
    public Library save(Library entity) {

        // convert to dm
        LibraryDataModel dm = _libraryAssembler.toDataModel(entity);

        // persist
        LibraryDataModel saved = _iSpringRepo.save(dm);

        // convert back to domain
        return _libraryAssembler.toDomain(saved);
    }

    @Override
    public Iterable<LibraryId> findAllKeys() {

        return _iSpringRepo.findAll().stream()
                .map(dm -> new LibraryId(new Email(dm.getLibraryId())))
                .toList();
    }

    @Override
    public Iterable<Library> findAll() {

        List<LibraryDataModel> dms = _iSpringRepo.findAll();

        return _libraryAssembler.listToDomain(dms);

    }

    @Override
    public Optional<Library> ofIdentity(LibraryId id) {

        return _iSpringRepo.findById(id.toString())
                .map(_libraryAssembler::toDomain);
    }

    @Override
    public boolean containsOfIdentity(LibraryId id) {

        return _iSpringRepo.existsById(id.toString());
    }

}
