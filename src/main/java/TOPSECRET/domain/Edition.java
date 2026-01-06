package TOPSECRET.domain;

/**
 * Edition refers to a specific version of a publication.
 */

import java.time.LocalDate;

public class Edition {
    private final ISSN _issn;
    private final ISBN _isbn;
    private final NumberOfPages _numberOfPages;
    private final int _editionNumber;
    private final LocalDate _publicationDate;
    private final Binding _binding;
    private final Description _description;
    private final Dimension _dimension;
    private final Weight _weight;
    private final Language _language;

    public Edition (ISSN issn, ISBN isbn, NumberOfPages numberOfPages, int editionNumber, LocalDate publicationDate, Binding binding,
                    Description description, Dimension dimension, Weight weight ,Language language) {

        // An edition cannot have both ISSN and ISBN
        if (issn != null && isbn != null)
            throw new IllegalArgumentException("An edition only can have on code: ISSN or ISBN");

        if (editionNumber <= 0)
            throw new IllegalArgumentException("Edition number needs to be positive");

        _issn = issn;
        _isbn = isbn;
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
    public NumberOfPages getNumberOfPages() { return _numberOfPages; }
    public int getEditionNumber() { return _editionNumber; }
    public LocalDate getPublicationDate() { return _publicationDate; }
    public Binding getBinding() { return _binding; }
    public Description getDescription() { return _description; }
    public Dimension getDimension() { return _dimension; }
    public Weight getWeight() { return _weight; }
    public Language getLanguage() { return _language; }

}
