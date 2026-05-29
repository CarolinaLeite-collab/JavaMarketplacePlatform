package MITELOVERS.applicationservices;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ListOfItemsService {
    private IListOfItemsRepo _listOfItemsRepo;
    private ListOfItemsFactory _factory;
    private ListOfItemsResponseDTOMapper _mapper;
    private IGenreRepo _genreRepo;
    private IItemRepo _itemRepo;

    @Transactional(readOnly = true)
    public List<ListOfItemsResponseDTO> getUserLists(String userId) {
        Objects.requireNonNull(userId);
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

    @Transactional(readOnly = true)
    public ListOfItemsResponseDTO getListById(String listId) {
        ListOfItemsId id = new ListOfItemsId(listId);

        ListOfItems list = _listOfItemsRepo.ofIdentity(id).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

    @Transactional
    public ListOfItemsResponseDTO save(String userId, ListOfItemsRequestDTO dto) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(dto);

        Email email = new Email(userId);
        UserId recUserId = new UserId(email);
        Name name = new Name(dto.getName());
        GenreId genreId = new GenreId(dto.getGenreId());

        if(!_genreRepo.containsOfIdentity(genreId)) {
            throw new IllegalArgumentException("Genre doesn't exist");
        }

        if (getUserLists(userId).contains(name.toString())) {
            throw new IllegalArgumentException("List already exists");
        }

        ListOfItems newList = _listOfItemsRepo.save(_factory.createListOfItems(recUserId, name, genreId));

        ListOfItemsResponseDTO savedDTO = _mapper.toModel(newList);

        return savedDTO;
    }

    @Transactional
    public ListOfItemsResponseDTO addItemToList(String listOfItemsId, AddItemRequestDTO dto) {
        ListOfItemsId recListOfItemsId = new ListOfItemsId(listOfItemsId);

        if (dto == null || dto.getItemId() == null) {
            throw new IllegalArgumentException("ItemId is invalid");
        }

        ListOfItems list = _listOfItemsRepo.ofIdentity(recListOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        ItemId recItemId = new ItemId(dto.getItemId());

        if (!_itemRepo.containsOfIdentity(recItemId)) {
            throw new IllegalArgumentException("Item doesn't exist");
        }

        list.addItem(recItemId);
        _listOfItemsRepo.save(list);

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

    @Transactional
    public ListOfItemsResponseDTO makePublic(String listOfItemsId, MakeListPublicRequestDTO durationDays) {
        ListOfItemsId recListOfItemsId = new ListOfItemsId(listOfItemsId);

        ListOfItems list = _listOfItemsRepo.ofIdentity(recListOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        SharedDuration recSharedUntil = new SharedDuration(durationDays.getSharedUntil());

        if (list.isPrivate()) {

            list.makePublic(recSharedUntil);
        }
        _listOfItemsRepo.save(list);

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

    @Transactional
    public ListOfItemsResponseDTO makePrivate(String listOfItemsId){
        ListOfItemsId recListOfItemsId = new ListOfItemsId(listOfItemsId);

        ListOfItems list = _listOfItemsRepo.ofIdentity(recListOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        if (!list.isPrivate()) {

            list.makePrivate();
        }
        _listOfItemsRepo.save(list);

        ListOfItemsResponseDTO result = _mapper.toModel(list);

        return result;
    }

    @Transactional
    public List<ListOfItemsResponseDTO> findByGenre(String genreId) {
        GenreId recGenreId = new GenreId(genreId);

        Iterable<ListOfItems> lists = _listOfItemsRepo.findAll();

        List<ListOfItems> publicListsByGenre = new ArrayList<>();

        for(ListOfItems list : lists) {
            if(list.getGenreId().equals(recGenreId) && !list.isPrivate()) {
                publicListsByGenre.add(list);
            }
        }

        List<ListOfItemsResponseDTO> result = new ArrayList<>();
        for(ListOfItems list : publicListsByGenre) {
            result.add(_mapper.toModel(list));
        }

        return result;
    }

}
