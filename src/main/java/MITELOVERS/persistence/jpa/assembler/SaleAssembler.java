package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.SaleLineDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler responsible for converting between {@link Sale} domain objects
 * and {@link SaleDataModel} persistence objects.
 */
@Component
@AllArgsConstructor
public class SaleAssembler {

    private final SaleLineAssembler _saleLineAssembler;

    public SaleDataModel toDataModel(Sale sale) {

        SaleDataModel saleDataModel = new SaleDataModel(
                sale.identity().toString(),
                sale.get_buyerId().toString(),
                sale.get_saleSaleStatus(),
                sale.get_createdAt(),
                sale.get_completedAt(),
                new ArrayList<>()
        );

        List<SaleLineDataModel> saleLines = sale.get_saleLines()
                .stream()
                .map(saleLine -> _saleLineAssembler.toDataModel(saleLine, saleDataModel))
                .toList();

        saleDataModel.getSaleLines().addAll(saleLines);

        return saleDataModel;
    }
}