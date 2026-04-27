package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.country.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * Data model object representing {@link Country} information, allowing its persistence in a database.
 */
@Getter
@Entity
@Table(name = "Countries")
public class CountryDataModel {

    @Id
    private String countryId;
    private String countryName;

    protected CountryDataModel() {}

    public CountryDataModel(String countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }
}
