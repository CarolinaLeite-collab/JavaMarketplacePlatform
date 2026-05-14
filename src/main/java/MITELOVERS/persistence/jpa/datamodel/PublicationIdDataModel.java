package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import jakarta.persistence.Embeddable;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Year;

/**
 * Data model object representing {@link PublicationId} value object,
 * enabling its persistence as part of a JPA entity.
 */

@Generated
@Getter
@NoArgsConstructor
@Embeddable
public class PublicationIdDataModel {
    private String title;
    private String authorId;
    private int releaseYear;

    public PublicationIdDataModel(String title, String authorId, int releaseYear) {
        this.title = title;
        this.authorId = authorId;
        this.releaseYear = releaseYear;
    }
}
