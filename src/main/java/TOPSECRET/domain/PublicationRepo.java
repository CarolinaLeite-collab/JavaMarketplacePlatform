package TOPSECRET.domain;

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

public class PublicationRepo {

    private List<Publication> _publications;
    private final PublicationFactory _publicationFactory;

    public PublicationRepo(PublicationFactory publicationFactory) {
        _publications = new ArrayList<>();
        _publicationFactory = publicationFactory;
    }

    public Publication addPublication(PublicationType type,
                           Identifier identifier,
                           Year year,
                           Title title,
                           Author author,
                           PublishingCompany publisher,
                           Edition edition,
                           Genre genre) {

        Publication newPublication = _publicationFactory.createPublication(type, identifier, year, title, author, publisher, edition, genre);

        if (_publications.contains(newPublication)) {  // Replaces the "publicationAlreadyExists" method
            throw new IllegalArgumentException("Publication already exists in the repository");
        }
        _publications.add(newPublication);
        return newPublication;
    }

    //check publications that are still out of the library
    public List<Publication> getDifferentOf(List<Publication> existentPublications) {
        List<Publication> result = new ArrayList<>();
        for (Publication publication : _publications){
            if (!existentPublications.contains(publication)){
                result.add(publication);
            }
        }
        return List.copyOf(result);
    }

    public Publication getPublication(Publication publication) {
        return _publications.stream()
                .filter(p -> p.equals(publication))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));
    }
}
