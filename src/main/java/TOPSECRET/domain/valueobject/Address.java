package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.Country;

/**
 * Represents a physical address with details such as street, door number, building type, city,
 * district/state, country, and postal code.
 * <p>
 * Provides validation for mandatory fields and ensures that postal codes conform to country-specific formats.
 * </p>
 */

public class Address implements ValueObject {

    public enum BuildingType {
        HOUSE,
        APARTMENT,
        OFFICE,
        STORE,
        OTHER
    }
    private Country _country;
    private String _street;
    private String _doorNumber;
    private BuildingType _buildingType;
    private String _city;
    private String _districtOrState;
    private String _postalCode;
    private String _postalCodeExtension;

    public Address(String street,
                   String doorNumber,
                   BuildingType buildingType,
                   String city,
                   String districtOrState,
                   Country country,
                   String postalCode,
                   String postalCodeExtension) {

        validateFields(street, doorNumber, buildingType, city, country, postalCode);

        _street = street;
        _doorNumber = doorNumber;
        _buildingType = buildingType;
        _city = city;
        _districtOrState = districtOrState;
        _country = country;
        _postalCode = postalCode;
        _postalCodeExtension = postalCodeExtension;
    }
    public String getStreet() { return _street; }
    public String getDoorNumber() { return _doorNumber; }
    public BuildingType getBuildingType() { return _buildingType; }
    public String getCity() { return _city; }
    public String getDistrictOrState() { return _districtOrState; }
    public Country getCountry() { return _country; }
    public String getPostalCode() { return _postalCode; }
    public String getPostalCodeExtension() { return _postalCodeExtension; }

    public void setStreet(String street) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be empty!");
        }
        _street = street.trim();
    }
    public void setDoorNumber(String doorNumber) {
        if (doorNumber == null || doorNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Door number cannot be empty!");
        }
        _doorNumber = doorNumber.trim();
    }
    public void setBuildingType(BuildingType buildingType) {
        if (buildingType == null) {
            throw new IllegalArgumentException("Must select a building type!");
        }
        _buildingType = buildingType;
    }
    public void setCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty!");
        }
        _city = city.trim();
    }
    // District or State isn't mandatory, there's no need for validation
    public void setDistrictOrState(String districtOrState) { _districtOrState = districtOrState; }


    public void setPostalCode(String postalCode) {
        if (!isValidPostalCode(postalCode, _country)) {
            throw new IllegalArgumentException( "Invalid postal code '" + postalCode + "' for " + _country);
        }
        _postalCode = postalCode.trim();
    }
    // Postal code extension isn't mandatory, there's no need for validation
    public void setPostalCodeExtension(String postalCodeExtension) { _postalCodeExtension = postalCodeExtension; }


    private void validateFields(String street,
                                String doorNumber,
                                BuildingType buildingType,
                                String city,
                                Country country,
                                String postalCode) {

        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be empty!");
        }
        if (doorNumber == null || doorNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Door number cannot be empty!");
        }
        if (buildingType == null) {
            throw new IllegalArgumentException("Must select a building type!");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty!");
        }
        if (country == null) {
            throw new IllegalArgumentException("Must select a country!");
        }
        if (!isValidPostalCode(postalCode, country)) {
            throw new IllegalArgumentException("Invalid postal code '" + postalCode + "' for " + country);
        }
    }

    //Postal code validation
    private boolean isValidPostalCode(String postalCode, Country country) {
        if (postalCode == null || postalCode.trim().isEmpty()) return false;
        if (country == null)  return false;

        String trimmed = postalCode.trim();
        String c = country.getCountryName();
        if (c == null) return false;

        c = c.trim().toLowerCase();

        return switch (c) {
            case "portugal" -> trimmed.matches("[1-9]\\d{3}-\\d{3}");
            case "spain", "germany", "italy", "france", "united states" -> trimmed.matches("\\d{5}");
            /*case FRANCE -> trimmed.matches("0[1-9]\\d{3}|9[78]\\d{2}");*/
            case "united kingdom" -> trimmed.matches("^[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}$");
            default -> false; //It's not complete yet!
        };
    }

    @Override
    public String toString() {

        String fullPostalCode = _postalCode;
        if (_postalCodeExtension != null && !_postalCodeExtension.isEmpty()) {
            fullPostalCode += "-" + _postalCodeExtension;
        }
        String checkForDistrictOrState = (_districtOrState != null && !_districtOrState.trim().isEmpty()) ? "(" + _districtOrState + ")" : "";

        return _country + ", " + _city + " " + checkForDistrictOrState + ", " + _street + ", " + _doorNumber + ", " + _buildingType + ", " + fullPostalCode;
    }

    public void changeCountryAndPostalCode(Country country, String postalCode) {
        if (country == null) {
            throw new IllegalArgumentException("Must select a country!");
        }
        if (!isValidPostalCode(postalCode, country)) {
            throw new IllegalArgumentException("Invalid postal code '" + postalCode + "' for " + country);
        }

        _country = country;
        _postalCode = postalCode.trim();
    }
}
