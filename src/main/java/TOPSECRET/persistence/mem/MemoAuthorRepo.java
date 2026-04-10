package TOPSECRET.persistence.mem;

import TOPSECRET.domain.repository.IAuthorRepo;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.author.AuthorFactory;
import TOPSECRET.domain.valueobject.AuthorId;

import java.util.*;

/**
 * Repository for managing {@link Author} instances.
 * <p>
 * Provides methods to create new authors, check for the existence of authors by name,
 * and retrieve all authors in the repository.
 * </p>
 */

public class MemoAuthorRepo implements IAuthorRepo {

    private Map<AuthorId, Author> DATA = new HashMap<AuthorId, Author>();
    private final AuthorFactory _authorFactory;

    public MemoAuthorRepo(AuthorFactory authorFactory) {

        _authorFactory = authorFactory;

    }


    @Override
    public Author save(Author author) {

        DATA.put(author.identity(), author);

        return author;

    }

    @Override
    public Iterable<Author> findAll() {

        return DATA.values();

    }

    @Override
    public Optional<Author> ofIdentity(AuthorId id) {

        if(!containsOfIdentity(id)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(id));

        }

    }

    @Override
    public boolean containsOfIdentity(AuthorId id) {

        return DATA.containsKey(id);

    }

    @Override
    public Author addAuthor(String authorName) {

        Author newAuthor = _authorFactory.createAuthor(authorName);

        if (containsOfIdentity(newAuthor.identity())) {
            throw new IllegalArgumentException("Author already exists in the repository");
        }

        return save (newAuthor);

    }

}
