package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.directsale.DirectSaleFactory;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.GenreId;
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
    public DirectSale addDirectSale(List<Item> items, Price price, Period timeLimit) {

        DirectSale directSale = _factory.createDirectSale(items, price, timeLimit);
        _directSales.add(directSale);

        return save(directSale);

    }


    @Override
    public List<Item> getDirectSaleItemsByAuthor(AuthorId authorId) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByAuthor(authorId)) {
                list.addAll(directSale.getItems());
            }
        }

        return List.copyOf(list);
    }

    @Override
    public List<Item> getDirectSaleItemsByGenre(GenreId genreId) {

        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByGenre(genreId)) {
                list.addAll(directSale.getItems());
            }
        }

        return List.copyOf(list);

    }

    @Override
    public List<Item> getDirectSaleItemsByPublication(Publication publication) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByPublication(publication)) {
                list.addAll(directSale.getItems());
            }
        }

        return List.copyOf(list);
    }

    @Override
    public List<Item> getDirectSaleItemsByPublisher(PublishingCompany publisher) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByPublishingCompany(publisher)) {
                list.addAll(directSale.getItems());
            }
        }

        return List.copyOf(list);
    }

}
