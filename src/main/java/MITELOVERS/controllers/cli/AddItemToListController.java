package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.AddItemRequestDTO;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AddItemToListController {

    private final ListOfItemsService _service;

    public AddItemToListController(ListOfItemsService service) {
        _service = service;
    }

    public List<ListOfItems> getMyLists(String userId) {

        UserId recUserId = new UserId(new Email(userId));

        return _service.getUserLists(recUserId);
    }

    public ListOfItems addItemToList(String listOfItemsId, AddItemRequestDTO dto) {

        ListOfItemsId listId = new ListOfItemsId(listOfItemsId);

        ItemId itemId = new ItemId(dto.getItemId());

        return _service.addItemToList(listId, itemId);
    }

    public List<ListOfItems> findByGenre(String genreId) {

        GenreId recGenreId = new GenreId(genreId);

        return _service.findByGenre(recGenreId);
    }
}