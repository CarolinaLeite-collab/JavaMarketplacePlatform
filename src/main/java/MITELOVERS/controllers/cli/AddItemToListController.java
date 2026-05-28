package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.AddItemRequestDTO;
import MITELOVERS.dto.ListOfItemsResponseDTO;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AddItemToListController {

    private final ListOfItemsService _service;

    public AddItemToListController(ListOfItemsService service) {
        _service = service;
    }

    public List<ListOfItemsResponseDTO> getMyLists(String userId) {
        return _service.getUserLists(userId);
    }

    public ListOfItemsResponseDTO addItemToList(String listOfItemsId, AddItemRequestDTO itemId) {
        return _service.addItemToList(listOfItemsId, itemId);
    }

    public List<ListOfItemsResponseDTO> findByGenre(String genreId) {
        return _service.findByGenre(genreId);
    }
}