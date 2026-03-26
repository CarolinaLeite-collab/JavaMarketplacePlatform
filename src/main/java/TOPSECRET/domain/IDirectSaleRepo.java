package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

public interface IDirectSaleRepo {

    DirectSale addDirectSale(Item item, Price price, Period timeLimit);
    List<Item> getDirectSaleItemsByAuthor(Author authorName);
    List<Item> getDirectSaleItemsByGenre(Genre genreName);
    List<Item> getDirectSaleItemsByPublication(Publication publication);
    List<Item> getDirectSaleItemsByPublisher(PublishingCompany publisher);


}