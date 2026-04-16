package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.valueobject.*;

import java.time.Year;
import java.util.List;

public interface IEditionRepo extends IRepository<EditionId, Edition> {

    Edition addEdition(PublicationTypeId typeId,
                       Identifier identifier,
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

}
