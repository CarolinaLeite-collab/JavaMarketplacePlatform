package MITELOVERS.persistence.mem;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory implementation of {@link IItemRepo}.
 * <p>
 *   This repository is responsible for persisting {@link Item} instances
 *   during runtime, using a {@link HashMap} as storage, with the key being {@link ItemId}.
 *  </p>
 *  <p>
 *   It provides basic CRUD-like operations such as saving, retrieving by identity,
 *   checking existence, and listing all stored entities.
 *  </p>
 */

@Repository
@Profile("mem")

public class MemItemRepo implements IItemRepo {

    private final Map<ItemId, Item> DATA = new HashMap<ItemId, Item>();
    private final IEditionRepo _iEditionRepo;
    private final IPublicationRepo _iPublicationRepo;

    public MemItemRepo(IEditionRepo iEditionRepo,
                       IPublicationRepo iPublicationRepo) {

        _iEditionRepo = iEditionRepo;
        _iPublicationRepo = iPublicationRepo;
    }

    @Override
    public Item save (Item itemSaved){
        DATA.put(itemSaved.identity(), itemSaved);
        return itemSaved;
    }

    @Override
    public Iterable<Item> findAll(){
        return DATA.values();
    }

    @Override
    public Optional<Item> ofIdentity(ItemId id){
        if (!containsOfIdentity(id)){

            return Optional.empty();
        }
        return Optional.of(DATA.get(id));
    }

    @Override
    public boolean containsOfIdentity(ItemId id){
        return DATA.containsKey(id);
    }

    @Override
    public List<ItemId> findAllKeys(){

        return new ArrayList<>(DATA.keySet());

    }

    @Override
    public List<Item> findByIdInOrderByDescriptionAsc(Collection<String> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ItemId> findByGenreId(GenreId genreId) {

        List<ItemId> result = new ArrayList<>();

        for (Item item : DATA.values()) {

            Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                    .orElseThrow(() -> new IllegalStateException("Edition not found: " + item.getEditionId()));

            Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                    .orElseThrow(() -> new IllegalStateException("Publication not found: " + edition.getPublicationId()));

            if (publication.isByGenreId(genreId)) {
                result.add(item.identity());
            }
        }

        return result;
    }

}
