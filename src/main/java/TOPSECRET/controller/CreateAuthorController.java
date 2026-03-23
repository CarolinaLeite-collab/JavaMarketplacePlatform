package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.IAuthorRepo;
import TOPSECRET.domain.MemoAuthorRepo;

/**
 * Controller responsible for creating new authors in the system.
 * <p>
 * This controller delegates the creation of {@link Author} instances to the
 * {@link IAuthorRepo}, ensuring that author names are valid and not empty.
 * </p>
 */

public class CreateAuthorController {

    private IAuthorRepo _authorRepo;

    public CreateAuthorController(IAuthorRepo ar) {
        _authorRepo = ar;
    }

    public Author createAuthor (String authorName){

        return _authorRepo.createAuthor(authorName.trim());

    }


}
