package MITELOVERS.domain.sale;

import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleSaleStatus;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Factory responsible for creating {@link Sale} instances.
 */

@Component
public class SaleFactory {

    public Sale createSale(UserId buyerId,
                           List<SaleLine> saleLines) {

        return new Sale(buyerId, saleLines);
    }

    public Sale reconstituteSale(SaleId saleId,
                                 UserId buyerId,
                                 List<SaleLine> saleLines,
                                 LocalDateTime createdAt,
                                 LocalDateTime completedAt,
                                 SaleSaleStatus saleSaleStatus) {

        return new Sale(saleId, buyerId, saleLines, createdAt, completedAt, saleSaleStatus
        );
    }
}


