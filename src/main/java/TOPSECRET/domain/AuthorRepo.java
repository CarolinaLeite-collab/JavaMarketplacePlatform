package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link Author} instances.
 * <p>
 * Provides methods to create new authors, check for the existence of authors by name,
 * and retrieve all authors in the repository.
 * </p>
 */

public class AuthorRepo {

    private List<Author> _authors;
    private AuthorFactory _authorFactory;

    public AuthorRepo(AuthorFactory authorFactory){
        _authorFactory = new AuthorFactory();
        _authors = new ArrayList<>();
    }
    // Repository creates and returns the Author
    public Author createAuthor(String authorName) {
        if (authorName == null || authorName.isBlank()) {
            throw new IllegalArgumentException("Author name is mandatory");
        }

        String normalizedName = authorName.trim();
        if (existsByName(normalizedName)){
            throw new IllegalStateException("Author already exists");
        }

        Author author = _authorFactory.createAuthor(normalizedName);

        _authors.add(author);

        return author;
    }

    public boolean existsByName(String name) {
        return _authors.stream().anyMatch(a -> a.getName().equalsIgnoreCase(name));
    }

    public List<Author> findAll() {
        return new ArrayList<>(_authors);
    }
}
