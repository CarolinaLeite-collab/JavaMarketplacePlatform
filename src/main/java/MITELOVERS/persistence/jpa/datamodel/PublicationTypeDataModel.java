package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.publicationtype.PublicationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link PublicationType} information, allowing its persistence in a database.
 */

@Generated
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PublicationTypes")
public class PublicationTypeDataModel {

    @Id
    private String publicationTypeId;

}
