package MITELOVERS.domain.valueobject;

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
        //Act
        //SUT
        CityId cityId = new CityId("Porto", _countryIdPT);

        //Assert
        assertNotNull(cityId);
    }

    @Test
    void shouldThrowWhenCityNameIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CityId(null, _countryIdPT));
    }

    @Test
    void shouldThrowWhenCityNameIsBlank() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CityId("   ", _countryIdPT));
    }

    @Test
    void shouldThrowWhenCountryIdIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CityId("Porto", null));
    }

    @Test
    void shouldNormalizeCityNameToLowercase() {
        //Act
        //SUT
        CityId cityId = new CityId("PORTO", _countryIdPT);

        //Assert
        assertEquals("PTporto", cityId.getNormalizedName());
    }

    @Test
    void shouldTrimCityName() {
        //Act
        //SUT
        CityId cityId = new CityId("  Porto  ", _countryIdPT);

        //Assert
        assertEquals("PTporto", cityId.getNormalizedName());
    }

    @Test
    void shouldBeEqualWhenSameName() {
        //Act
        //SUT
        CityId cityId1 = new CityId("Porto");
        CityId cityId2 = new CityId("Porto");

        //Assert
        assertEquals(cityId1, cityId2);
    }

    @Test
    void shouldBeEqualWhenSameNameDifferentCase() {
        //Act
        //SUT
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("PORTO", _countryIdPT);

        //Assert
        assertEquals(cityId1, cityId2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentName() {
        //Act
        //SUT
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Lisboa", _countryIdPT);

        //Assert
        assertNotEquals(cityId1, cityId2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentCountry() {
        //Act
        //SUT
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Porto", _countryIdES);

        //Assert
        assertNotEquals(cityId1, cityId2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        //Act
        //SUT
        CityId cityId = new CityId("Porto", _countryIdPT);

        //Assert
        assertNotEquals(null, cityId);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        //Act
        //SUT
        CityId cityId = new CityId("Porto", _countryIdPT);

        //Assert
        assertNotEquals("porto, PT", cityId);
    }

    @Test
    void shouldHaveSameHashCodeWhenEqual() {
        //Act
        //SUT
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Porto", _countryIdPT);

        //Assert
        assertEquals(cityId1.hashCode(), cityId2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenNotEqual() {
        //Act
        //SUT
        CityId cityId1 = new CityId("Porto", _countryIdPT);
        CityId cityId2 = new CityId("Lisboa", _countryIdPT);

        //Assert
        assertNotEquals(cityId1.hashCode(), cityId2.hashCode());
    }

    @Test
    void toStringShouldReturnNormalizedNameAndCountryCode() {
        //Act
        //SUT
        CityId cityId = new CityId("Porto");

        //Assert
        assertEquals("Porto", cityId.toString());
    }
    @Test
    void equalsShouldReturnFalseWhenObjectIsDifferentType() {
        //Arrange
        Object other = new Object();

        //Act
        //SUT
        CityId cityId = new CityId("Porto", _countryIdPT);


        //Act & Assert
        assertFalse(cityId.equals(other));
    }

}
