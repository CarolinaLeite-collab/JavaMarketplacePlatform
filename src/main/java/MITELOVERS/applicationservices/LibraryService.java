package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.ItemDetailsDTO;
import MITELOVERS.mapper.ItemDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class LibraryService {

    @Autowired
    private ILibraryRepo _libraryRepo;

    @Autowired
    private IItemRepo _itemRepo;

    @Autowired
    private IEditionRepo _editionRepo;

    @Autowired
    private IPublicationRepo _publicationRepo;

    @Autowired
    private IAuthorRepo _authorRepo;

    @Autowired
    private IPublicationTypeRepo _publicationTypeRepo;

    public LibraryService(ILibraryRepo libraryRepo,
                          IItemRepo itemRepo,
                          IEditionRepo editionRepo,
                          IPublicationRepo publicationRepo,
                          IAuthorRepo authorRepo,
                          IPublicationTypeRepo publicationTypeRepo) {
        _libraryRepo = libraryRepo;
        _itemRepo = itemRepo;
        _editionRepo = editionRepo;
        _publicationRepo = publicationRepo;
        _authorRepo = authorRepo;
        _publicationTypeRepo = publicationTypeRepo;
    }

    @Transactional(readOnly = true)
    public List<ItemDetailsDTO> getListOfItemInfoInMyLibrary(UserId userId) {

        LibraryId libraryId = LibraryId.fromUserId(userId);

        Library library = _libraryRepo.ofIdentity(libraryId)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

        List<ItemId> itemIds = library.getItemsIdInLibrary();
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

            result.add(ItemDetailsMapper.toDTO(
                    edition,
                    publication,
                    publicationType,
                    author
            ));
        }

        return result;
    }

//    public void addItemToLibrary(ItemId itemId, UserId userId) {
//        _addItemToLibraryCtrl.addItemIdToLibrary(itemId, userId);
//    }

}

