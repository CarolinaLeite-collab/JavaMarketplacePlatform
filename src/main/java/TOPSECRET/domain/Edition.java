package TOPSECRET.domain;

/**
 * Edition refers to a specific version of a publication.
 */

import java.time.LocalDate;

public class Edition {
    private final ISSN _issn;
    private final ISBN _isbn;
    private final int _numberOfPages;
    private final int _editionNumber;
    private final LocalDate _publicationDate;
    private final Binding _binding;
    private final Description _description;
    private final Dimension _dimension;
    private final Weight _weight;
    private final Language _language;

    // Constructor for Books
    public Edition (ISBN isbn, int numberOfPages, int editionNumber, LocalDate publicationDate, Binding binding,
                    Description description, Dimension dimension, Weight weight ,Language language) {

        _issn = null;
        _isbn = isbn;

        // An edition cannot have both ISSN and ISBN
        if (_isbn == null)
            throw new IllegalArgumentException("A Book with ISBN is required!");

        if (editionNumber <= 0)
            throw new IllegalArgumentException("Edition number needs to be positive");

        if (numberOfPages <= 0)
            throw new IllegalArgumentException("Number of pages cannot be zero or negative.");


        _numberOfPages = numberOfPages;
        _editionNumber = editionNumber;
        _publicationDate = publicationDate;
        _binding = binding;
        _description = description;
        _dimension = dimension;
        _weight = weight;
        _language = language;
    }

    // Constructor for Magazine
    public Edition (ISSN issn, int numberOfPages, int editionNumber, LocalDate publicationDate, Binding binding,
                    Description description, Dimension dimension, Weight weight ,Language language) {

        _isbn = null;
        _issn = issn;

        // An edition cannot have both ISSN and ISBN
        if (_issn == null)
            throw new IllegalArgumentException("A magazine ISSN is required!");

        if (editionNumber <= 0)
            throw new IllegalArgumentException("Edition number needs to be positive");

        if (numberOfPages <= 0)
            throw new IllegalArgumentException("Number of pages cannot be zero or negative.");


        _numberOfPages = numberOfPages;
        _editionNumber = editionNumber;
        _publicationDate = publicationDate;
        _binding = binding;
        _description = description;
        _dimension = dimension;
        _weight = weight;
        _language = language;
    }

    // Constructor for Books or Magazines without ISBN or ISSN
    public Edition (int numberOfPages, int editionNumber, LocalDate publicationDate, Binding binding,
                    Description description, Dimension dimension, Weight weight ,Language language) {

        _isbn = null;
        _issn = null;

        if (editionNumber < 0)
            throw new IllegalArgumentException("Edition number needs to be positive");

        if (numberOfPages <= 0)
            throw new IllegalArgumentException("Number of pages cannot be zero or negative.");


        _numberOfPages = numberOfPages;
        _editionNumber = editionNumber;
        _publicationDate = publicationDate;
        _binding = binding;
        _description = description;
        _dimension = dimension;
        _weight = weight;
        _language = language;
    }

    public ISSN getIssn() { return _issn; }
    public ISBN getIsbn() { return _isbn; }
    public int getNumberOfPages() { return _numberOfPages; }
    public int getEditionNumber() { return _editionNumber; }
    public LocalDate getPublicationDate() { return _publicationDate; }
    public Binding getBinding() { return _binding; }
    public Description getDescription() { return _description; }
    public Dimension getDimension() { return _dimension; }
    public Weight getWeight() { return _weight; }
    public Language getLanguage() { return _language; }

}
