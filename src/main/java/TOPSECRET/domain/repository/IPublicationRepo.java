package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.*;

import java.time.Year;
import java.util.List;

public interface IPublicationRepo extends IRepository<PublicationId, Publication> {

    Publication addPublication(Title title, AuthorId authorId, Year releaseYear, PublicationTypeId publicationTypeId, GenreId genreId);

    List<Publication> getDifferentOf(List<Publication> existentPublications);

    Publication getPublication(Publication publication);
}
