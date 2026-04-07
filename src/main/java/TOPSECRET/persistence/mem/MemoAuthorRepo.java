package TOPSECRET.persistence.mem;

import TOPSECRET.domain.repository.IAuthorRepo;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.author.AuthorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link Author} instances.
 * <p>
 * Provides methods to create new authors, check for the existence of authors by name,
 * and retrieve all authors in the repository.
 * </p>
 */

public class MemoAuthorRepo implements IAuthorRepo {

    private List<Author> _authors;
    private AuthorFactory _authorFactory;

    public MemoAuthorRepo(AuthorFactory authorFactory) {
        _authorFactory = new AuthorFactory();
        _authors = new ArrayList<>();
    }

    @Override
    public Author addAuthor(String authorName) {

        String normalizedName = authorName.trim();
        if (existsByName(normalizedName)) {
            throw new IllegalStateException("Author already exists");
        }

        Author author = _authorFactory.createAuthor(normalizedName);

        _authors.add(author);

        return author;
    }

    @Override
    public boolean existsByName(String name) {
        return _authors.stream().anyMatch(a -> a.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Author> findAll() {
        return new ArrayList<>(_authors);
    }
}
