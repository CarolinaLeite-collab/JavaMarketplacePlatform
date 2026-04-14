package TOPSECRET.domain.edition;

import TOPSECRET.domain.valueobject.*;

import java.time.Year;

/**
 * Factory responsible for creating {@link Edition} aggregate instances.
 * <p>
 * This factory centralizes the creation of {@code Edition} objects while
 * delegating validation of domain invariants to the aggregate itself.
 */

public final class EditionFactory {
    public Edition createEdition(PublicationTypeId typeId,
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

        return new Edition(
                typeId,
                identifier,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage,
                dimension,
                weight,
                numberOfPages,
                editionNumber,
                binding
        );
    }
}
