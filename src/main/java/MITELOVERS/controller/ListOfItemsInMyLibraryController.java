package MITELOVERS.controller;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving detailed information about the items
 * contained in a user's library.
 *
 * This controller interacts with multiple repositories to reconstruct the full
 * domain context of each item stored in a {@link Library} associated with a
 * given {@link UserId}.
 *
 * The resulting domain objects are mapped into {@link ItemDetailsDTO}
 * instances using {@link ItemDetailsMapper}.
 */

public class ListOfItemsInMyLibraryController {

    private final ILibraryRepo _libraryRepo;
    private final IItemRepo _itemRepo;
    private final IEditionRepo _editionRepo;
    private final IPublicationRepo _publicationRepo;
    private final IAuthorRepo _authorRepo;
    private final IPublicationTypeRepo _publicationTypeRepo;

    public ListOfItemsInMyLibraryController(ILibraryRepo libraryRepo,
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

}
