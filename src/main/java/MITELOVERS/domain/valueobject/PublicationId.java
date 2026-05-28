package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;
import MITELOVERS.domain.publication.Publication;

import java.time.Year;
import java.util.Objects;

/**
 * Represents the unique technical identifier of a {@link Publication}.
 * <p>
 * A {@code PublicationId} wraps a single {@link String} value that uniquely identifies
 * a publication. It can be created either from its components ({@link Title},
 * {@link AuthorId}, and release {@link Year}), or directly from a pre-formatted string.
 * </p>
 * When created from components, the string is formatted as: title - authorId (releaseYear)
 * For example: {@code "Clean-Code-MartinR.U.-ABC123(2008)"}
 * </p>
 * <p><b>Equality:</b> Two {@code PublicationId} instances are equal if they
 * wrap the same underlying string value.</p>
 */


public class PublicationId implements DomainId {

    private final String _publicationId;

    public PublicationId(Title title, AuthorId authorId, Year releaseYear) {

        title = Objects.requireNonNull(title);
        authorId = Objects.requireNonNull(authorId);
        releaseYear = Objects.requireNonNull(releaseYear);

        String cleanTitle = title.toString().trim().replaceAll("[^a-zA-Z0-9]", "-");

        _publicationId = cleanTitle + "-" + authorId + "(" + releaseYear + ")";
    }

    public PublicationId(String publicationId) {

        _publicationId = publicationId;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationId other)) return false;
        return Objects.equals(_publicationId, other._publicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_publicationId);
    }

    @Override
    public String toString() {
        return _publicationId;
    }

}

