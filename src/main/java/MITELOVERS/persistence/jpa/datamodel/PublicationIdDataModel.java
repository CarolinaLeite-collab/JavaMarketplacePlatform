package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.valueobject.PublicationId;
import jakarta.persistence.Embeddable;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link PublicationId} value object,
 * enabling its persistence as part of a JPA entity.
 */

@Generated
@Getter
@NoArgsConstructor
@Embeddable
public class PublicationIdDataModel {

    private String publicationId;

    public PublicationIdDataModel(String publicationId) {

        this.publicationId = publicationId;

    }

}
