package MITELOVERS.persistence.mem;


import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;

import java.time.Year;
import java.util.*;

/**
 * In-memory implementation of {@link IPublicationRepo}.
 *  * <p>
 *  * Stores {@link Publication} instances in a {@link HashMap} keyed by {@link PublicationId}.
 *  * Prevents duplicate publications based on {@link PublicationId} equality.
 *  * </p>
 */

public class MemPublicationRepo implements IPublicationRepo {

    private final Map<PublicationId, Publication> DATA = new HashMap<PublicationId, Publication>();
    private final PublicationFactory _publicationFactory;

    public MemPublicationRepo(PublicationFactory publicationFactory) {
        _publicationFactory = publicationFactory;
    }

    @Override
    public Publication save(Publication publication){
        DATA.put(publication.identity(), publication);
        return publication;
    }

    @Override
    public Publication addPublication(Title title, AuthorId authorId, Year releaseYear, GenreId genreId) {

        Publication newPublication = _publicationFactory.createPublication(title, authorId, releaseYear, genreId);

        if (containsOfIdentity(newPublication.identity())){
            throw new IllegalArgumentException("Publication already exists in the repository");
        }
        return save(newPublication);
    }

    @Override
    public Optional<Publication> ofIdentity(PublicationId publicationId) {
        if (!containsOfIdentity(publicationId)) {
            return Optional.empty();
        } else {
            return Optional.of(DATA.get(publicationId));
        }
    }

    @Override
    public boolean containsOfIdentity(PublicationId publicationId) {
        return DATA.containsKey(publicationId);
    }

    @Override
    public Iterable<Publication> findAll() {
        return DATA.values();
    }

    @Override
    public List<PublicationId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());
    }


    // TODO: move to Service layer
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
}
