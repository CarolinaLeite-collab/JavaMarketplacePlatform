package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.valueobject.AuthorId;

public interface IAuthorRepo extends IRepository<AuthorId,Author> {

    public AuthorId findByName(final String name);

}
