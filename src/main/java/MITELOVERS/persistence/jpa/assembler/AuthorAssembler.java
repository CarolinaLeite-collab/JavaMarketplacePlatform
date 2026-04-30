package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.persistence.jpa.datamodel.AuthorDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link Author} domain objects
 * and {@link AuthorDataModel} persistence objects.
 */

@Component
@AllArgsConstructor

public class AuthorAssembler {

    private final AuthorFactory _authorFactory;

    public AuthorDataModel toDataModel(Author author) {
        if (author == null)
            throw new IllegalArgumentException("Author cannot be null");

        return new AuthorDataModel(author.identity().toString(), author.getName().toString());
    }

    public Author toDomain(AuthorDataModel dataModel) {
        if (dataModel == null)
            throw new IllegalArgumentException("AuthorDataModel cannot be null");

        AuthorId authorId = new AuthorId(dataModel.getId());
        Name name = new Name(dataModel.getName());

        Author author = _authorFactory.createAuthor(authorId, name);

        return author;
    }
}
