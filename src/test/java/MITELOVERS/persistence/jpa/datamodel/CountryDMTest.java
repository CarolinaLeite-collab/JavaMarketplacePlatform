package MITELOVERS.persistence.jpa.datamodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryDMTest {

    @Test
    void constructorValidArgumentsCreatesDataModel() {
        // Act
        CountryDM countryDM = new CountryDM("PT", "PORTUGAL");

        // Assert
        assertNotNull(countryDM);
    }

    @Test
    void getCountryIdReturnsStoredId() {
        // SUT
        CountryDM countryDM = new CountryDM("PT", "PORTUGAL");

        // Act + Assert
        assertEquals("PT", countryDM.getCountryId());
    }

    @Test
    void getCountryNameReturnsStoredName() {
        // SUT
        CountryDM countryDM = new CountryDM("PT", "PORTUGAL");

        // Act + Assert
        assertEquals("PORTUGAL", countryDM.getCountryName());
    }
}