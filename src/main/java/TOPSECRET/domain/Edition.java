package TOPSECRET.domain;
import TOPSECRET.domain.valueobject.Binding;
import TOPSECRET.domain.valueobject.Dimension;
import TOPSECRET.domain.valueobject.ISSN;
import TOPSECRET.domain.valueobject.Language;

import java.time.LocalDate;

/**
 * Represents a specific edition of a publication, such as a book or magazine.
 * <p>
 * Contains information including ISBN or ISSN, number of pages, edition number, publication date,
 * binding, description, physical dimensions, weight, and language.
 * Validates that editions have positive page counts and edition numbers, and ensures that books have ISBN and magazines have ISSN when required.
 * </p>
 */

public class Edition {
    private final ISSN _issn;
    private final ISBN _isbn;
    private final NumberOfPages _numberOfPages;
    private final Integer _editionNumber;
    private final LocalDate _publicationDate;
    private final Binding _binding;
    private final Description _description;
    private final Dimension _dimension;
    private final Weight _weight;
    private final Language _language;

    // Constructor for Books
    public Edition (ISBN isbn, NumberOfPages numberOfPages, Integer editionNumber, LocalDate publicationDate, Binding binding,
                    Description description, Dimension dimension, Weight weight, Language language) {

        _issn = null;
        _isbn = isbn;

        // An edition cannot have both ISSN and ISBN
        if (_isbn == null)
            throw new IllegalArgumentException("A Book with ISBN is required!");

        if (editionNumber == null || editionNumber <= 0)
            throw new IllegalArgumentException("Edition number needs to be positive");

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
    public Edition (ISSN issn, NumberOfPages numberOfPages, Integer editionNumber, LocalDate publicationDate, Binding binding,
                    Description description, Dimension dimension, Weight weight ,Language language) {

        _isbn = null;
        _issn = issn;

        // An edition cannot have both ISSN and ISBN
        if (_issn == null)
            throw new IllegalArgumentException("A magazine ISSN is required!");

        if (editionNumber == null || editionNumber <= 0)
            throw new IllegalArgumentException("Edition number needs to be positive");


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
    public Edition (NumberOfPages numberOfPages, Integer editionNumber, LocalDate publicationDate, Binding binding,
                    Description description, Dimension dimension, Weight weight ,Language language) {

        _isbn = null;
        _issn = null;

        if (editionNumber != null && editionNumber <= 0)
            throw new IllegalArgumentException("Edition number needs to be positive");


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
    public Integer getEditionNumber() { return _editionNumber; }
    public LocalDate getPublicationDate() { return _publicationDate; }
    public Binding getBinding() { return _binding; }
    public Description getDescription() { return _description; }
    public Dimension getDimension() { return _dimension; }
    public Weight getWeight() { return _weight; }
    public Language getLanguage() { return _language; }


}
