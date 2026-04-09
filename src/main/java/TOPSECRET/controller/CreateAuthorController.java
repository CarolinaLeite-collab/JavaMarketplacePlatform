package TOPSECRET.controller;

import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.repository.IAuthorRepo;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for creating new authors in the system.
 * <p>
 * This controller delegates the creation of {@link Author} instances to the
 * {@link IAuthorRepo}, ensuring that author names are valid and not empty.
 * </p>
 */

public class CreateAuthorController {

    private IAuthorRepo _iAuthorRepo;

    public CreateAuthorController(IAuthorRepo ar, UserId adminId) {

        _iAuthorRepo = ar;

    }

    public Author createAuthor (String authorName){

        return _iAuthorRepo.addAuthor(authorName.trim());

    }

}
