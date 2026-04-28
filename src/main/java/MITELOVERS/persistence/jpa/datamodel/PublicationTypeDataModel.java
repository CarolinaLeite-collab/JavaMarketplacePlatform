package MITELOVERS.persistence.jpa.datamodel;

import  MITELOVERS.domain.publicationtype.PublicationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link PublicationType} information, allowing its persistence in a database.
 */


@Entity
@Getter
@Table(name = "PublicationTypes")
@NoArgsConstructor
@AllArgsConstructor
public class PublicationTypeDataModel {

    @Id
    private String publicationTypeId;

}
