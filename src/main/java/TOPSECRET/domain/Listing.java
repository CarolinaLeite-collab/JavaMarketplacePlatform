package TOPSECRET.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The entity responsible for publication. Cannot be null, empty, or whitespace‑only. There can only be either one book or magazine.
 */

public class Listing {
    private Price _price;
    private final LocalDate _createdDate;
    private Description _description;
    private final SKU _sku;
    private Book _book;
    private Magazine _magazine;
    private final User _seller;
    private List<String> _urls;

    //Listing of a book construct
    public Listing (Book book, Price price, User seller, SKU sku, Description description, LocalDate createdDate, List<String> urls) {

        //TODO: Verify if SKU is unique

        if (price == null || description == null || sku == null || book == null || seller == null || urls == null) {
            throw new IllegalArgumentException("Listing parameters cannot be null");
        }

        _magazine = null;
        _book = book;
        _price = price;
        _seller = seller;
        _sku = sku;
        _description = description;
        _createdDate = getFormattedDate(createdDate);
        _urls =  urls;
    }

    //Listing of a magazine constructor
    public Listing (Magazine magazine, Price price, User seller, SKU sku, Description description, LocalDate createdDate, List<String> urls) {

        //TODO: Verify if SKU is unique

        if (price == null || description == null || sku == null || magazine == null || seller == null ||  urls == null) {
            throw new IllegalArgumentException("Listing parameters cannot be null");
        }

        _book = null;
        _magazine = magazine;
        _price = price;
        _seller = seller;
        _sku = sku;
        _description = description;
        _createdDate = getFormattedDate(createdDate);
        _urls =  urls;
    }

    public LocalDate getFormattedDate(LocalDate createdDate) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateString = createdDate.format(formatter);

        return LocalDate.parse( dateString , formatter);
    }


    //Getters
    public Price getPrice() {
        return _price;
    }

    public LocalDate getCreatedDate() {
        return _createdDate;
    }

    public Description getDescription() {
        return _description;
    }

    public SKU getSku() {
        return _sku;
    }

    public Book getBook() {
        return _book;
    }

    public Magazine getMagazine() {
        return _magazine;
    }

    public User getSeller() {
        return _seller;
    }

    public List<String> getUrls() {
        return List.copyOf(_urls);
    }
}