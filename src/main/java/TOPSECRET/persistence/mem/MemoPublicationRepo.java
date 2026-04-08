package TOPSECRET.persistence.mem;


import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.publication.PublicationFactory;
import TOPSECRET.domain.repository.IPublicationRepo;
import TOPSECRET.domain.valueobject.*;

import java.time.Year;
import java.util.*;

/**
 * In-memory implementation of {@link IPublicationRepo}.
 *  * <p>
 *  * Stores {@link Publication} instances in a {@link HashMap} keyed by {@link PublicationId}.
 *  * Prevents duplicate publications based on {@link PublicationId} equality.
 *  * </p>
 */

public class MemoPublicationRepo implements IPublicationRepo {

    private Map<PublicationId, Publication> DATA = new HashMap<PublicationId, Publication>();
    private final PublicationFactory _publicationFactory;

    public MemoPublicationRepo(PublicationFactory publicationFactory) {
        _publicationFactory = publicationFactory;
    }

    @Override
    public Publication save(Publication publication){
        DATA.put(publication.identity(), publication);
        return publication;
    }

    @Override
    public Publication addPublication(Title title, AuthorId authorId, Year releaseYear, PublicationTypeId publicationTypeId, GenreId genreId) {
        PublicationId publicationId = new PublicationId(title, authorId, releaseYear);
        if (containsOfIdentity(publicationId)){
            throw new IllegalArgumentException("Publication already exists in the repository");
        }
        Publication newPublication = _publicationFactory.createPublication(title, authorId, releaseYear, publicationTypeId, genreId);

        return save(newPublication);
    }

    @Override
    public Optional<Publication> ofIdentity(PublicationId publicationId) {
        return Optional.ofNullable(DATA.get(publicationId));
    }

    @Override
    public boolean containsOfIdentity(PublicationId publicationId) {
        return DATA.containsKey(publicationId);
    }

    @Override
    public Iterable<Publication> findAll() {
        return List.copyOf(DATA.values());
    }

    @Override
    public List<Publication> getDifferentOf(List<Publication> existentPublications) {
        List<Publication> result = new ArrayList<>();
        for (Publication publication : DATA.values()){
            if (!existentPublications.contains(publication)){
                result.add(publication);
            }
        }
        return List.copyOf(result);
    }

    @Override
    public Publication getPublication(Publication publication) {
        return DATA.values().stream()
                .filter(p -> p.equals(publication))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));
    }
}
