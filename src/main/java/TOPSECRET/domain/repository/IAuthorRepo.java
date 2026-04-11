package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.valueobject.AuthorId;

import java.util.List;

public interface IAuthorRepo extends IRepository<AuthorId,Author> {

    Author addAuthor(String authorName);

}
