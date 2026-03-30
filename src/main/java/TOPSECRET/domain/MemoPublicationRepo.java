package TOPSECRET.domain;

import TOPSECRET.domain.edition.EditionBook;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Title;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link Publication} instances.
 * <p>
 * Provides methods to add new publications, check for duplicates, retrieve a specific publication,
 * and obtain publications that are not present in a given list.
 * Ensures that publications are not null and prevents adding duplicates.
 * </p>
 */

public class MemoPublicationRepo implements IPublicationRepo {

    private List<Publication> _publications;
    private final PublicationFactory _publicationFactory;

    public MemoPublicationRepo(PublicationFactory publicationFactory) {
        _publications = new ArrayList<>();
        _publicationFactory = publicationFactory;
    }

    @Override
    public Publication addPublication(PublicationType type,
                           Identifier identifier,
                           Year publicationYear,
                           Title title,
                           Author author,
                           PublishingCompany publisher,
                           EditionBook editionBook,
                           Genre genre) {

        Publication newPublication = _publicationFactory.createPublication(type, identifier, publicationYear, title, author, publisher, editionBook, genre);

        if (_publications.contains(newPublication)) {
            throw new IllegalArgumentException("Publication already exists in the repository");
        }
        _publications.add(newPublication);
        return newPublication;
    }

    @Override
    public List<Publication> getDifferentOf(List<Publication> existentPublications) {
        List<Publication> result = new ArrayList<>();
        for (Publication publication : _publications){
            if (!existentPublications.contains(publication)){
                result.add(publication);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public Publication getPublication(Publication publication) {
        return _publications.stream()
                .filter(p -> p.equals(publication))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));
    }
}
