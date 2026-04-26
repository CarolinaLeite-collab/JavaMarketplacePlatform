package MITELOVERS.persistence.jpa.assembler;


import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.time.Period;

@Component
public class DirectSaleAssembler {
    @Autowired
    private final DirectSaleFactory factory;

    public DirectSaleAssembler(DirectSaleFactory factory){
        this.factory = factory;
    }

    public DirectSaleDataModel domain2DM(DirectSale directSale){

        DirectSaleDataModel dm = new DirectSaleDataModel(
                directSale.identity().toString(),
                mapItemsToString(directSale.getItemsId()),
                new PriceDataModel(
                        directSale.getPrice().getValue(),
                        directSale.getPrice().getCurrency().toString()),
                directSale.getTimeLimit().toString());

        return dm;
    }

    public DirectSale DM2Domain(DirectSaleDataModel dm){

        DirectSale directSale = factory.createDirectSale(
                new DirectSaleId(dm.getDirectSaleId()),
                mapStringToItems(dm.getItemsId()),
                new Price(
                        dm.getPrice().getNumericValue(),
                        Currency.valueOf(dm.getPrice().getCurrency())),
                Period.parse(dm.getTimeLimit())
        );

        return directSale;
    }

    private List<String> mapItemsToString(List<ItemId> itemsId){
        List<String> result = new ArrayList<>();

        for (ItemId item : itemsId) {
            result.add(item.toString());
        }
        return result;
    }

    private List<ItemId> mapStringToItems(List<String> itemsId){
        List<ItemId> result = new ArrayList<>();

        for (String item : itemsId) {
            result.add(new ItemId(item));
        }
        return result;
    }

}
