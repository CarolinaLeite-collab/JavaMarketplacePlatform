package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityIdTest {

    private CountryId _countryIdPT;
    private CountryId _countryIdES;

    @BeforeEach
    void setUp() {
        _countryIdPT = new CountryId("PT");
        _countryIdES = new CountryId("ES");
    }

    @Test
    void shouldConstructCityId() {
        CityId cityId = new CityId("Porto", _countryIdPT);

        assertNotNull(cityId);
    }

    @Test
    void shouldThrowWhenCityNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new CityId(null, _countryIdPT));
    }

    @Test
    void shouldThrowWhenCityNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new CityId("   ", _countryIdPT));
    }

    @Test
    void shouldThrowWhenCountryIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new CityId("Porto", null));
    }

    @Test
    void shouldNormalizeCityNameToLowercase() {
        CityId cityId = new CityId("PORTO", _countryIdPT);

        assertEquals("porto", cityId.getNormalizedName());
    }

    @Test
    void shouldTrimCityName() {
        CityId cityId = new CityId("  Porto  ", _countryIdPT);

        assertEquals("porto", cityId.getNormalizedName());
    }

    @Test
    void shouldReturnCountryId() {
        CityId cityId = new CityId("Porto", _countryIdPT);

        assertEquals(_countryIdPT, cityId.getCountryId());
    }

    @Test
    void shouldBeEqualWhenSameNameAndCountry() {
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Porto", _countryIdPT);

        assertEquals(cityId1, cityId2);
    }

    @Test
    void shouldBeEqualWhenSameNameDifferentCase() {
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("PORTO", _countryIdPT);

        assertEquals(cityId1, cityId2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentName() {
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Lisboa", _countryIdPT);

        assertNotEquals(cityId1, cityId2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentCountry() {
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Porto", _countryIdES);

        assertNotEquals(cityId1, cityId2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        CityId cityId = new CityId("Porto", _countryIdPT);

        assertNotEquals(null, cityId);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        CityId cityId = new CityId("Porto", _countryIdPT);

        assertNotEquals("porto, PT", cityId);
    }

    @Test
    void shouldHaveSameHashCodeWhenEqual() {
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Porto", _countryIdPT);

        assertEquals(cityId1.hashCode(), cityId2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenNotEqual() {
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Lisboa", _countryIdPT);

        assertNotEquals(cityId1.hashCode(), cityId2.hashCode());
    }

    @Test
    void toStringShouldReturnNormalizedNameAndCountryCode() {
        CityId cityId = new CityId("Porto", _countryIdPT);

        assertEquals("porto, PT", cityId.toString());
    }
}