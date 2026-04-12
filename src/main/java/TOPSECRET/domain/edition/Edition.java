package TOPSECRET.domain.edition;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.*;

import java.time.Year;

/**
 * Represents a generic publication edition within the domain.
 * <p>
 * An {@code Edition} defines the common contract for all types of editions,
 * such as books and magazines, including identity, publication reference,
 * publishing company, publishing year, and language.
 * </p>
 */

public sealed interface Edition extends AggregateRoot<EditionId>
        permits EditionBook, EditionMagazine {

    PublicationId getPublicationId();

    PublishingCompanyId getPublishingCompanyId();

    Year getPublishingYear();

    Language getEditionLanguage();

}
