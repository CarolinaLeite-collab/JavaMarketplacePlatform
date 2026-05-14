package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.persistence.springdata.IAuthorSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaAuthorRepo implements IAuthorRepo {

    @Autowired
    private IAuthorSpringDataRepo _springDataRepo;

    @Autowired
    private AuthorAssembler _assembler;


    @Override
    public Author save(Author author) {
        AuthorDataModel authorDataModel = _assembler.toDataModel(author);
        AuthorDataModel savedAuthorDataModel = _springDataRepo.save(authorDataModel);
        Author savedAuthor = _assembler.toDomain(savedAuthorDataModel);

        return savedAuthor;
    }

    @Override
    public Iterable<AuthorId> findAllKeys() {

        Iterable<AuthorDataModel> dms = _springDataRepo.findAll();

        List<AuthorId> authorIds = new ArrayList<>();

        for (AuthorDataModel authorDataModel : dms) {
            authorIds.add(new AuthorId(authorDataModel.getId()));
        }

        return authorIds;
    }

    @Override
    public Iterable<Author> findAll() {

        Iterable<AuthorDataModel> dms = _springDataRepo.findAll();

        List<Author> result = new ArrayList<>();

        for (AuthorDataModel authorDataModel : dms) {
            result.add(_assembler.toDomain(authorDataModel));
        }

        return result;
    }

    @Override
    public Optional<Author> ofIdentity(AuthorId id) {

        AuthorDataModel savedDataModel = _springDataRepo.findById(id.toString())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Author savedAuthor = _assembler.toDomain(savedDataModel);

        return Optional.of(savedAuthor);
    }

    @Override
    public boolean containsOfIdentity(AuthorId id) {

        return _springDataRepo.existsById(id.toString());
    }

    @Override
    public AuthorId findByName(String name) {
        AuthorDataModel authorDataModel = _springDataRepo.findByName(name).getFirst();
        return _assembler.toDomain(authorDataModel).identity();
    }
}