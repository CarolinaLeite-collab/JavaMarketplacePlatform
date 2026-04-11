package TOPSECRET.domain.edition;

import TOPSECRET.domain.valueobject.*;

import java.time.Year;

/**
 * Factory responsible for creating {@link Edition} aggregate instances,
 * namely {@link EditionBook} and {@link EditionMagazine}.
 *
 * <p>
 * - Mandatory attributes are enforced by the corresponding builders.
 * - Domain invariants (e.g., ISBN/ISSN rules) are enforced within the aggregate.
 * - Ensures that an Edition is created either as a Book or a Magazine,
 *   never both.
 * </p>
 */

public final class EditionFactory {

    public EditionBook createEditionBook(BookId bookId,
                                     PublicationId publicationId,
                                     PublishingCompanyId publishingCompanyId,
                                     Year publishingYear,
                                     Language editionLanguage,
                                     Dimension dimension,
                                     Weight weight,
                                     NumberOfPages numberOfPages,
                                     EditionNumber editionNumber,
                                     Binding binding) {

        EditionBook.Builder builder = new EditionBook.Builder(
                bookId,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage
        );

        if (dimension != null) builder.withDimension(dimension);
        if (weight != null) builder.withWeight(weight);
        if (numberOfPages != null) builder.withNumberOfPages(numberOfPages);
        if (editionNumber != null) builder.withEditionNumber(editionNumber);
        if (binding != null) builder.withBinding(binding);
        return builder.build();
    }

    public EditionMagazine createEditionMagazine(MagazineId magazineId,
                                         PublicationId publicationId,
                                         PublishingCompanyId publishingCompanyId,
                                         Year publishingYear,
                                         Language editionLanguage,
                                         Dimension dimension,
                                         Weight weight,
                                         IssueNumber issueNumber,
                                         Periodicity periodicity) {


        EditionMagazine.Builder builder = new EditionMagazine.Builder(
                magazineId,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage,
                issueNumber,
                periodicity
        );

        if (dimension != null) builder.withDimension(dimension);
        if (weight != null) builder.withWeight(weight);
        return builder.build();
    }

}
