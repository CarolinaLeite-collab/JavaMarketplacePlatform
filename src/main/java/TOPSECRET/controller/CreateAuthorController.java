package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.AuthorRepo;

public class CreateAuthorController {

    private final AuthorRepo authorRepo;

    public CreateAuthorController(AuthorRepo ar) {
        authorRepo = ar;
    }

    // Delegates creation to the repository
    public Author createAuthor (String authorName){
        if (authorName == null || authorName.isBlank()){
            throw new IllegalArgumentException("Author name is mandatory");
        }
        return authorRepo.create(authorName.trim());
    }


}
