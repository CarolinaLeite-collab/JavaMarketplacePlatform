package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    private Address validAddress;

    @BeforeEach
    void setUp() {
        Address validAddress = new Address(
                "Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                "Lisboa", Address.Country.PORTUGAL, "1000-205", null);
    }

    @Test
    void constructor_validPortugalAddress() {
        Address address = new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE,
                "Lisboa", "Lisboa", Address.Country.PORTUGAL, "1000-205", null);

        assertEquals("Rua Vasco da Gama", address.getStreet());
        assertEquals("123", address.getDoorNumber());
        assertEquals(Address.BuildingType.HOUSE, address.getBuildingType());
        assertEquals("Lisboa", address.getCity());
        assertEquals("Lisboa", address.getDistrictOrState());
        assertEquals(Address.Country.PORTUGAL, address.getCountry());
        assertEquals("1000-205", address.getPostalCode());
        assertNull(address.getPostalCodeExtension());
    }

    @Test
    void constructor_nullStreet_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(null, "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", Address.Country.PORTUGAL, "1000-205", null));
    }
    @Test
    void constructor_emptyStreet_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Address("", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", Address.Country.PORTUGAL, "1000-205", null));
    }
    @Test
    void constructor_nullPostalCodeForPortugal_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", Address.Country.PORTUGAL, null, null));
    }
    @Test
    void constructor_invalidPortugalPostalCode_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", Address.Country.PORTUGAL, "9999-999", null));
    }
    @Test
    void constructor_validUSZipPlus4_accepted() {
        new Address("123 Main St", "Apt 4B", Address.BuildingType.APARTMENT,
                "New York", "NY", Address.Country.UNITED_STATES, "10001", "1234"); //Need to decide if we should keep extensions
    }

    //setter tests
    @Test
    void setStreet_valid() {
        Address address = new Address("  Rua Vasco da Gama  ", "123", Address.BuildingType.HOUSE,
                "Lisboa", null, Address.Country.PORTUGAL, "1000-205", null);
        address.setStreet("New Street  ");
        assertEquals("New Street", address.getStreet());
    }
    @Test
    void setStreet_emptyValue_throwsException() {
        Address address = new Address(" ", "123", Address.BuildingType.OTHER,
                "Lisboa", null, Address.Country.PORTUGAL, "1000-205", null);;
        assertThrows(IllegalArgumentException.class, () -> validAddress.setStreet(""));
    }
    @Test
    void setCountry_validCountryWithValidPostalCode_succeeds() {
        Address address = new Address("Rua Whatever", "123", Address.BuildingType.STORE,
                "Barcelona", null, Address.Country.SPAIN, "28301", null);;
        address.setCountry(Address.Country.SPAIN);
        assertEquals(Address.Country.SPAIN, address.getCountry());
    }
    @Test
    void setCountry_spainPostalCodeForPortugal_throwsException() {
        Address address = validAddress;
        assertThrows(IllegalArgumentException.class, () ->
                address.setCountry(Address.Country.SPAIN));
    }
    @Test
    void setPostalCode_validForCurrentCountry_succeeds() {
        Address address = validAddress;
        address.setPostalCode("1050-123");
        assertEquals("1050-123", address.getPostalCode());
    }
    @Test
    void setPostalCode_invalidForCurrentCountry_throwsException() {
        Address address = validAddress;
        assertThrows(IllegalArgumentException.class, () ->
                address.setPostalCode("99999"));  // Spain's postal code format not valid for Portugal
    }
    // I need to update dependencies in xml file
    //valid postal codes
    @ParameterizedTest
    @CsvSource({
            "1000-205, PORTUGAL",
            "28001, SPAIN",
            "75001, FRANCE",
            "10115, GERMANY",
            "00100, ITALY",
            "SW1A 1AA, UNITED_KINGDOM",
            "10001, UNITED_STATES",
            "90210-1234, UNITED_STATES"
    })
    public void validPostalCodes(String postalCode, Address.Country country) {
        Address address = simplifiedAddress(country, postalCode);
        assertDoesNotThrow(() -> address.setPostalCode(postalCode));
    }

    // Invalid postal codes
    @ParameterizedTest
    @CsvSource({
            "'', PORTUGAL",
            "'abc', SPAIN",
            "'12345', PORTUGAL",
            "'99999-999', PORTUGAL",     // Invalid first digit
            "'A1A 1A1', UNITED_STATES"
    })
    public void invalidPostalCodes(String postalCode, Address.Country country) {
        Address address = simplifiedAddress(country, "1000-205");
        assertThrows(IllegalArgumentException.class, () -> address.setPostalCode(postalCode));
    }

    private Address simplifiedAddress(Address.Country country, String postalCode) {
        return new Address("street", "1", Address.BuildingType.HOUSE, "City",
                null, country, postalCode, null);
    }

    //to string tests
    @Test
    void toString_withDistrict() {
        Address addr = new Address("Rua Example", "123", Address.BuildingType.OFFICE
                ,
                "Lisboa", "Centro", Address.Country.PORTUGAL, "1000-205", null);
        assertTrue(addr.toString().contains("PORTUGAL, Lisboa (Centro)"));
    }

    @Test
    void toString_withoutDistrict_omitsParentheses() {
        Address addr = new Address("Rua Example", "123", Address.BuildingType.HOUSE,
                "Lisboa", null, Address.Country.PORTUGAL, "1000-205", null);
        assertEquals("PORTUGAL, Lisboa , Rua Example, 123, HOUSE, 1000-205",
                addr.toString());
    }
    @Test
    void toString_withPostalExtension() {
        Address addr = new Address("123 Main St", "Apt 4B", Address.BuildingType.APARTMENT,
                "New York", "NY", Address.Country.UNITED_STATES, "10001", "1234"); //Need to decide if we should keep extensions
        assertEquals("UNITED_STATES, New York (NY), 123 Main St, APARTMENT, 10001-1234",
                addr.toString());
    }
}