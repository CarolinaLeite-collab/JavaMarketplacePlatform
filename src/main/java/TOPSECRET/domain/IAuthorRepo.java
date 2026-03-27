package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;

import java.util.List;

public interface IAuthorRepo {

    Author createAuthor(String authorName);

    boolean existsByName(String name);

    List<Author> findAll();
}
