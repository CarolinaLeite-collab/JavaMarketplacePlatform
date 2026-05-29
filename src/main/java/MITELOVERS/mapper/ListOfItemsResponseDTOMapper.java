package MITELOVERS.mapper;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListOfItemsResponseDTOMapper implements RepresentationModelAssembler<ListOfItems, ListOfItemsResponseDTO> {

    @Override
    public ListOfItemsResponseDTO toModel(ListOfItems listOfItems) {

        List<String> itemIds = listOfItems.getItemIds().stream()
                .map(ItemId::toString)
                .toList();

        ListOfItemsResponseDTO dto = new ListOfItemsResponseDTO(listOfItems.identity().toString(), listOfItems.getUserId().toString(), listOfItems.getName().toString(), listOfItems.getGenreId().toString(), listOfItems.isPrivate(), listOfItems.getSharedUntil(), itemIds);

        return dto;
    }
}
