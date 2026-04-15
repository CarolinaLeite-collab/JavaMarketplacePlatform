package TOPSECRET.persistence.mem;

import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.directsale.DirectSaleFactory;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;

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

public class MemoDirectSaleRepo implements IDirectSaleRepo {

    private final Map<DirectSaleId, DirectSale> DATA = new HashMap<DirectSaleId, DirectSale>();
    private final List<DirectSale> _directSales;
    private final DirectSaleFactory _factory;

    public MemoDirectSaleRepo(DirectSaleFactory factory) {
        _factory = factory;
        _directSales = new ArrayList<>();
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
    public DirectSale addDirectSale(List<ItemId> itemsId, Price price, Period timeLimit) {

        DirectSale directSale = _factory.createDirectSale(itemsId, price, timeLimit);
        _directSales.add(directSale);

        if (containsOfIdentity(directSale.identity())) {

            throw new IllegalStateException("Direct sale already exists!");

        }

        return save(directSale);

    }
}
