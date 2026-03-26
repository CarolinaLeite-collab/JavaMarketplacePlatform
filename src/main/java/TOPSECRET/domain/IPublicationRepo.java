package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
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
                   Edition edition,
                   Genre genre);

    List<Publication> getDifferentOf(List<Publication> existentPublications);

    Publication getPublication(Publication publication);
}
