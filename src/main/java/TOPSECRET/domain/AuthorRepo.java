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

    private List<Author> authors = new ArrayList<>();

    // Repository creates and returns the Author
    public Author create(String authorName) {
        if (authorName == null || authorName.isBlank()) {
            throw new IllegalArgumentException("Author name is mandatory");
        }

        String normalizedName = authorName.trim();
        if (existsByName(normalizedName)){
            throw new IllegalStateException("Author already exists");
        }

            Author author = new Author(normalizedName);
            authors.add(author);
            return author;
    }

    public boolean existsByName(String name) {
        return authors.stream().anyMatch(a -> a.getName().equalsIgnoreCase(name));
    }

    public List<Author> findAll() {
        return new ArrayList<>(authors);
    }
}
