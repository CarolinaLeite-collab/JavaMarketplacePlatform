package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.valueobject.GenreId;

/**
 * Repository interface for managing {@link Genre} aggregate roots.
 * <p>
 * Extends {@link IRepository} with {@link GenreId} as the identity type
 * and {@link Genre} as the aggregate root type.
 * </p>
 */

public interface IGenreRepo extends IRepository<GenreId, Genre> {

}
