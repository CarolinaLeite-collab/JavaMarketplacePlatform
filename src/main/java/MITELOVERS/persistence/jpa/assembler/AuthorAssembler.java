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

        String stringId = author.identity().toString();
        String stringName = author.getName().toString();

        AuthorDataModel dataModelToSave = new AuthorDataModel(stringId, stringName);

        return dataModelToSave;
    }

    public Author toDomain(AuthorDataModel dataModel) {

        AuthorId authorId = new AuthorId(dataModel.getId());
        Name name = new Name(dataModel.getName());

        Author reconstructoredAuthor = _authorFactory.createAuthor(authorId, name);

        return reconstructoredAuthor;
    }
}
