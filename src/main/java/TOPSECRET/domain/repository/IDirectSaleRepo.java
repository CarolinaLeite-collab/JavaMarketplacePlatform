package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

public interface IDirectSaleRepo extends IRepository<DirectSaleId, DirectSale> {

    DirectSale addDirectSale(List<Item> items, Price price, Period timeLimit);

    List<Item> getDirectSaleItemsByAuthor(AuthorId authorId);

    List<Item> getDirectSaleItemsByGenre(GenreId genreId);

    List<Item> getDirectSaleItemsByPublication(Publication publication);

    List<Item> getDirectSaleItemsByPublisher(PublishingCompany publisher);
}