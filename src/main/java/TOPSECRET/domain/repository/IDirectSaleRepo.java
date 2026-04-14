package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.*;

import java.time.Period;
import java.util.List;

public interface IDirectSaleRepo extends IRepository<DirectSaleId, DirectSale> {

    DirectSale addDirectSale(List<Item> items, Price price, Period timeLimit);

    List<Item> getDirectSaleItemsByAuthor(AuthorId authorId);

    List<Item> getDirectSaleItemsByGenre(GenreId genreId);

    List<Item> getDirectSaleItemsByPublication(Publication publication);

    List<Item> getDirectSaleItemsByPublisher(PublishingCompanyId publisherId);
}