package TOPSECRET.domain.repository;

import TOPSECRET.domain.author.Author;

import java.util.List;

public interface IAuthorRepo {

    Author addAuthor(String authorName);

    boolean existsByName(String name);

    List<Author> findAll();
}
