package TOPSECRET.domain.city;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CityId;
import TOPSECRET.domain.valueobject.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    private Country _countryDouble;
    private Country _countryDouble2;
    private CountryId _countryIdPT;
    private CountryId _countryIdES;

    @BeforeEach
    void setUp() {
        _countryIdPT = new CountryId("PT");
        _countryIdES = new CountryId("ES");

        _countryDouble = mock(Country.class);
        _countryDouble2 = mock(Country.class);

        when(_countryDouble.identity()).thenReturn(_countryIdPT);
        when(_countryDouble.getCountryName()).thenReturn("Portugal");
        when(_countryDouble2.identity()).thenReturn(_countryIdES);
        when(_countryDouble2.getCountryName()).thenReturn("Spain");
    }

    @Test
    void shouldConstructCity() {
        City city = new City("Porto", _countryDouble);

        assertNotNull(city);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new City(null, _countryDouble));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new City("   ", _countryDouble));
    }

    @Test
    void shouldThrowWhenCountryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new City("Porto", null));
    }

    @Test
    void shouldReturnName() {
        City city = new City("Porto", _countryDouble);

        assertEquals("Porto", city.getName());
    }

    @Test
    void shouldReturnCountry() {
        City city = new City("Porto", _countryDouble);

        assertSame(_countryDouble, city.getCountry());
    }

    @Test
    void shouldReturnIdentity() {
        City city = new City("Porto", _countryDouble);

        assertNotNull(city.identity());
        assertEquals(new CityId("Porto", _countryIdPT), city.identity());
    }

    @Test
    void shouldBeEqualWhenSameNameAndCountry() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("Porto", _countryDouble);

        assertEquals(city1, city2);
    }

    @Test
    void shouldBeEqualWhenSameNameDifferentCase() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("PORTO", _countryDouble);

        assertEquals(city1, city2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentName() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("Lisboa", _countryDouble);

        assertNotEquals(city1, city2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentCountry() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("Porto", _countryDouble2);

        assertNotEquals(city1, city2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        City city = new City("Porto", _countryDouble);

        assertNotEquals(null, city);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        City city = new City("Porto", _countryDouble);

        assertNotEquals("Porto", city);
    }

    @Test
    void shouldHaveSameHashCodeWhenEqual() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("Porto", _countryDouble);

        assertEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenNotEqual() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("Lisboa", _countryDouble);

        assertNotEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void sameAsShouldReturnTrueWhenSameCityId() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("Porto", _countryDouble);

        assertTrue(city1.sameAs(city2));
    }

    @Test
    void sameAsShouldReturnFalseWhenDifferentCityId() {
        City city1 = new City("Porto", _countryDouble);
        City city2 = new City("Lisboa", _countryDouble);

        assertFalse(city1.sameAs(city2));
    }

    @Test
    void sameAsShouldReturnFalseWhenNotCity() {
        City city = new City("Porto", _countryDouble);

        assertFalse(city.sameAs("Porto"));
    }

    @Test
    void toStringShouldReturnNameAndCountryName() {
        City city = new City("Porto", _countryDouble);

        assertEquals("Porto, Portugal", city.toString());
    }
}