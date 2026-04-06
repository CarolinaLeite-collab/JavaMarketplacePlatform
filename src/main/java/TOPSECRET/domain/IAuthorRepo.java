package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;

import java.util.List;

public interface IAuthorRepo {

    Author addAuthor(String authorName);

    boolean existsByName(String name);

    List<Author> findAll();
}
