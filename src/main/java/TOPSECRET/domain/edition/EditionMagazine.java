package TOPSECRET.domain.edition;

import TOPSECRET.domain.valueobject.*;

import java.time.Year;

/**
 * Represents a magazine edition, a concrete implementation of {@link Edition}.
 * <p>
 * An {@code EditionMagazine} models a specific publication instance of type magazine,
 * including its identity, publishing metadata, and periodicity-related attributes.
 * </p>
 */

public final class EditionMagazine implements Edition {

    private final MagazineId _magazineId;
    private final PublicationId _publicationId;
    private final PublishingCompanyId _publishingCompanyId;
    private final Year _publishingYear;
    private final Language _editionLanguage;
    private final IssueNumber _issueNumber;
    private final Periodicity _periodicity;
    //optional
    private final Dimension _dimension;
    private final Weight _weight;


    private EditionMagazine(Builder builder){
        //Deals with NoIssnMagazines
        MagazineId magazineIdPlaceholder = builder._magazineId;

        if (magazineIdPlaceholder == null) {
            magazineIdPlaceholder = NoIssnMagazine.generate();
        }

        if (magazineIdPlaceholder instanceof NoIssnMagazine && builder._publishingYear.isAfter(Year.of(1976))) {
            throw new IllegalArgumentException(
                    "Magazines published after 1976 must have a valid ISSN"
            );
        }
        _magazineId = magazineIdPlaceholder;
        //
        _publicationId = builder._publicationId;
        _publishingCompanyId = builder._publishingCompanyId;
        _publishingYear = builder._publishingYear;
        _editionLanguage = builder._editionLanguage;
        _dimension = builder._dimension;
        _weight = builder._weight;
        _issueNumber = builder._issueNumber;
        _periodicity = builder._periodicity;
    }

    public static class Builder {
        private final MagazineId _magazineId;
        private final PublicationId _publicationId;
        private final PublishingCompanyId _publishingCompanyId;
        private final Year _publishingYear;
        private final Language _editionLanguage;
        private final IssueNumber _issueNumber;
        private final Periodicity _periodicity;

        // opcionais
        private Dimension _dimension;
        private Weight _weight;

        public Builder(MagazineId magazineIdId,
                       PublicationId publicationId,
                       PublishingCompanyId publishingCompanyId,
                       Year publishingYear,
                       Language editionLanguage,
                       IssueNumber issueNumber,
                       Periodicity periodicity) {

            _magazineId = magazineIdId;
            _publicationId = publicationId;
            _publishingCompanyId = publishingCompanyId;
            _publishingYear = publishingYear;
            _editionLanguage = editionLanguage;
            _issueNumber = issueNumber;
            _periodicity = periodicity;
        }

        public EditionMagazine build() {

            if (_publicationId == null)
                throw new IllegalArgumentException("PublicationId is required");

            if (_publishingCompanyId == null)
                throw new IllegalArgumentException("PublishingCompanyId is required");

            if (_publishingYear == null)
                throw new IllegalArgumentException("PublishingYear is required");

            if (_editionLanguage == null)
                throw new IllegalArgumentException("Language is required");

            if (_issueNumber == null)
                throw new IllegalArgumentException("IssueNumber is required");

            if (_periodicity == null)
                throw new IllegalArgumentException("Periodicity is required");

            return new EditionMagazine(this);
        }

        public Builder withDimension(Dimension dimension) {
            _dimension = dimension;
            return this;
        }
        public Builder withWeight(Weight weight){
            _weight = weight;
            return this;
        }

    }

    //methods from DomainEntity contract
    @Override
    public EditionId identity() {
        return _magazineId;
    }

    //Identity-based equality
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if(object instanceof EditionMagazine) {
            EditionMagazine otherEditionMagazine = (EditionMagazine) object;
            return _magazineId.equals(otherEditionMagazine._magazineId);
        }
        return false;
    }

    //Field-base equality
    @Override
    public boolean sameAs(Object object){
        if (object instanceof EditionMagazine) {
            EditionMagazine otherEditionMagazine = (EditionMagazine) object;

            if (_magazineId instanceof ISSN) {
                return _magazineId.equals(otherEditionMagazine._magazineId);
            }

            if (_publicationId.equals(otherEditionMagazine._publicationId) &&
                    _publishingCompanyId.equals(otherEditionMagazine._publishingCompanyId) &&
                    _publishingYear.equals(otherEditionMagazine._publishingYear) &&
                    _editionLanguage.equals(otherEditionMagazine._editionLanguage) &&
                    _issueNumber.equals(otherEditionMagazine._issueNumber) &&
                    _periodicity.equals(otherEditionMagazine._periodicity)
            ){
                return true;
            }
        }
        return false;    }

    @Override
    public String toString() {
        return "Id: " + identity().toString() +
                "\nPublication: " + _publicationId +
                "\nPublishing Company: " + _publishingCompanyId +
                "\nYear: " + _publishingYear +
                "\nLanguage: " + _editionLanguage +
                "\nIssue Number: " + _issueNumber +
                "\nPeriodicity: " + _periodicity +
                (_dimension != null ? "\nDimension: " + _dimension : "") +
                (_weight != null ? "\nWeight: " + _weight : "");
    }

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

    public Weight getWeight() {
        return _weight;
    }

    public Dimension getDimension() {
        return _dimension;
    }

    public IssueNumber getIssueNumber() {
        return _issueNumber;
    }

    public Periodicity getPeriodicity() {
        return _periodicity;
    }

}
