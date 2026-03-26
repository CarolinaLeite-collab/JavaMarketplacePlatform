package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link City}.
 *
 * <p>{@link Country} is mocked as a dummy — City only holds a reference to it,
 * never interrogates its behaviour.</p>
 *
 * <p>No other Mockito doubles are used — {@link City} is a pure Value Object.</p>
 */

class CityTest {

    private Country _countryDouble;

    @BeforeEach
    void setUp() {
        _countryDouble = mock(Country.class);
    }

    private City city(String name) {
        return new City(name, _countryDouble);
    }


    @Test
    void constructor_validArgumentsCreatesCity() {
        // Arrange
        String name = "Porto";

        // SUT
        City city = new City(name, _countryDouble);

        // Assert
        assertAll(
                () -> assertEquals("Porto", city.getName()),
                () -> assertSame(_countryDouble, city.getCountry())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void constructorNullOrBlankName_throwsIllegalArgumentException(String badName) {
        assertThrows(IllegalArgumentException.class, () -> new City(badName, _countryDouble));
    }

    @Test
    void constructor_nullCountryThrows() {
        assertThrows(IllegalArgumentException.class, () -> new City("Porto", null));
    }

    @Test
    void equalsReflexive() {
        City city = new City("Porto", _countryDouble);
        assertEquals(city, city);
    }

    @Test
    void equalsReturnFalseForNullAndDifferentType() {
        City a = city("Lisboa");
        assertNotEquals(null, a);
        assertNotEquals(a, "Lisboa");
    }

    @Test
    void constructor_normalizesInternalWhitespace() {
        City city = new City("  New   York  ", _countryDouble);
        assertEquals("New York", city.getName());
    }

    @Test
    void equalsIgnoresCaseAndWhiteSpace() {
        City a = new City(" Porto ", _countryDouble);
        City b = new City("porto", _countryDouble);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalsSameNameDifferentCountryMocksFalse () {
        Country otherCountry = mock(Country.class);

        City a = new City("Lisboa", _countryDouble);
        City b = new City("Lisboa", otherCountry);

        assertNotEquals(a, b);
    }

    @Test
    void hashCodeIsCaseInsensitiveSameAsEqualsLogic () {
        City a = new City("PORTO", _countryDouble);
        City b = new City("porto", _countryDouble);

        assertEquals(a.hashCode(), b.hashCode());
    }
    @Test
    void hashCodeIsNonZeroForValidCity() {
        City a = new City("Porto", _countryDouble);

        // Assert
        assertNotEquals(0, a.hashCode());
    }

    @Test
    void toStringReturnsExpectedFormat() {

        // Arrange
        when(_countryDouble.getCountryName()).thenReturn("Portugal");

        // SUT
        City city = new City("Porto", _countryDouble);

        // Assert
        assertEquals("Porto, Portugal", city.toString());
    }
}
