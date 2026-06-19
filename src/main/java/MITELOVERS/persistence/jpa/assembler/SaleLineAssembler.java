package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.sale.SaleLineFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.SaleLineDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link SaleLine} domain objects
 * and {@link SaleLineDataModel} persistence representations.
 */
@Component
@AllArgsConstructor
public class SaleLineAssembler {

    private final SaleLineFactory _factory;

    public SaleLineDataModel toDataModel(SaleLine saleLine, SaleDataModel saleDataModel) {
        return new SaleLineDataModel(
                saleLine.identity().toString(),
                saleLine.get_sellerId().toString(),
                saleLine.get_directSaleId().toString(),
                new PriceDataModel(
                        saleLine.get_priceAtSale().getValue(),
                        saleLine.get_priceAtSale().getCurrency().toString()
                ),
                saleDataModel
        );
    }

    public SaleLine toDomain(SaleLineDataModel saleLineDataModel) {
        return _factory.reconstituteSaleLine(
                new SaleLineId(saleLineDataModel.getSaleLineId()),
                new UserId(new Email(saleLineDataModel.getSellerId())),
                new Price(
                        saleLineDataModel.getPrice().getNumericValue(),
                        Currency.valueOf(saleLineDataModel.getPrice().getCurrency())
                ),
                new DirectSaleId(saleLineDataModel.getDirectSaleId())
        );
    }
}