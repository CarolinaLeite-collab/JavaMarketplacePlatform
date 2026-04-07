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

public class EditionBook implements Edition {

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

    protected EditionBook(Builder builder) {
        _bookId = builder._bookId;
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
            if (_bookId == null)
                throw new IllegalArgumentException("BookId is required");

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
        public Builder withWeight(Weight weight){
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

    @Override
    public EditionId getId() {
        return _bookId;
    }

    @Override
    public PublicationId getPublication() {
        return _publicationId;
    }

    @Override
    public PublishingCompanyId getPublishingCompany() {
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
