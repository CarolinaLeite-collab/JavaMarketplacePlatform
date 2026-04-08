package TOPSECRET.domain.edition;

import TOPSECRET.domain.valueobject.*;

import java.time.Year;

public class EditionFactory {

    public Edition createEditionBook(BookId bookId,
                                     PublicationId publicationId,
                                     PublishingCompanyId publishingCompanyId,
                                     Year publishingYear,
                                     Language editionLanguage,
                                     Dimension dimension,
                                     Weight weight,
                                     NumberOfPages numberOfPages,
                                     EditionNumber editionNumber,
                                     Binding binding) {

        if (publishingYear == null) {
            throw new IllegalArgumentException("PublishingYear cannot be null");
        }

        if (bookId instanceof NoIdBook && publishingYear.isAfter(Year.of(1970))) {
            throw new IllegalArgumentException(
                    "Books published after 1970 must have a valid ISBN"
            );
        }
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

    public Edition createEditionMagazine(MagazineId magazineId,
                                         PublicationId publication,
                                         PublishingCompanyId publishingCompany,
                                         Year publishingYear,
                                         Language editionLanguage,
                                         Dimension dimension,
                                         Weight weight,
                                         IssueNumber issueNumber,
                                         Periodicity periodicity) {

        if (publishingYear == null) {
            throw new IllegalArgumentException("PublishingYear cannot be null");
        }

        if (magazineId instanceof NoIdMagazine && publishingYear.isAfter(Year.of(1976))) {
            throw new IllegalArgumentException(
                    "Magazines published after 1976 must have a valid ISSN"
            );
        }

        EditionMagazine.Builder builder = new EditionMagazine.Builder(
                magazineId,
                publication,
                publishingCompany,
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
