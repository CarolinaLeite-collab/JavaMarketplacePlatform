package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link PublishingCompany} information,
 * allowing its persistence in a database.
 */
@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PublishingCompanies")

public class PublishingCompanyDataModel {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String publishingCompanyId;

}
