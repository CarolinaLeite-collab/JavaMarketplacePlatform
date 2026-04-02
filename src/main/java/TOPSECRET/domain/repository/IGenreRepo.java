package TOPSECRET.domain;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.GenreId;

/**
 * Repository interface for managing {@link Genre} aggregate roots.
 * <p>
 * Extends {@link IRepository} with {@link GenreId} as the identity type
 * and {@link Genre} as the aggregate root type.
 * </p>
 */

public interface IGenreRepo extends IRepository<GenreId, Genre> {

    Genre addGenre(String name);
}
