package TOPSECRET.domain;

public class NumberOfPages {
    private int number_of_pages;

    public NumberOfPages(int num) {
        // Throws exception if number of pages is zero or negative
        if (num <= 0)
            throw new IllegalArgumentException("Number of pages cannot be zero or negative.");

        this.number_of_pages = num;
    }

    public int getNumberOfPages() {
        return this.number_of_pages;
    }
}

