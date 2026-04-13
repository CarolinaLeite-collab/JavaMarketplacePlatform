package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.valueobject.*;

import java.time.Year;

public interface IEditionRepo extends IRepository<EditionId, Edition> {

    Edition addEditionBook(BookId bookId,
                                  PublicationId publicationId,
                                  PublishingCompanyId publishingCompanyId,
                                  Year publishingYear,
                                  Language editionLanguage,
                                  Dimension dimension,
                                  Weight weight,
                                  NumberOfPages numberOfPages,
                                  EditionNumber editionNumber,
                                  Binding binding
    );

    Edition addEditionMagazine(MagazineId magazineId,
                               PublicationId publicationId,
                               PublishingCompanyId publishingCompanyId,
                               Year publishingYear,
                               Language editionLanguage,
                               Dimension dimension,
                               Weight weight,
                               IssueNumber issueNumber,
                               Periodicity periodicity
                               );

}
