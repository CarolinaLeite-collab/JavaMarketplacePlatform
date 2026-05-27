package MITELOVERS.mapper;

import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.ListOfItemsResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ListOfItemsResponseDTOMapper implements RepresentationModelAssembler<ListOfItems, ListOfItemsResponseDTO> {

    @Override
    public ListOfItemsResponseDTO toModel(ListOfItems listOfItems) {

        List<String> itemIds = listOfItems.getItemIds().stream()
                .map(ItemId::toString)
                .toList();

        ListOfItemsResponseDTO dto = new ListOfItemsResponseDTO(listOfItems.identity().toString(), listOfItems.getUserId().toString(), listOfItems.getName().toString(), listOfItems.getGenreId().toString(), listOfItems.isPrivate(), listOfItems.getSharedUntil(), itemIds);

        String listId = dto.getListId();

        Link link = linkTo(methodOn(ListOfItemsRestController.class).getList(listId)).withSelfRel();

        dto.add(link);

        return dto;
    }
}
