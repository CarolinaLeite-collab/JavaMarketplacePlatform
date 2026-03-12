package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddressTest {

    private Address _validAddress;
    private Country _countryPortugal;

    @BeforeEach
    void setUp() {
        _countryPortugal = mock(Country.class);
        when(_countryPortugal.getCountryName()).thenReturn("PORTUGAL"); // used by isValidPostalCode
        when(_countryPortugal.toString()).thenReturn("PORTUGAL");       // used by Address.toString()

    }

    // -------------------- Constructor --------------------

    @Test
    void constructor_validPortugalAddress() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertEquals("Rua Vasco da Gama", _validAddress.getStreet());
        assertEquals("123", _validAddress.getDoorNumber());
        assertEquals(Address.BuildingType.HOUSE, _validAddress.getBuildingType());
        assertEquals("Lisboa", _validAddress.getCity());
        assertEquals("Lisboa", _validAddress.getDistrictOrState());
        assertSame(_countryPortugal, _validAddress.getCountry());
        assertEquals("1000-205", _validAddress.getPostalCode());
        assertNull(_validAddress.getPostalCodeExtension());
    }

    @Test
    void constructor_nullStreet_throwsException() {
        //act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address(null, "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", _countryPortugal, "1000-205", null));
    }

    @Test
    void constructor_emptyStreet_throwsException() {
        //act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address("", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", _countryPortugal, "1000-205", null));
    }

    @Test
    void constructor_nullPostalCode_throwsException() {
        //act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", _countryPortugal, null, null));
    }

    @Test
    void constructor_invalidPortugalPostalCode_throwsException() {
        //act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", _countryPortugal, "99998", null));
    }

    @Test
    void constructor_validUSZipPlus4_extensionAccepted_butPostalValidationIsForBaseZip() {
        //arrange
        Country us = mock(Country.class);
        when(us.getCountryName()).thenReturn("United States");
        when(us.toString()).thenReturn("UNITED_STATES");

        //act + assert
        assertDoesNotThrow(() ->
                new Address("123 Main St", "Apt 4B", Address.BuildingType.APARTMENT,
                        "New York", "NY", us, "10001", "1234"));
    }

    // -------------------- Setters (basic fields) --------------------

    @Test
    void setStreet_valid_trimsAndUpdates() {
        //SUT
        Address address = new Address("  Rua Vasco da Gama  ", "123", Address.BuildingType.HOUSE,
                "Lisboa", null, _countryPortugal, "1000-205", null);

        //act
        address.setStreet("New Street  ");

        //assert
        assertEquals("New Street", address.getStreet());
    }

    @Test
    void setStreet_empty_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setStreet(""));
    }

    @Test
    void setDoorNumber_valid_trimsAndUpdates() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act
        _validAddress.setDoorNumber("  1000  ");

        //assert
        assertEquals("1000", _validAddress.getDoorNumber());
    }

    @Test
    void setDoorNumber_null_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setDoorNumber(null));
    }

    @Test
    void setDoorNumber_empty_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setDoorNumber(""));
    }

    @Test
    void setDoorNumber_spaces_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setDoorNumber("   "));
    }

    @Test
    void setBuildingType_valid_updates() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act
        _validAddress.setBuildingType(Address.BuildingType.OTHER);

        //assert
        assertEquals(Address.BuildingType.OTHER, _validAddress.getBuildingType());
    }

    @Test
    void setBuildingType_null_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setBuildingType(null));
    }

    @Test
    void setCity_valid_trimsAndUpdates() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act
        _validAddress.setCity("Braga   ");

        //assert
        assertEquals("Braga", _validAddress.getCity());
    }

    @Test
    void setCity_null_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setCity(null));
    }

    @Test
    void setCity_empty_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setCity(""));
    }

    @Test
    void setCity_spaces_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setCity("   "));
    }

    // -------------------- Postal code / atomic country change behavior --------------------
    @Test
    void setPostalCode_validForPortugal_succeeds() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act
        _validAddress.setPostalCode("1050-123");

        //assert
        assertEquals("1050-123", _validAddress.getPostalCode());
    }

    @Test
    void setPostalCode_invalidForPortugal_throwsException() {
        //SUT
        _validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", _countryPortugal, "1000-205", null
        );

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _validAddress.setPostalCode("99999"));
    }

    @Test
    void changeCountryAndPostalCode_validChangeToUnitedStates_succeeds() {
        //arrange
        Country us = mock(Country.class);
        when(us.getCountryName()).thenReturn("United States");
        when(us.toString()).thenReturn("UNITED_STATES");

        //SUT
        Address addr = new Address("Rua X", "1", Address.BuildingType.HOUSE,
                "Lisboa", null, _countryPortugal, "1000-205", null);

        //act
        addr.changeCountryAndPostalCode(us, "10001");

        //assert
        assertSame(us, addr.getCountry());
        assertEquals("10001", addr.getPostalCode());
    }

    @Test
    void changeCountryAndPostalCode_throws_whenPostalCodeInvalidForUnitedKingdom() {
        //arrange
        Country uk = mock(Country.class);
        when(uk.getCountryName()).thenReturn("United Kingdom");
        when(uk.toString()).thenReturn("UNITED_KINGDOM");

        //SUT
        Address addr = new Address("Rua X", "1", Address.BuildingType.HOUSE,
                "Lisboa", null, _countryPortugal, "1000-205", null);

        //act + assert
        assertThrows(IllegalArgumentException.class,
                () -> addr.changeCountryAndPostalCode(uk, "1000-205"));
    }

    // -------------------- Parameterized: postal codes by country --------------------

    @ParameterizedTest
    @CsvSource({
            "1000-205, Portugal",
            "28001, Spain",
            "75001, France",
            "10115, Germany",
            "00100, Italy",
            "SW1A 1AA, United Kingdom",
            "10001, United States",
            "90210, United States"
    })
    void validPostalCodes(String postalCode, String countryName) {
        //arrange
        Country c = mock(Country.class);
        when(c.getCountryName()).thenReturn(countryName);

        //SUT
        Address address = new Address("street", "1", Address.BuildingType.HOUSE,
                "city", null, c, postalCode, null);

        //act + assert
        assertDoesNotThrow(() -> address.setPostalCode(postalCode));
    }

    @ParameterizedTest
    @CsvSource({
            "'', Portugal",
            "abc, Spain",
            "12345, Portugal",
            "99999-999, Portugal",
            "A1A 1A1, United States"
    })
    void invalidPostalCodes(String postalCode, String countryName) {
        //arrange
        Country c = mock(Country.class);
        when(c.getCountryName()).thenReturn(countryName);

        //SUT
        Address address = new Address("street", "1", Address.BuildingType.HOUSE,
                "city", null, c, validPostalCodeForCountry(countryName), null);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> address.setPostalCode(postalCode));
    }

    private String validPostalCodeForCountry(String countryName) {
        String c = countryName.trim().toLowerCase();

        return switch (c) {
            case "spain" -> "12345";
            case "portugal" -> "1000-036";
            case "france" -> "13001";
            case "italy" -> "00134";
            case "germany" -> "10115";
            case "united kingdom" -> "W1A 1AA";
            case "united states" -> "10001";
            default -> "1000-036";
        };
    }

    // -------------------- toString --------------------

    @Test
    void toString_withDistrict_containsDistrictInParentheses() {
        //SUT
        Address addr = new Address("Rua Example", "123", Address.BuildingType.OFFICE,
                "Lisboa", "Centro", _countryPortugal, "1000-205", null);

        //act + assert
        assertTrue(addr.toString().contains("PORTUGAL, Lisboa (Centro)"));
    }

    @Test
    void toString_withoutDistrict_omitsParentheses() {
        //SUT
        Address addr = new Address("Rua Example", "123", Address.BuildingType.HOUSE,
                "Lisboa", null, _countryPortugal, "1000-205", null);

        //act + assert
        assertEquals("PORTUGAL, Lisboa , Rua Example, 123, HOUSE, 1000-205",
                addr.toString());
    }

    @Test
    void toString_withPostalExtension_appendsExtension() {
        //arrange
        Country us = mock(Country.class);
        when(us.getCountryName()).thenReturn("United States");
        when(us.toString()).thenReturn("UNITED_STATES");

        //SUT
        Address addr = new Address("123 Main St", "Apt 4B", Address.BuildingType.APARTMENT,
                "New York", "NY", us, "10001", "1234");

        //act + assert
        assertEquals("UNITED_STATES, New York (NY), 123 Main St, Apt 4B, APARTMENT, 10001-1234",
                addr.toString());
    }
}