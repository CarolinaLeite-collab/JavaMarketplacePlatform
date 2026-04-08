package TOPSECRET.domain.edition;

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

public interface Edition {
    EditionId getId();
    PublicationId getPublication();
    PublishingCompanyId getPublishingCompany();
    Year getPublishingYear();
    Language getEditionLanguage();
}
