package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.*;

import java.time.Year;

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
