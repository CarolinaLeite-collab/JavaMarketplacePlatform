package TOPSECRET.domain;

import java.util.List;
import java.time.Period;

public interface IDirectSaleRepo {

    DirectSale addDirectSale(Item item, Price price, Period timeLimit);
    List<Item> getDirectSaleItemsByAuthor(Author authorName);
    List<Item> getDirectSaleItemsByGenre(Genre genreName);
    List<Item> getDirectSaleItemsByPublication(Publication publication);
    List<Item> getDirectSaleItemsByPublisher(PublishingCompany publisher);


}