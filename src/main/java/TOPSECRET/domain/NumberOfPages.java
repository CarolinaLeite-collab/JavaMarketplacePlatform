package TOPSECRET.domain;

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
}
