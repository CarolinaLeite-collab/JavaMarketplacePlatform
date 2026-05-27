package MITELOVERS.applicationservices;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.ListOfItemsRequestDTO;
import MITELOVERS.dto.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListOfItemsService {
    @Autowired
    private IListOfItemsRepo _listOfItemsRepo;
    @Autowired
    private ListOfItemsFactory _factory;
    @Autowired
    private ListOfItemsResponseDTOMapper _mapper;
    @Autowired
    private IGenreRepo _genreRepo;
    @Autowired
    private IItemRepo _itemRepo;

    @Transactional(readOnly = true)
    public List<ListOfItemsResponseDTO> getUserLists(String userId) {
        Email email = new Email(userId);
        UserId newUserId = new UserId(email);

        Iterable<ListOfItems> lists = _listOfItemsRepo.findListOfItemsByUserId(newUserId);

        List<ListOfItemsResponseDTO> result = new ArrayList<>();

        for (ListOfItems list : lists) {

            ListOfItemsResponseDTO listDTO = _mapper.toModel(list);

            result.add(listDTO);
        }

        return result;
    }

    public ListOfItemsResponseDTO getList(String listId) {
        ListOfItemsId id = new ListOfItemsId(listId);

        ListOfItems list = _listOfItemsRepo.ofIdentity(id).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

    @Transactional
    public ListOfItemsResponseDTO save(String userId, ListOfItemsRequestDTO dto) {
        Email email = new Email(userId);
        UserId recUserId = new UserId(email);
        Name name = new Name(dto.getName());
        GenreId genreId = new GenreId(dto.getGenreId());

        if(!_genreRepo.containsOfIdentity(genreId)) {
            throw new IllegalArgumentException("Genre doesn't exist");
        }

        ListOfItems newList = _listOfItemsRepo.save(_factory.createListOfItems(recUserId, name, genreId));

        ListOfItemsResponseDTO savedDTO = _mapper.toModel(newList);

        return savedDTO;
    }

    @Transactional
    public ListOfItemsResponseDTO addItemToList(String listOfItemsId, String itemId) {
        ListOfItemsId recListOfItemsId = new ListOfItemsId(listOfItemsId);

        ListOfItems list = _listOfItemsRepo.ofIdentity(recListOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        ItemId recItemId = new ItemId(itemId);

        if (!_itemRepo.containsOfIdentity(recItemId)) {
            throw new IllegalArgumentException("Item doesn't exist");
        }

        list.addItem(recItemId);

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

    public ListOfItemsResponseDTO makePublic(String listOfItemsId, int sharedUntil) {
        ListOfItemsId recListOfItemsId = new ListOfItemsId(listOfItemsId);

        ListOfItems list = _listOfItemsRepo.ofIdentity(recListOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        SharedDuration recSharedUntil = new SharedDuration(sharedUntil);

        list.makePublic(recSharedUntil);

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

}
