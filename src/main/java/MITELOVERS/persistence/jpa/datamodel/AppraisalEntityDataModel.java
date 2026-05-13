package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.appraisalentity.AppraisalEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data model object representing {@link AppraisalEntity} information,
 * allowing its persistence in a database.
 */

@Generated
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "AppraisalEntities")
public class AppraisalEntityDataModel {

    @Id
    private String id;
    private String name;
    @ElementCollection
    @CollectionTable(
            name = "AppraisalEntityPublicationTypes",
            joinColumns = @JoinColumn(name = "id")
    )
    @Column(name = "PublicationTypesIds")
    private List<String> publicationTypeIds;
    @ElementCollection
    @CollectionTable(
            name = "AppraisalEntityGenres",
            joinColumns = @JoinColumn(name = "id")
    )
    @Column(name = "GenreIds")
    private List<String> genresIds;

}
