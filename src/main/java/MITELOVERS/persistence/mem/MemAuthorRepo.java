package MITELOVERS.persistence.mem;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory implementation of the {@link IAuthorRepo} repository.
 * <p>
 * This class stores {@link Author} entities in a HashMap using their identity as the key.
 * It is intended for testing, prototyping, or scenarios where persistence is not required.
 * </p>
 * <p>
 * It provides basic repository operations such as saving and retrieving.
 * </p>
 * Active only when the {@code mem} Spring profile is enabled.
 * </p>
 */

@Repository
@Profile("mem")
public class MemAuthorRepo implements IAuthorRepo {

    private Map<AuthorId, Author> DATA = new HashMap<AuthorId, Author>();

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

}
