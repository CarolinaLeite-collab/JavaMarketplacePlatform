package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import MITELOVERS.domain.item.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link Item} domain objects
 * and {@link ItemDataModel} persistence objects.
 */

@Component
@AllArgsConstructor

public class ItemAssembler {

    private final ItemFactory _itemFactory;

    public ItemDataModel toDataModel(Item item) {

        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        return new ItemDataModel(
              item.identity().toString(),
              item.getEditionId().toString(),
              item.getCondition().toString(),
              item.getDescription().toString(),
                item.getSaleStatus().toString()
        );

    }

    public Item toDomain(ItemDataModel itemDataModel) {

        if (itemDataModel == null) {
            throw new IllegalArgumentException("ItemDataModel cannot be null");
        }

        return _itemFactory.createItem(
                new ItemId(itemDataModel.getId()),
                new EditionId(itemDataModel.getEditionId()),
                Condition.valueOf(itemDataModel.getCondition()),
                new Description(itemDataModel.getDescription()),
                SaleStatus.valueOf(itemDataModel.getSaleStatus())
        );



    }

}
