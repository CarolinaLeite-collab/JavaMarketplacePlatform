package MITELOVERS.domain.sale;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link SaleLine} instances.
 */

@Component
public class SaleLineFactory {

    public SaleLine createSaleLine(UserId sellerId,
                                   Price priceAtSale,
                                   DirectSaleId directSaleId) {

        return new SaleLine(sellerId, priceAtSale, directSaleId);
    }

    public SaleLine createSale(SaleLineId saleLineId,
                               UserId sellerId,
                               Price priceAtSale,
                               DirectSaleId directSaleId) {

        return new SaleLine(saleLineId, sellerId, priceAtSale, directSaleId);
    }
}