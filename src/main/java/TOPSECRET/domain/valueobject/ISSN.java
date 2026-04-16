package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents an ISSN (International Standard Serial Number) for a magazine.
 * <p>
 * ensures that the ISSN follows the standard
 * format (four digits, a hyphen, three digits, and a check digit which may be 'X').
 * </p>
 */

public class ISSN implements Identifier, ValueObject {

    private final String _issn;

    public ISSN(String issn) {
        String normalized = normalize(issn);
        if (!isValidIssn(normalized)) {
            throw new IllegalArgumentException("Invalid ISSN format");
        }
        _issn = normalized;
    }

    private String normalize(String value) {
        if (value == null) {
            throw new IllegalArgumentException("ISSN cannot be null");
        }
        return value.replace("-", "")
                    .replace(" ", "")
                    .toUpperCase();
        }

        public static boolean isValidIssn (String s){
            if (s == null || s.length() != 8) return false;
            int sum = 0;

            for (int i = 0; i < 7; i++) {
                char c = s.charAt(i);
                if (!Character.isDigit(c)) return false;

                int digit = c - '0';
                sum += digit * (8 - i);
            }

            char last = s.charAt(7);
            int checkDigit;

            if (last == 'X') checkDigit = 10;
            else if (Character.isDigit(last)) checkDigit = last - '0';
            else return false;

            sum += checkDigit;

            return sum % 11 == 0;
        }

        public String get_issn () {
            return _issn;
        }

        public String toString () {
            return _issn;
        }

        @Override
        public String getIdentifier () {
            return _issn;
        }

        public boolean equals (Object o){
            if (this == o) return true;
            if (!(o instanceof ISSN other)) return false;
            return _issn.equals(other._issn);
        }

    }
