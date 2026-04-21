package MITELOVERS.domain.edition;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.*;

import java.time.Year;
import java.util.Objects;

/**
 * Represents a specific released version of a {@link PublicationId},
 * such as a book or magazine edition.
 * <p>
 * An {@code Edition} is an Aggregate Root identified by a generated {@link EditionId}.
 * It captures the publishing metadata of a release, including the {@link PublicationId}
 * it refers to, the {@link PublishingCompanyId} responsible for the release,
 * the {@link PublicationTypeId}, the publishing {@link Year}, and the {@link Language}.
 * </p>
 * <p>
 * An {@code Edition} may also carry optional physical characteristics such as
 * {@link Dimension}, {@link Weight}, {@link NumberOfPages}, {@link EditionNumber},
 * and {@link Binding}.
 * </p>
 */

public class Edition implements AggregateRoot<EditionId> {

    private final PublicationTypeId _typeId;
    private final Identifier _identifier;
    private final PublicationId _publicationId;
    private final PublishingCompanyId _publishingCompanyId;
    private final Year _publishingYear;
    private final Language _editionLanguage;
    private final EditionId _generatedId;
    // optional fields
    private final Dimension _dimension;
    private final Weight _weight;
    private final NumberOfPages _numberOfPages;
    private final EditionNumber _editionNumber;
    private final Binding _binding;

    Edition(PublicationTypeId typeId,
            Identifier identifier,
            PublicationId publicationId,
            PublishingCompanyId publishingCompanyId,
            Year publishingYear,
            Language editionLanguage,
            Dimension dimension,
            Weight weight,
            NumberOfPages numberOfPages,
            EditionNumber editionNumber,
            Binding binding) {

        if (identifier == null)
            throw new IllegalArgumentException("Identifier is required");

        if (typeId == null)
            throw new IllegalArgumentException("Publication Type Id is required");

        if (publicationId == null)
            throw new IllegalArgumentException("Publication Id is required");

        if (publishingCompanyId == null)
            throw new IllegalArgumentException("Publishing Company is required");

        if (publishingYear == null)
            throw new IllegalArgumentException("Publishing Year is required");

        if (editionLanguage == null)
            throw new IllegalArgumentException("Language is required");

        if (!isValidIdentifier(typeId, identifier, publishingYear))
            throw new IllegalArgumentException("Invalid identifier for given type and year");

        _typeId = typeId;
        _identifier = identifier;
        _publicationId = publicationId;
        _publishingCompanyId = publishingCompanyId;
        _publishingYear = publishingYear;
        _editionLanguage = editionLanguage;
        _dimension = dimension;
        _weight = weight;
        _numberOfPages = numberOfPages;
        _editionNumber = editionNumber;
        _binding = binding;
        _generatedId = new EditionId();
    }

    //Constructor without optional fields
    Edition(PublicationTypeId typeId,
            Identifier identifier,
            PublicationId publicationId,
            PublishingCompanyId publishingCompanyId,
            Year publishingYear,
            Language editionLanguage) {

        this(typeId,
                identifier,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage,
                null, null, null, null, null);
    }

    //methods from DomainEntity contract
    @Override
    public EditionId identity() {
        return _generatedId;
    }

    //Identity-based equality
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object instanceof Edition) {
            Edition otherEdition = (Edition) object;
            return _generatedId.equals(otherEdition._generatedId);
        }
        return false;
    }

    //Field-base equality
    @Override
    public boolean sameAs(Object object) {
        if (object instanceof Edition) {
            Edition otherEdition = (Edition) object;

            // Only compare identifier if same publication type and none are Type NoIdentifier
            if (_typeId.equals(otherEdition._typeId) &&
                    !(_identifier instanceof NoIdentifier) &&
                    !(otherEdition._identifier instanceof NoIdentifier)) {
                return _identifier.equals(otherEdition._identifier);
            }

            if (_typeId.equals(otherEdition._typeId) &&
                    _publicationId.equals(otherEdition._publicationId) &&
                    _publishingCompanyId.equals(otherEdition._publishingCompanyId) &&
                    _publishingYear.equals(otherEdition._publishingYear) &&
                    _editionLanguage.equals(otherEdition._editionLanguage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Id: " + identity() +
                "\nType: " + _typeId +
                "\nIdentifier: " + _identifier +
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

    private boolean isValidIdentifier(PublicationTypeId typeId, Identifier identifier, Year year) {

        if (typeId.isBook()) {
            if (year.isAfter(Year.of(1970))) {
                return identifier instanceof ISBN;
            }
            return identifier instanceof NoIdentifier;
        }

        if (typeId.isMagazine()) {
            if (year.isAfter(Year.of(1976))) {
                return identifier instanceof ISSN;
            }
            return identifier instanceof NoIdentifier;
        }

        return false;
    }

    public PublicationTypeId getPublicationTypeId() {
        return _typeId;
    }

    public Identifier getIdentifier() {
        return _identifier;
    }

    public PublicationId getPublicationId() {
        return _publicationId;
    }

    public PublishingCompanyId getPublishingCompanyId() {
        return _publishingCompanyId;
    }

    public Year getPublishingYear() {
        return _publishingYear;
    }

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

    public boolean isByPublishingCompanyId(PublishingCompanyId publishingCompanyId) {
        return Objects.equals(_publishingCompanyId, publishingCompanyId);
    }

    public boolean isByPublicationId(PublicationId publicationId) {
        return Objects.equals(_publicationId, publicationId);
    }
}
