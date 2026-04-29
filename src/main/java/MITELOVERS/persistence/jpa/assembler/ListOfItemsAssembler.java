package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler responsible for converting between {@link ListOfItems} domain objects
 * and {@link ListOfItemsDataModel} persistence objects.
 */
@Component
@AllArgsConstructor
public class ListOfItemsAssembler {

    private final ListOfItemsFactory _listOfItemsFactory;

    public ListOfItemsDataModel toDataModel(ListOfItems listOfItems) {
        if (listOfItems == null)
            throw new IllegalArgumentException("ListOfItems cannot be null");

        List<String> itemIds = listOfItems.getItemIds().stream()
                .map(ItemId::toString)
                .toList();

        return new ListOfItemsDataModel(
                listOfItems.identity().toString(),
                listOfItems.getUserId().toString(),
                listOfItems.getName().toString(),
                listOfItems.getGenreId().toString(),
                listOfItems.isPrivate(),
                itemIds
        );
    }

    public ListOfItems toDomain(ListOfItemsDataModel dm) {
        if (dm == null)
            throw new IllegalArgumentException("ListOfItemsDataModel cannot be null");

        ListOfItemsId listOfItemsId = new ListOfItemsId(dm.getListOfItemsId());
        UserId userId = new UserId(new Email(dm.getUserId()));
        Name name = new Name(dm.getName());
        GenreId genreId = new GenreId(dm.getGenreId());

        ListOfItems listOfItems = _listOfItemsFactory.createListOfItems(
                listOfItemsId, userId, name, genreId);

        if (!dm.isPrivate())
            listOfItems.makePublic();

        dm.getItemIds().stream()
                .map(ItemId::new)
                .forEach(listOfItems::addItem);

        return listOfItems;
    }

    public List<ListOfItemsDataModel> toDataModelList(List<ListOfItems> listOfItems) {
        List<ListOfItemsDataModel> list = new ArrayList<>();
        for (ListOfItems item : listOfItems) {
            list.add(toDataModel(item));
        }
        return list;
    }

    public List<ListOfItems> toDomainList(List<ListOfItemsDataModel> dataModels) {
        List<ListOfItems> list = new ArrayList<>();
        for (ListOfItemsDataModel dm : dataModels) {
            list.add(toDomain(dm));
        }
        return list;
    }

}