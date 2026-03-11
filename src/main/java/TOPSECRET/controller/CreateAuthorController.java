package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorRepo;

/**
 * Controller responsible for creating new authors in the system.
 * <p>
 * This controller delegates the creation of {@link Author} instances to the
 * {@link AuthorRepo}, ensuring that author names are valid and not empty.
 * </p>
 */

public class CreateAuthorController {

    private AuthorRepo _authorRepo;

    public CreateAuthorController(AuthorRepo ar) {
        _authorRepo = ar;
    }

    public Author createAuthor (String authorName){
        if (authorName == null || authorName.isBlank()){
            throw new IllegalArgumentException("Author name is mandatory");
        }
        return _authorRepo.createAuthor(authorName.trim());
    }


}
