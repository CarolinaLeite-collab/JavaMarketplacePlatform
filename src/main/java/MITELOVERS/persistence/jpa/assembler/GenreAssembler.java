package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link Genre} domain objects
 * and {@link GenreDataModel} persistence objects.
 */

@Component
@AllArgsConstructor
public class GenreAssembler {

    private final GenreFactory _genreFactory;

    public GenreDataModel toDataModel(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        return new GenreDataModel(
                genre.identity().toString(),
                genre.getGenre()
        );
    }

    public Genre toDomain(GenreDataModel dm) {
        if (dm == null) {
            throw new IllegalArgumentException("GenreDataModel cannot be null");
        }

        return _genreFactory.createGenre(new GenreId(dm.getId()), dm.getName());
    }
}

