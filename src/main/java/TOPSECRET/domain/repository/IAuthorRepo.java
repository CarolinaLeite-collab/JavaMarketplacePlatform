package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.valueobject.AuthorId;

public interface IAuthorRepo extends IRepository<AuthorId,Author> {

    Author addAuthor(String authorName);

}
