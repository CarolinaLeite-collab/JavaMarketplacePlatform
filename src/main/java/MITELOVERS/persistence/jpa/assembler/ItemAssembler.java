package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Assembler responsible for converting between {@link Item} domain objects
 * and {@link ItemDataModel} persistence objects.
 */

@Component
@AllArgsConstructor

public class ItemAssembler {

    private final ItemFactory _itemFactory;

    public ItemDataModel toDataModel(Item item) {
        Objects.requireNonNull(item, "Item cannot be null");

        String pictureValue;

        if (item.getPicture() != null) {
            pictureValue = item.getPicture().toString();
        } else {
            pictureValue = null;
        }

        return new ItemDataModel(
              item.identity().getValue(),
              item.getEditionId().getValue(),
              item.getCondition().name(),
              item.getDescription().toString(),
                item.getSaleStatus().name(),
                item.getName().toString(),
                pictureValue
        );

    }

    public Item toDomain(ItemDataModel itemDataModel) {
        Objects.requireNonNull(itemDataModel, "ItemDataModel cannot be null");

        Picture picture;

        if (itemDataModel.getPicture() != null) {
            picture = new Picture(itemDataModel.getPicture());
        } else {
            picture = null;
        }

        return _itemFactory.createItem(
                new ItemId(itemDataModel.getId()),
                new EditionId(itemDataModel.getEditionId()),
                Condition.valueOf(itemDataModel.getCondition()),
                new Description(itemDataModel.getDescription()),
                SaleStatus.valueOf(itemDataModel.getSaleStatus()),
                new Name(itemDataModel.getName()),
                picture
        );

    }




}
