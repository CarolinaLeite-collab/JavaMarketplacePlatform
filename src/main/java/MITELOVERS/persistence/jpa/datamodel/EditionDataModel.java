package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.Binding;
import MITELOVERS.domain.valueobject.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link Edition} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "editions")
public class EditionDataModel {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "type_id", nullable = false)
    private String typeId;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "identifier_type", nullable = false)
    private String identifierType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "pubIdTitle", column = @Column(name = "pub_title")),
            @AttributeOverride(name = "pubIdAuthorId", column = @Column(name = "pub_author_id")),
            @AttributeOverride(name = "pubIdYear", column = @Column(name = "pub_year"))
    })
    private PublicationIdDataModel publicationIdDm;

    @Column(name = "pub_company_id", nullable = false)
    private String publishingCompanyId;

    @Column(name = "publishing_year", nullable = false)
    private int publishingYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "edition_language", nullable = false)
    private Language editionLanguage;

    // Optional
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "width", column = @Column(name = "dim_width")),
            @AttributeOverride(name = "height", column = @Column(name = "dim_height")),
            @AttributeOverride(name = "thickness", column = @Column(name = "dim_thickness")),
            @AttributeOverride(name = "unit", column = @Column(name = "dim_unit"))
    })
    private DimensionDataModel dimensionDm;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "weight_value")),
            @AttributeOverride(name = "unit", column = @Column(name = "weight_unit"))
    })
    private WeightDataModel weightDm;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Column(name = "edition_number")
    private Integer editionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "binding")
    private Binding binding;

}
