package MITELOVERS.domain.author;

import MITELOVERS.domain.valueobject.AuthorId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link Author} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

@Component
public class AuthorFactory {

    public Author createAuthor(String authorName){

        return new Author(authorName);

    }

    public Author createAuthor(AuthorId authorId, String authorName){

        return new Author(authorId, authorName);

    }

}
