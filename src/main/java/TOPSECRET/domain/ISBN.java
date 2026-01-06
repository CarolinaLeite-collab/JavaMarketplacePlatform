package TOPSECRET.domain;

public class    ISBN {

    /**
     * International Standard Book Number: an identifier for a book, consisting of a unique numerical code assigned to each published book edition.
     * There are two types of ISBN:
     * a 10-digit code for books published since 1970
     * and a 13-digit code for books published after 2007.
     * An ISBN-10 can be converted to an ISBN-13 by adding a "978" prefix and recalculating the check digit.
     * ISBN-10 and ISBN-13 can coexist for the same edition.
     */
    private long _number;

    private ISBN() {
    }

    public ISBN(long isbn) {
        if (isValid(isbn)) {
            _number = isbn;
        } else {
            throw new IllegalArgumentException("Invalid ISBN");
        }
    }

    private boolean isValid(long isbn) {
        boolean result = false;

        long check13 = isbn / 1000000000000L;
        if (check13 < 10 && check13 > 0) {
            result = true;
        }

        long check10 = isbn / 1000000000L;
        if (check10 < 10 && check10 > 0) {
            result = true;
        }

        return result;
    }

    public boolean isSameISBN(long otherIsbnNumber) {
        return _number == otherIsbnNumber;
    }

}
