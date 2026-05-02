package MITELOVERS.domain.author;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link Author} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

@Component
public class AuthorFactory {

    public Author createAuthor(Name authorName){

        return new Author(authorName);

    }

    public Author createAuthor(AuthorId authorId, Name authorName){

        return new Author(authorId, authorName);

    }

}
