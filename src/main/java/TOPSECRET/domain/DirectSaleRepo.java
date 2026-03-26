package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link DirectSale} instances.
 * <p>
 * Provides methods to create new direct sales and to retrieve items on direct sale by a specific {@link Author}.
 * </p>
 */

public class DirectSaleRepo {

    private final List<DirectSale> _directSales;
    private final DirectSaleFactory _factory;

    public DirectSaleRepo(DirectSaleFactory factory) {
        _factory = factory;
        _directSales = new ArrayList<>();
    }

    public DirectSale addDirectSale(Item item, Price price, Period timeLimit) {
        DirectSale directSale = _factory.createDirectSale(item, price, timeLimit);
        _directSales.add(directSale);
        return directSale;
    }

    public List<Item> getDirectSaleItemsByAuthor(Author authorName) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByAuthor(authorName)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }

    public List<Item> getDirectSaleItemsByGenre(Genre genreName) {

        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {

            if (directSale.isByGenre(genreName)) {

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

    public List<Item> getDirectSaleItemsByPublisher(PublishingCompany publisher) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByPublisher(publisher)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }
}