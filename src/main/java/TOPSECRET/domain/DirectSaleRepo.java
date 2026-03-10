package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;
import java.time.Period;

/**
 * Repository for managing {@link DirectSale} instances.
 * <p>
 * Provides methods to create new direct sales and to retrieve items on direct sale by a specific {@link Author}.
 * </p>
 */

public class DirectSaleRepo {

    private final List<DirectSale> _directSales;
    private final DirectSaleFactory _factory;

    public DirectSaleRepo() {
        this(new DirectSaleFactory());
    }

    public DirectSaleRepo(DirectSaleFactory factory) {
        if (factory == null) throw new IllegalArgumentException("Factory cannot be null");
        _factory = factory;
        _directSales = new ArrayList<>();
    }

    public DirectSale createDirectSale(Item item, Price price, Period timeLimit) {
        DirectSale directSale = _factory.createDirectSale(item, price, timeLimit);
        _directSales.add(directSale);
        return directSale;
    }

    public List<Item> getDirectSaleItemsByAuthor(Author author) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByAuthor(author)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }

    public List<Item> getDirectSaleItemsByGenre(Genre genre) {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < _directSales.size(); i++) {
            DirectSale directSale = _directSales.get(i);
            Publication publication = directSale.getItem().getPublication();

            if (publication.matchGenre(genre)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }

    public List<Item> getDirectSaleItemsByPublication(Publication publication) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByPublication(publication)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }

    public List<Item> getDirectSaleItemByPublisher(PublishingCompany publisher) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByPublisher(publisher)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }
}