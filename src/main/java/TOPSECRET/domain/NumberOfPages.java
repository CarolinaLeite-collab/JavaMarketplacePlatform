package TOPSECRET.domain;

import java.util.Objects;

public class NumberOfPages {
    private int _numberOfPages;

    public NumberOfPages(int numberOfPages) {
        if (numberOfPages <= 0) {
            throw new IllegalArgumentException("Number of pages needs to be positive");
        }
        _numberOfPages = numberOfPages;
    }

    public int getNumberOfPages() {
        return _numberOfPages;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NumberOfPages that)) return false;
        return _numberOfPages == that._numberOfPages;
    }

}
