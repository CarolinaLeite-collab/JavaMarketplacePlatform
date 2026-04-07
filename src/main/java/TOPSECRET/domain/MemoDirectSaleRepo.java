package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

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

    private final List<DirectSale> _directSales;
    private final DirectSaleFactory _factory;

    public MemoDirectSaleRepo(DirectSaleFactory factory) {
        _factory = factory;
        _directSales = new ArrayList<>();
    }

    @Override
    public DirectSale addDirectSale(Item item, Price price, Period timeLimit) {

        DirectSale directSale = _factory.createDirectSale(item, price, timeLimit);
        _directSales.add(directSale);

        return directSale;

    }

    @Override
    public List<Item> getDirectSaleItemsByAuthor(Author authorName) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByAuthor(authorName)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }

    @Override
    public List<Item> getDirectSaleItemsByGenre(Genre genreName) {

        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {

            if (directSale.isByGenre(genreName)) {

                list.add(directSale.getItem());

            }

        }

        return List.copyOf(list);

    }

    @Override
    public List<Item> getDirectSaleItemsByPublication(Publication publication) {
        List<Item> list = new ArrayList<>();

        for (DirectSale directSale : _directSales) {
            if (directSale.isByPublication(publication)) {
                list.add(directSale.getItem());
            }
        }
        return List.copyOf(list);
    }

    @Override
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
