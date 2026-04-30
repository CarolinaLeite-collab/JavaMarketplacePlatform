package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "cities")
public class CityDataModel {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String _cityId;

    @Column(name = "name", nullable = false)
    private String _name;

    @Column(name = "countryId",  nullable = false)
    private String _countryId;

}
