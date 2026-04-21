package MITELOVERS.domain.city;

import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CityTest {
    private CountryId _countryIdPT;

    @BeforeEach
    void setUp() {
        _countryIdPT = new CountryId("PT");
    }

    @Test
    void shouldConstructCity() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertNotNull(city);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City(null, _countryIdPT));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new City("   ", _countryIdPT));
    }

    @Test
    void shouldThrowWhenCountryIsNull() {
        //Act & Assert
        assertThrows(NullPointerException.class,
                () -> new City("Porto", null));
    }

    @Test
    void shouldReturnName() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertEquals("Porto", city.getName());
    }

    @Test
    void shouldReturnCountry() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertSame(_countryIdPT, city.getCountryId());
    }

    @Test
    void shouldReturnIdentity() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertEquals(new CityId("Porto", _countryIdPT), city.identity());
    }

    @Test
    void shouldBeEqualWhenSameNameAndCountry() {
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("Porto", _countryIdPT);

        //Assert
        assertEquals(city1, city2);
    }

    @Test
    void shouldBeEqualWhenSameNameDifferentCase() {
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("PORTO", _countryIdPT);

        //Assert
        assertEquals(city1, city2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentName() {
        //Act
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("Lisboa", _countryIdPT);

        //Assert
        assertNotEquals(city1, city2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentCountry() {
        //Arrange
        CountryId countryIdES = new CountryId("ES");

        //Act
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("Porto", countryIdES);

        //Assert
        assertNotEquals(city1, city2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertNotEquals(null, city);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertNotEquals("Porto", city);
    }

    @Test
    void shouldHaveSameHashCodeWhenEqual() {
        //Act
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("Porto", _countryIdPT);

        //Assert
        assertEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenNotEqual() {
        //Act
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("Lisboa", _countryIdPT);

        //Assert
        assertNotEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void sameAsShouldReturnTrueWhenSameCityId() {
        //Act
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("Porto", _countryIdPT);

        //Assert
        assertTrue(city1.sameAs(city2));
    }

    @Test
    void sameAsShouldReturnFalseWhenDifferentCityId() {
        //Act
        //SUT
        City city1 = new City("Porto", _countryIdPT);
        City city2 = new City("Lisboa", _countryIdPT);

        //Assert
        assertFalse(city1.sameAs(city2));
    }

    @Test
    void sameAsShouldReturnFalseWhenNotCity() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertFalse(city.sameAs("Porto"));
    }

    @Test
    void toStringShouldReturnNameAndCountryName() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertEquals("Porto, PT", city.toString());
    }

    @Test
    void equalsShouldReturnTrueForSameReference() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);

        //Assert
        assertTrue(city.equals(city));
    }

    @Test
    void equalsShouldReturnFalseForNonCityObject() {
        //Act
        //SUT
        City city = new City("Porto", _countryIdPT);
        String notACity = "Porto";

        //Assert
        assertFalse(city.equals(notACity));
    }
    
}
