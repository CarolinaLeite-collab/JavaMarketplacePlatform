package TOPSECRET.domain;

import TOPSECRET.domain.edition.EditionBook;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Identifier;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.List;

public interface IPublicationRepo {

    Publication addPublication(PublicationType type,
                   Identifier identifier,
                   Year year,
                   Title title,
                   Author author,
                   PublishingCompany publisher,
                   EditionBook editionBook,
                   Genre genre);

    List<Publication> getDifferentOf(List<Publication> existentPublications);

    Publication getPublication(Publication publication);
}
