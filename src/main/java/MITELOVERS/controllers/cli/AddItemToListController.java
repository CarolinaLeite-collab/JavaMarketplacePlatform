package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AddItemToListController {

    private final ListOfItemsService _service;
    private final ListOfItemsResponseDTOMapper _mapper;

    public AddItemToListController(ListOfItemsService service, ListOfItemsResponseDTOMapper mapper) {
        _service = service;
        _mapper = mapper;
    }

    public List<ListOfItemsResponseDTO> getMyLists(String userId) {
        UserId recUserId = new UserId(new Email(userId));

        List<ListOfItems> listOfLists = _service.getUserLists(recUserId);

        List<ListOfItemsResponseDTO> result =  new ArrayList<>();

        for (ListOfItems list : listOfLists) {
            result.add(_mapper.toModel(list));
        }

        return result;
    }

    public ListOfItemsResponseDTO addItemToList(String listOfItemsId, AddItemRequestDTO itemId) {
        ListOfItemsId listId = new ListOfItemsId(listOfItemsId);
        ItemId recItemId = new ItemId(itemId.getItemId());

        ListOfItems list = _service.addItemToList(listId, recItemId);

        ListOfItemsResponseDTO result = _mapper.toModel(list);
        return result;
    }

    public List<ListOfItemsResponseDTO> findByGenre(String genreId) {
        GenreId recGenreId = new GenreId(genreId);

        List<ListOfItems> listOfLists = _service.findByGenre(recGenreId);

        List<ListOfItemsResponseDTO> result =  new ArrayList<>();
        for (ListOfItems list : listOfLists) {
            result.add(_mapper.toModel(list));
        }

        return result;
    }
}