package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class DirectSaleAssembler {

    private final DirectSaleFactory factory;

    public DirectSaleAssembler(DirectSaleFactory factory) {
        this.factory = factory;
    }

    public DirectSaleDataModel toDataModel(DirectSale directSale) {

        long timeLimit = directSale.getTimeLimit() != null
                ? directSale.getTimeLimit().toDays()
                : 0L;

        DirectSaleDataModel dm = new DirectSaleDataModel(
                directSale.identity().toString(),
                directSale.getSellerId().toString(),
                mapItemsToString(directSale.getItemsId()),
                new PriceDataModel(
                        directSale.getPrice().getValue(),
                        directSale.getPrice().getCurrency().toString()),
                timeLimit,
                directSale.getCreationDate(),
                directSale.getDSStatus().toString());

        return dm;
    }

    public DirectSale toDomain(DirectSaleDataModel dm) {

        Duration timeLimit = dm.getTimeLimit() == 0
                ? null
                : Duration.ofDays(dm.getTimeLimit());

        DirectSale directSale = factory.createDirectSale(
                new DirectSaleId(dm.getDirectSaleId()),
                mapStringToItems(dm.getItemsId()),
                new UserId(new Email(dm.getUserId())),
                new Price(
                        dm.getPrice().getNumericValue(),
                        Currency.valueOf(dm.getPrice().getCurrency())),
                timeLimit,
                dm.getCreationDate(),
                DirectSaleStatus.valueOf(dm.getStatus())
        );

        return directSale;
    }

    private List<String> mapItemsToString(List<ItemId> itemsId) {
        List<String> result = new ArrayList<>();
        for (ItemId item : itemsId) {
            result.add(item.toString());
        }
        return result;
    }

    private List<ItemId> mapStringToItems(List<String> itemsId) {
        List<ItemId> result = new ArrayList<>();
        for (String item : itemsId) {
            result.add(new ItemId(item));
        }
        return result;
    }
}