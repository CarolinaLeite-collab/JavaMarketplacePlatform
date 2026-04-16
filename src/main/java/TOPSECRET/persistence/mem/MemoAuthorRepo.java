package TOPSECRET.persistence.mem;

import TOPSECRET.domain.repository.IAuthorRepo;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.author.AuthorFactory;
import TOPSECRET.domain.valueobject.AuthorId;
import java.util.*;

/**
 * In-memory implementation of the {@link IAuthorRepo} repository.
 * <p>
 * This class stores {@link Author} entities in a HashMap using their identity as the key.
 * It is intended for testing, prototyping, or scenarios where persistence is not required.
 * </p>
 * <p>
 * It provides basic repository operations such as saving, retrieving, checking existence,
 * and creating new {@link Author} instances through an injected {@link AuthorFactory}.
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
    public List<AuthorId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

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

        return save (newAuthor);

    }

}
