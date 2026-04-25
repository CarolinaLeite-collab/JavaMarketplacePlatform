package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

public interface IDirectSaleRepo extends IRepository<DirectSaleId, DirectSale> {

}
