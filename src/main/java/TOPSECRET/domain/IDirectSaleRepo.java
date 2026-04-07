package TOPSECRET.domain;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

public interface IDirectSaleRepo {

    DirectSale addDirectSale(Item item, Price price, Period timeLimit);

    List<Item> getDirectSaleItemsByAuthor(AuthorId authorId);

    List<Item> getDirectSaleItemsByGenre(GenreId genreId);

    List<Item> getDirectSaleItemsByPublication(Publication publication);

    List<Item> getDirectSaleItemsByPublisher(PublishingCompany publisher);
}