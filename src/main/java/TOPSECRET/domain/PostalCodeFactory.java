package TOPSECRET.domain;

import java.util.Set;

public class PostalCodeFactory {
    private static final Set<String> FiveDigitPostalCodeCountry = Set.of(
            "GERMANY",
            "AUSTRIA",
            "SWITZERLAND",
            "ITALY",
            "SPAIN",
            "FRANCE",
            "FINLAND",
            "NORWAY",
            "DENMARK",
            "MEXICO",
            "TURKEY",
            "UKRAINE",
            "CROATIA",
            "SERBIA",
            "SLOVENIA",
            "SLOVAKIA",
            "CZECHIA"
    );

    public PostalCode createPostalCode(Country country, String code) {
        String countryName = country.getCountryName();

        if ("PORTUGAL".equals(countryName)) {
            return new PortuguesePostalCode(country, code);
        }
        if ("UNITED STATES".equals(countryName) ||
                "USA".equals(countryName) ||
                "US".equals(countryName)) {
            return new USZipCode(country, code);
        }
        if (FiveDigitPostalCodeCountry.contains(countryName)) {
            return new FiveDigitPostalCode(country, code);
        } else {
            return new GenericPostalCode(country, code);
        }
    }
}
