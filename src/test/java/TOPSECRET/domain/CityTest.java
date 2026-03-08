package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityTest {

    private Country country;

    @BeforeEach
    void setUp() {
        country = mock(Country.class);
    }

    private City city(String name) {
        return new City(name, country);
    }


    @Test
    void constructor_validArgumentsCreatesCity() {
        // Arrange
        String name = "Porto";

        // Act
        City city = new City(name, country);

        // Assert
        assertAll(
                () -> assertEquals("Porto", city.getName()),
                () -> assertSame(country, city.getCountry())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void constructorNullOrBlankName_throwsIllegalArgumentException(String badName) {
        assertThrows(IllegalArgumentException.class, () -> new City(badName, country));
    }

    @Test
    void constructor_nullCountryThrows() {
        assertThrows(IllegalArgumentException.class, () -> new City("Porto", null));
    }

    @Test
    void equalsReflexive() {
        City city = new City("Porto", country);
        assertEquals(city, city);
    }

    @Test
    void equalsReturnFalseForNullAndDifferentType() {
        City a = city("Lisboa");
        assertNotEquals(null, a);
        assertNotEquals("Lisboa", a);
    }

    @Test
    void equalsCaseInsensitiveNameSameCountryMockTrueAndHashCodeEqual() {
        City a = new City("Porto", country);
        City b = new City("porto", country);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalsSameNameDifferentCountryMocksFalse () {
        Country otherCountry = mock(Country.class);

        City a = new City("Lisboa", country);
        City b = new City("Lisboa", otherCountry);

        assertNotEquals(a, b);
    }

    @Test
    void hashCodeIsCaseInsensitiveSameAsEqualsLogic () {
        City a = new City("PORTO", country);
        City b = new City("porto", country);

        assertEquals(a.hashCode(), b.hashCode());
    }
}
