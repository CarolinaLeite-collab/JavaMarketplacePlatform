package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.List;

public interface IPublicationRepo {

    Publication addPublication(Title title, Author author, Year releaseYear, PublicationType publicationType, Genre genre);

    List<Publication> getDifferentOf(List<Publication> existentPublications);

    Publication getPublication(Publication publication);
}
