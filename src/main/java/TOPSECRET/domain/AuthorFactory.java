package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link Author} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

public class AuthorFactory {
    public Author createAuthor(String authorName){
        return new Author(authorName);
    }
}
