package MITELOVERS.persistence.mem;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;

import java.time.Period;
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

}
