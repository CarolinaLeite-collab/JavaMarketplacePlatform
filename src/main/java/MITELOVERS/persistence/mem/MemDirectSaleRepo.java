package MITELOVERS.persistence.mem;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

/**
 * Repository responsible for managing {@link DirectSale} entities.
 * <p>
 * This class provides management mechanisms for
 * {@link DirectSale} objects.
 * <p>
 * It encapsulates all data access
 * operations related to libraries and isolates the domain and controller
 * layers from persistence concerns.
 * </p>
 */


@Repository
@Profile("mem")
public class MemDirectSaleRepo implements IDirectSaleRepo {

    private final Map<DirectSaleId, DirectSale> DATA = new HashMap<DirectSaleId, DirectSale>();

    @Override
    public List<DirectSaleId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }
    
    @Override
    public DirectSale save(DirectSale directSale) {

        DATA.put(directSale.identity(), directSale);

        return directSale;
    }

    @Override
    public Iterable<DirectSale> findAll() {

        return DATA.values();
    }

    @Override
    public Optional<DirectSale> ofIdentity(DirectSaleId id) {
        if(!containsOfIdentity(id)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(id));

        }
    }

    @Override
    public boolean containsOfIdentity(DirectSaleId id) {
        return DATA.containsKey(id);
    }

    @Override
    public List<ItemId> findDirectSaleItemsByAuthorIdSortedByDescription(AuthorId authorId){
        return new ArrayList<>();
    }

    @Override
    public List<DirectSale> findByItemsIdSortedByPublicationDateAsc(List<ItemId> itemIds) {

        return DATA.values().stream()
                .filter(ds -> ds.getItemsId().stream().anyMatch(itemIds::contains))
                .sorted(Comparator.comparing(DirectSale::getCreationDate))
                .toList();
    }

    @Override
    public List<DirectSale> findByItemsIdSortedByPublicationDateDesc(List<ItemId> itemIds) {

        return DATA.values().stream()
                .filter(ds -> ds.getItemsId().stream().anyMatch(itemIds::contains))
                .sorted(Comparator.comparing(DirectSale::getCreationDate).reversed())
                .toList();
    }

    @Override
    public void deleteDirectSale(DirectSaleId directSaleId) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DirectSaleId> findExpired() {

        Instant now = Instant.now();

        return DATA.values().stream()
                .filter(ds -> ds.getTimeLimit() != null)   // only sales with a time limit
                .filter(ds -> ds.getCreationDate()
                        .plus(ds.getTimeLimit())             // creationDate + timeLimit
                        .isBefore(now))
                .map(DirectSale::identity)
                .toList();
    }

}
