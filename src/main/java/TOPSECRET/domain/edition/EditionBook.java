package TOPSECRET.domain.edition;

import TOPSECRET.domain.valueobject.*;

import java.time.Year;

/**
 * Represents a book edition, a concrete implementation of {@link Edition}.
 * <p>
 * An {@code EditionBook} models a specific publication instance of type book,
 * including its identity, publishing metadata, and optional physical characteristics.
 * </p>
 */

public final class EditionBook implements Edition {

    private final BookId _bookId;
    private final PublicationId _publicationId;
    private final PublishingCompanyId _publishingCompanyId;
    private final Year _publishingYear;
    private final Language _editionLanguage;
    // opcionais
    private final Dimension _dimension;
    private final Weight _weight;
    private final NumberOfPages _numberOfPages;
    private final EditionNumber _editionNumber;
    private final Binding _binding;

    private EditionBook(Builder builder) {
        //deals with NoIsbnBooks
        BookId bookIdPlaceholder = builder._bookId;

        if (bookIdPlaceholder == null) {
            bookIdPlaceholder = NoIsbnBook.generate();
        }

        if (bookIdPlaceholder instanceof NoIsbnBook && builder._publishingYear.isAfter(Year.of(1970))) {
            throw new IllegalArgumentException("Books published after 1970 must have a valid ISBN.");
        }
        _bookId = bookIdPlaceholder;
        //
        _publicationId = builder._publicationId;
        _publishingCompanyId = builder._publishingCompanyId;
        _publishingYear = builder._publishingYear;
        _editionLanguage = builder._editionLanguage;
        _dimension = builder._dimension;
        _weight = builder._weight;
        _numberOfPages = builder._numberOfPages;
        _editionNumber = builder._editionNumber;
        _binding = builder._binding;
    }

    public static class Builder {
        private final BookId _bookId;
        private final PublicationId _publicationId;
        private final PublishingCompanyId _publishingCompanyId;
        private final Year _publishingYear;
        private final Language _editionLanguage;
        // opcionais
        private Dimension _dimension;
        private Weight _weight;
        private NumberOfPages _numberOfPages;
        private EditionNumber _editionNumber;
        private Binding _binding;

        public Builder(BookId bookId,
                       PublicationId publicationId,
                       PublishingCompanyId publishingCompanyId,
                       Year publishingYear,
                       Language editionLanguage) {
            _bookId = bookId;
            _publicationId = publicationId;
            _publishingCompanyId = publishingCompanyId;
            _publishingYear = publishingYear;
            _editionLanguage = editionLanguage;
        }

        public EditionBook build() {

            if (_publicationId == null)
                throw new IllegalArgumentException("PublicationId is required");

            if (_publishingCompanyId == null)
                throw new IllegalArgumentException("PublishingCompanyId is required");

            if (_publishingYear == null)
                throw new IllegalArgumentException("PublishingYear is required");

            if (_editionLanguage == null)
                throw new IllegalArgumentException("Language is required");

            return new EditionBook(this);
        }

        public Builder withDimension(Dimension dimension) {
            _dimension = dimension;
            return this;
        }

        public Builder withWeight(Weight weight) {
            _weight = weight;
            return this;
        }

        public Builder withNumberOfPages(NumberOfPages numberOfPages) {
            _numberOfPages = numberOfPages;
            return this;
        }

        public Builder withEditionNumber(EditionNumber editionNumber) {
            _editionNumber = editionNumber;
            return this;
        }

        public Builder withBinding(Binding binding) {
            _binding = binding;
            return this;
        }
    }

    //methods from DomainEntity contract
    @Override
    public EditionId identity() {
        return _bookId;
    }

    //Identity-based equality
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object instanceof EditionBook) {
            EditionBook otherEditionBook = (EditionBook) object;
            return _bookId.equals(otherEditionBook._bookId);
        }
        return false;
    }

    //Field-base equality
    @Override
    public boolean sameAs(Object object) {
        if (object instanceof EditionBook) {
            EditionBook otherEditionBook = (EditionBook) object;

            if (_bookId instanceof ISBN) {
                return _bookId.equals(otherEditionBook._bookId);
            }

            if (_publicationId.equals(otherEditionBook._publicationId) &&
                    _publishingCompanyId.equals(otherEditionBook._publishingCompanyId) &&
                    _publishingYear.equals(otherEditionBook._publishingYear) &&
                    _editionLanguage.equals(otherEditionBook._editionLanguage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Id: " + identity().toString() +
                "\nPublication: " + _publicationId +
                "\nPublishing Company: " + _publishingCompanyId +
                "\nYear: " + _publishingYear +
                "\nLanguage: " + _editionLanguage +
                (_dimension != null ? "\nDimension: " + _dimension : "") +
                (_weight != null ? "\nWeight: " + _weight : "") +
                (_numberOfPages != null ? "\nNumber of pages: " + _numberOfPages : "") +
                (_editionNumber != null ? "\nEdition number: " + _editionNumber : "") +
                (_binding != null ? "\nBinding: " + _binding : "");
    }

// Methods from Edition contract
@Override
public PublicationId getPublicationId() {
    return _publicationId;
}

@Override
public PublishingCompanyId getPublishingCompanyId() {
    return _publishingCompanyId;
}

@Override
public Year getPublishingYear() {
    return _publishingYear;
}

@Override
public Language getEditionLanguage() {
    return _editionLanguage;
}

public NumberOfPages getNumberOfPages() {
    return _numberOfPages;
}

public EditionNumber getEditionNumber() {
    return _editionNumber;
}

public Binding getBinding() {
    return _binding;
}

public Dimension getDimension() {
    return _dimension;
}

public Weight getWeight() {
    return _weight;
}

}
