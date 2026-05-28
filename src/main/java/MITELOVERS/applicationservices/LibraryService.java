package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.ItemDetailsDTO;
import MITELOVERS.dto.LibraryItemDetailsDTO;
import MITELOVERS.dto.LibraryItemSummaryDTO;
import MITELOVERS.mapper.ItemDetailsMapper;
import MITELOVERS.mapper.LibraryItemDetailsMapper;
import MITELOVERS.mapper.LibraryItemSummaryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class LibraryService {

    private final ILibraryRepo _libraryRepo;
    private final IItemRepo _itemRepo;
    private final IEditionRepo _editionRepo;
    private final IPublicationRepo _publicationRepo;
    private final IAuthorRepo _authorRepo;
    private final IPublicationTypeRepo _publicationTypeRepo;
    private final LibraryItemSummaryMapper _summaryMapper;
    private final LibraryItemDetailsMapper _detailsMapper;
    private final ItemDetailsMapper _itemDetailsMapper;
    private final LibraryFactory _libraryFactory;

    public LibraryService(ILibraryRepo libraryRepo,
                          IItemRepo itemRepo,
                          IEditionRepo editionRepo,
                          IPublicationRepo publicationRepo,
                          IAuthorRepo authorRepo,
                          IPublicationTypeRepo publicationTypeRepo,
                          LibraryItemDetailsMapper detailsMapper,
                          LibraryItemSummaryMapper summaryMapper,
                          ItemDetailsMapper itemDetailsMapper,
                          LibraryFactory libraryFactory) {
        _libraryRepo = libraryRepo;
        _itemRepo = itemRepo;
        _editionRepo = editionRepo;
        _publicationRepo = publicationRepo;
        _authorRepo = authorRepo;
        _publicationTypeRepo = publicationTypeRepo;
        _detailsMapper = detailsMapper;
        _summaryMapper = summaryMapper;
        _itemDetailsMapper = itemDetailsMapper;
        _libraryFactory = libraryFactory;

    }

    @Transactional(readOnly = true)
    public List<LibraryItemSummaryDTO> getListOfItemInfoInMyLibrary(UserId userId) {

        LibraryId libraryId = LibraryId.fromUserId(userId);

        if (_libraryRepo.ofIdentity(libraryId).isEmpty()) {
            return Collections.emptyList();
        }

        Library library = _libraryRepo.ofIdentity(libraryId).get();
        List<ItemId> itemIds = library.getItemsIdInLibrary();

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<LibraryItemSummaryDTO> result = new ArrayList<>();

        for (ItemId itemId : itemIds) {

            Item item = _itemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalStateException("Item not found!"));

            Edition edition = _editionRepo.ofIdentity(item.getEditionId())
                    .orElseThrow(() -> new IllegalStateException("Edition not found!"));

            Publication publication = _publicationRepo.ofIdentity(edition.getPublicationId())
                    .orElseThrow(() -> new IllegalStateException("Publication not found!"));

            result.add(_summaryMapper.toDTO(item, publication));
        }

        return result;
    }

    @Transactional(readOnly = true)
    public LibraryItemDetailsDTO getItemDetail(String itemId) {

        Item item = _itemRepo.ofIdentity(new ItemId(itemId))
                .orElseThrow(() -> new IllegalStateException("Item not found!"));

        Edition edition = _editionRepo.ofIdentity(item.getEditionId())
                .orElseThrow(() -> new IllegalStateException("Edition not found!"));

        Publication publication = _publicationRepo.ofIdentity(edition.getPublicationId())
                .orElseThrow(() -> new IllegalStateException("Publication not found!"));

        Author author = _authorRepo.ofIdentity(publication.getAuthorId())
                .orElseThrow(() -> new IllegalStateException("Author not found!"));

        PublicationType publicationType = _publicationTypeRepo.ofIdentity(edition.getPublicationTypeId())
                .orElseThrow(() -> new IllegalStateException("Publication Type not found!"));

        return _detailsMapper.toDTO(edition, author, publicationType);
    }

    @Transactional
    public void addItemToLibrary(ItemId itemId, UserId userId) {

        LibraryId libraryId = LibraryId.fromUserId(userId);

        Library library = _libraryRepo.ofIdentity(libraryId)
                .orElseGet(() -> _libraryFactory.createLibrary(userId));

        boolean added = library.addItemIdToLibrary(itemId);

        if (!added) {
            throw new IllegalStateException("Item already exists in library");
        }

        _libraryRepo.save(library);
    }

    @Transactional(readOnly = true)
    public List<ItemDetailsDTO> getListOfItemInfoInMyLibraryFull(UserId userId) {

        LibraryId libraryId = LibraryId.fromUserId(userId);

        if (_libraryRepo.ofIdentity(libraryId).isEmpty()) {
            return Collections.emptyList();
        }

        Library library = _libraryRepo.ofIdentity(libraryId).get();
        List<ItemId> itemIds = library.getItemsIdInLibrary();

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ItemDetailsDTO> result = new ArrayList<>();

        for (ItemId itemId : itemIds) {

            Item item = _itemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalStateException("Item not found!"));

            Edition edition = _editionRepo.ofIdentity(item.getEditionId())
                    .orElseThrow(() -> new IllegalStateException("Edition not found!"));

            Publication publication = _publicationRepo.ofIdentity(edition.getPublicationId())
                    .orElseThrow(() -> new IllegalStateException("Publication not found!"));

            PublicationType publicationType = _publicationTypeRepo.ofIdentity(edition.getPublicationTypeId())
                    .orElseThrow(() -> new IllegalStateException("Publication Type not found!"));

            Author author = _authorRepo.ofIdentity(publication.getAuthorId())
                    .orElseThrow(() -> new IllegalStateException("Author not found!"));

            result.add(_itemDetailsMapper.toDTO(edition, publication, publicationType, author));
        }

        return result;
    }


//    public void addItemToLibrary(ItemId itemId, UserId userId) {
//        _addItemToLibraryCtrl.addItemIdToLibrary(itemId, userId);
//    }

}

