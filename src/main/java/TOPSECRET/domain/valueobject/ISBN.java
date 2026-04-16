package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * International Standard Book Number: an identifier for a book, consisting of a unique numerical code assigned to each book edition published since 1970.
 * There are two types of ISBN:
 * a 10-digit code for books published since 1970
 * and a 13-digit code for books published after 2007.
 * An ISBN-10 can be converted to an ISBN-13 by adding a "978" prefix and recalculating the check digit.
 * ISBN-10 and ISBN-13 can coexist for the same edition.
 */

public class ISBN implements Identifier, ValueObject {

    private final String _isbn;

    public ISBN(String isbn) {

        String normalized = normalize(isbn);

        if (isValidIsbn10(normalized) || isValidIsbn13(normalized)) {
            _isbn = normalized;
        } else {
            throw new IllegalArgumentException("Invalid ISBN");
        }
    }

    @Override
    public String getIdentifier() {
        return _isbn;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISBN other)) return false;
        return toIsbn13(_isbn).equals(toIsbn13(other._isbn));
    }

    public int hashCode() {
        return toIsbn13(_isbn).hashCode();
    }

    public static boolean isValidIsbn10(String s) {
        if (s == null || s.length() != 10) return false;
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) return false;
            int d = c - '0';
            int weight = 10 - i;
            sum += weight * d;
        }

        char last = s.charAt(9);
        int d10;
        if (last == 'X') d10 = 10;
        else if (Character.isDigit(last)) d10 = last - '0';
        else return false;

        sum += d10;

        return sum % 11 == 0;
    }

    public static boolean isValidIsbn13(String s) {

        if (s == null || s.length() != 13) return false;

        int sum = 0;

        for (int i = 0; i < 12; i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) return false;

            int digit = c - '0';

            sum += (i % 2 == 0) ? digit : 3 * digit;
        }

        char last = s.charAt(12);
        if (!Character.isDigit(last)) return false;
        int actualCheck = last - '0';

        int expectedCheck = (10 - (sum % 10)) % 10;

        return actualCheck == expectedCheck;
    }

    private String normalize(String originalIsbn) {
        if (originalIsbn == null) {
            throw new IllegalArgumentException("ISBN cannot be null");
        }
        String result = originalIsbn.replace("-", "")
                .replace(" ", "")
                .toUpperCase();
        return result;
    }

    public static String toIsbn13(String isbn) {
        if (isValidIsbn13(isbn)) {
            return isbn;
        }
        if (!isValidIsbn10(isbn)) {
            throw new IllegalArgumentException("Invalid ISBN");
        }

        String core = isbn.substring(0, 9);

        String isbn12 = "978" + core;

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int d = isbn12.charAt(i) - '0';
            sum += (i % 2 == 0) ? d : 3 * d;
        }

        int check = (10 - (sum % 10)) % 10;

        return isbn12 + check;
    }

    public String toString() {
        return _isbn;
    }

}
