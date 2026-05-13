package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.persistence.jpa.datamodel.PublicationDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
@AllArgsConstructor
public class PublicationAssembler {
    private final PublicationFactory _publicationFactory;

    public PublicationDataModel toDataModel(Publication publication) {
        PublicationId publicationId = publication.identity();
        Title title = publication.getTitle();
        AuthorId authorId = publication.getAuthorId();
        Year releaseYear = publication.getReleaseYear();
        GenreId genreId = publication.getGenreId();


        return new PublicationDataModel(publicationId.toString(), title.toString(), authorId.toString(), releaseYear.toString(), genreId.toString());
    }

    public Publication toDomain(PublicationDataModel publicationDataModel) {
        Title title = new Title(publicationDataModel.getTitle());
        AuthorId authorId = new AuthorId (publicationDataModel.getAuthorId());
        Year releaseYear = Year.parse(publicationDataModel.getReleaseYear());
        GenreId genreId = new GenreId(publicationDataModel.getGenreId());

        PublicationId publicationId = new PublicationId(title, authorId, releaseYear);

        Publication publication = _publicationFactory.createPublication(publicationId, title, authorId, releaseYear, genreId);

        return publication;
    }
}
