package MITELOVERS.controller;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;

/**
 * Controller responsible for creating new authors in the system.
 * <p>
 * This controller delegates the creation of {@link Author} instances to the
 * {@link IAuthorRepo}, ensuring that author names are valid and not empty.
 * </p>
 */

public class CreateAuthorController {

    private IAuthorRepo _iAuthorRepo;
    private AuthorFactory _authorFactory;

    public CreateAuthorController(IAuthorRepo iAuthorRepo,AuthorFactory authorFactory, UserId adminId) {

        _iAuthorRepo = iAuthorRepo;
        _authorFactory = authorFactory;

    }

    public Author createAuthor (Name authorName){

        Author newAuthor = _authorFactory.createAuthor(authorName);

        return _iAuthorRepo.save (newAuthor);

    }

}
