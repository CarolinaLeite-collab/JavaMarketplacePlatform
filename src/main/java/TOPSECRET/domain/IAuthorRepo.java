package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;

import java.util.List;

public interface IAuthorRepo {

    public Author createAuthor(String authorName);

    public boolean existsByName(String name);

    public List<Author> findAll();
}
