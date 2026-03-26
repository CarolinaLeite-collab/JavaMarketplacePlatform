package TOPSECRET.domain;

/** A genre is a category used to classify publications based
 * on shared characteristics like style, form, or content.
 * Cannot be null, empty, or whitespace‑only.
 */

public class Genre {

    private final String _genre;

    Genre(String genre) {
        if (genre == null || genre.trim().isEmpty())
            throw new IllegalArgumentException("Genre name cannot be null or empty");

        _genre = genre.trim();
    }

    public String getGenre() {
        return _genre;
    }

    // Avoid genre duplication
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return _genre.equalsIgnoreCase(genre._genre);
    }

    @Override
    public int hashCode() {
        return _genre.toUpperCase().hashCode();
    }

    // Converts the Genre object into a readable String
    @Override
    public String toString() {
        return _genre;
    }
}
