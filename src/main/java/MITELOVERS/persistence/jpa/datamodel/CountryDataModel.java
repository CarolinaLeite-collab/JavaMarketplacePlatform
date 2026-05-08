package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.country.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link Country} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Countries")
public class CountryDataModel {

    @Id
    @Column (name = "Country_Id", nullable = false, unique = true)
    private String countryId;

    @Column (name = "Country_Name", nullable = false, unique = true)
    private String countryName;
}
