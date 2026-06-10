package MITELOVERS.domain.directsale;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Factory responsible for creating {@link DirectSale } instances.
 */

@Component
public class DirectSaleFactory {

    public DirectSale createDirectSale(List<ItemId> itemsId, UserId sellerId, Price price, Duration timeLimit) {

        DirectSale newDirectSale = new DirectSale(itemsId, sellerId, price, timeLimit);

        return newDirectSale;
    }

    public DirectSale createDirectSale(DirectSaleId directSaleId, List<ItemId> itemsId, UserId sellerId, Price price, Duration timeLimit, Instant creationDate) {

        DirectSale newDirectSale = new DirectSale(directSaleId, itemsId, sellerId, price, timeLimit, creationDate);

        return newDirectSale;
    }
}
