package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.List;

public interface IPublicationRepo extends IRepository<PublicationId, Publication> {

    Publication addPublication(Title title, AuthorId authorId, Year releaseYear, GenreId genreId);

    List<Publication> getDifferentOf(List<Publication> existentPublications);

}
