package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

public interface IDirectSaleRepo extends IRepository<DirectSaleId, DirectSale> {

    DirectSale addDirectSale(List<ItemId> itemsId, Price price, Period timeLimit);

}