package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Application service responsible for managing a user's library.
 *
 * <p>
 * Coordinates domain repositories to support library-related use cases:
 * retrieving and sorting library items, retrieving a single item's details,
 * listing item identifiers, and adding items to a library.
 * </p>
 */

@Service
public class LibraryService {

    private final ILibraryRepo _libraryRepo;
    private final IItemRepo _itemRepo;
    private final IEditionRepo _editionRepo;
    private final IPublicationRepo _publicationRepo;
    private final IAuthorRepo _authorRepo;
    private final IPublicationTypeRepo _publicationTypeRepo;
    private final LibraryFactory _libraryFactory;

    public LibraryService(ILibraryRepo libraryRepo,
                          IItemRepo itemRepo,
                          IEditionRepo editionRepo,
                          IPublicationRepo publicationRepo,
                          IAuthorRepo authorRepo,
                          IPublicationTypeRepo publicationTypeRepo,
                          LibraryFactory libraryFactory) {
        _libraryRepo = libraryRepo;
        _itemRepo = itemRepo;
        _editionRepo = editionRepo;
        _publicationRepo = publicationRepo;
        _authorRepo = authorRepo;
        _publicationTypeRepo = publicationTypeRepo;
        _libraryFactory = libraryFactory;

    }

    @Transactional(readOnly = true)
    public List<LibraryItemDetails> getListOfItemInfoInMyLibrary(String userId) {

        UserId uid = new UserId(new Email(userId));

        return getListOfItemInfoInMyLibrary(uid, LibrarySort.NONE);
    }

    @Transactional(readOnly = true)
    public List<LibraryItemDetails> getListOfItemInfoInMyLibrary(UserId userId, LibrarySort sort) {
        LibraryId libraryId = LibraryId.fromUserId(userId);

        if (_libraryRepo.ofIdentity(libraryId).isEmpty()) {
            return Collections.emptyList();
        }

        Library library = _libraryRepo.ofIdentity(libraryId).get();
        List<ItemId> itemIds = library.getItemsIdInLibrary();

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<LibraryItemDetails> result = new ArrayList<>();

        for (ItemId itemId : itemIds) {
            result.add(loadItemDetails(itemId));
        }

        sortLibraryItems(result, sort);

        return result;
    }

    @Transactional(readOnly = true)
    public LibraryItemDetails getItemDetail(String itemId) {
        return loadItemDetails(new ItemId(itemId));
    }

    @Transactional
    public LibraryItemDetails addItemToLibrary(String itemId, String userId) {

        ItemId itemId1 = new ItemId(itemId);
        UserId uid = new UserId(new Email(userId));
        LibraryId libraryId = LibraryId.fromUserId(uid);

        Item item = _itemRepo.ofIdentity(itemId1)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Library library = _libraryRepo.ofIdentity(libraryId)
                .orElseGet(() -> _libraryFactory.createLibrary(uid));

        boolean added = library.addItemIdToLibrary(itemId1);

        if (!added) {
            throw new IllegalStateException("Item already exists in library");
        }

        _libraryRepo.save(library);

        return loadItemDetails(itemId1);
    }

    @Transactional(readOnly = true)
    public List<LibraryItemDetails> getListOfItemInfoInMyLibraryFull(String userId) {

        UserId uid = new UserId(new Email(userId));
        LibraryId libraryId = LibraryId.fromUserId(uid);

        if (_libraryRepo.ofIdentity(libraryId).isEmpty()) {
            return Collections.emptyList();
        }

        Library library = _libraryRepo.ofIdentity(libraryId).get();
        List<ItemId> itemIds = library.getItemsIdInLibrary();

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<LibraryItemDetails> result = new ArrayList<>();

        for (ItemId itemId : itemIds) {
            result.add(loadItemDetails(itemId));
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<ItemId> getItemIdsInLibrary(String userId) {
        UserId uid = new UserId(new Email(userId));
        LibraryId libraryId = LibraryId.fromUserId(uid);

        return _libraryRepo.ofIdentity(libraryId)
                .map(Library::getItemsIdInLibrary)
                .orElse(Collections.emptyList());
    }

    private LibraryItemDetails loadItemDetails(ItemId itemId) {

        Item item = _itemRepo.ofIdentity(itemId)
                .orElseThrow(() -> new IllegalStateException("Item not found!"));

        Edition edition = _editionRepo.ofIdentity(item.getEditionId())
                .orElseThrow(() -> new IllegalStateException("Edition not found!"));

        Publication publication = _publicationRepo.ofIdentity(edition.getPublicationId())
                .orElseThrow(() -> new IllegalStateException("Publication not found!"));

        Author author = _authorRepo.ofIdentity(publication.getAuthorId())
                .orElseThrow(() -> new IllegalStateException("Author not found!"));

        PublicationType publicationType = _publicationTypeRepo.ofIdentity(edition.getPublicationTypeId())
                .orElseThrow(() -> new IllegalStateException("Publication Type not found!"));

        return new LibraryItemDetails(item, publication, edition, author, publicationType);
    }


    private void sortLibraryItems(List<LibraryItemDetails> items, LibrarySort sort) {
        Comparator<LibraryItemDetails> comparator = switch (sort) {
            case TITLE -> Comparator.comparing(
                    details -> details.publication().getTitle().toString(),
                    String.CASE_INSENSITIVE_ORDER
            );
            case AUTHOR -> Comparator.comparing(
                    details -> details.author().getName().toString(),
                    String.CASE_INSENSITIVE_ORDER
            );
            case PUBLICATION_TYPE -> Comparator.comparing(
                    details -> details.publicationType().toString(),
                    String.CASE_INSENSITIVE_ORDER
            );
            case IDENTIFIER -> Comparator.comparing(
                    details -> details.edition().getIdentifier().toString(),
                    String.CASE_INSENSITIVE_ORDER
            );
            case NONE -> null;
        };

        if (comparator != null) {
            items.sort(comparator);
        }
    }


}

