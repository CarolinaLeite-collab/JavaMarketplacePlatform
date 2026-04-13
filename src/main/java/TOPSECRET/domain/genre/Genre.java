package TOPSECRET.domain.genre;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.GenreId;

import java.util.Objects;

/**
 * Represents a genre in the MiteLovers domain.
 * <p>
 * A {@code Genre} is an Aggregate Root that classifies publications based on
 * shared characteristics like style, form, or content.
 * </p>
 *
 * <p><b>Identity:</b> Each {@code Genre} is identified by a {@link GenreId}
 * derived from its normalised name. Two genres are considered equal if they
 * share the same name, regardless of case.</p>
 * <p><b>Validation:</b> The genre name cannot be null, empty, or whitespace-only.</p>
 *
 */

public class Genre implements AggregateRoot<GenreId> {

    private final GenreId _genreId;
    private final String _genre;

    Genre(String genre) {
        if (genre == null || genre.trim().isEmpty())
            throw new IllegalArgumentException("Genre name cannot be null or empty");
        _genre = genre.trim();
        _genreId = new GenreId(_genre);
    }

    public String getGenre() {
        return _genre;
    }

    @Override
    public GenreId identity() { return _genreId; }

    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof Genre other)) return false;
        return _genre.equalsIgnoreCase(other._genre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre other)) return false;
        return _genreId.equals(other._genreId);
    }

    @Override
    public int hashCode() {
        return _genre.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return _genre;
    }
}
