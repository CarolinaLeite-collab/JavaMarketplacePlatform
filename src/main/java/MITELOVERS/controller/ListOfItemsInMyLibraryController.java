package MITELOVERS.controller;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
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

    private final IRepository<LibraryId, Library> _libraryRepo;
    private final IRepository<ItemId, Item> _itemRepo;
    private final IRepository<EditionId, Edition> _editionRepo;
    private final IRepository<PublicationId, Publication> _publicationRepo;
    private final IRepository<AuthorId, Author> _authorRepo;
    private final IRepository<PublicationTypeId, PublicationType> _publicationTypeRepo;

    public ListOfItemsInMyLibraryController(IRepository<LibraryId, Library> libraryRepo,
                                            IRepository<ItemId, Item> itemRepo,
                                            IRepository<EditionId, Edition> editionRepo,
                                            IRepository<PublicationId, Publication> publicationRepo,
                                            IRepository<AuthorId, Author> authorRepo,
                                            IRepository<PublicationTypeId, PublicationType> publicationTypeRepo) {

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
