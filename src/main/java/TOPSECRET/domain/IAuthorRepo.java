package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public interface IAuthorRepo {

    public Author createAuthor(String authorName);

    public boolean existsByName(String name);

    public List<Author> findAll();
}
