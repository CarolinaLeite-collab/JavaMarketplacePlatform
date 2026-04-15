package TOPSECRET.controller;

import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.repository.*;
import TOPSECRET.domain.valueobject.*;
import TOPSECRET.dto.ItemDetailsDTO;
import TOPSECRET.mapper.ItemDetailsMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving detailed information about the items
 * contained in a user's library.
 *
 * <p>
 * This controller interacts with multiple repositories to reconstruct the full
 * domain context of each item stored in a {@link Library} associated with a
 * given {@link UserId}. It gathers data from {@link IItemRepo},
 * {@link IEditionRepo}, {@link IPublicationRepo}, {@link IAuthorRepo}, and
 * {@link IPublicationTypeRepo}.
 * </p>
 *
 * <p>
 * The resulting domain objects are then mapped into {@link ItemDetailsDTO}
 * instances using {@link ItemDetailsMapper}, providing a structured
 * representation of the library contents for external use.
 * </p>
 */

public class ListOfItemsInMyLibraryController {

    private final ILibraryRepo _iLibraryRepo;
    private final IItemRepo _iItemRepo;
    private final IEditionRepo _iEditionRepo;
    private final IPublicationRepo _iPublicationRepo;
    private final IAuthorRepo _iAuthorRepo;
    private final IPublicationTypeRepo _iPublicationTypeRepo;

    public ListOfItemsInMyLibraryController(ILibraryRepo libraryRepo,
                                            IItemRepo iItemRepo,
                                            IEditionRepo iEditionRepo,
                                            IPublicationRepo iPublicationRepo,
                                            IAuthorRepo iAuthorRepo,
                                            IPublicationTypeRepo iPublicationTypeRepo,
                                            UserId userId) {
        _iLibraryRepo = libraryRepo;
        _iItemRepo = iItemRepo;
        _iEditionRepo = iEditionRepo;
        _iPublicationRepo = iPublicationRepo;
        _iAuthorRepo = iAuthorRepo;
        _iPublicationTypeRepo = iPublicationTypeRepo;
    }

    public List<ItemDetailsDTO> getListOfItemInfoInMyLibrary(UserId userId) {

        List<ItemId> listOfItemIds = _iLibraryRepo.getItemsInLibraryByUserId(userId);
        List<ItemDetailsDTO> listOfItemDetailsDTOs = new ArrayList<>();

        for  (ItemId itemId : listOfItemIds) {

            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalStateException("Item not found!"));

            Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                    .orElseThrow(() -> new IllegalStateException("Edition not found!"));

            Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                    .orElseThrow(() -> new IllegalStateException("Publication not found!"));

            PublicationType publicationType = _iPublicationTypeRepo.ofIdentity(edition.getPublicationTypeId())
                    .orElseThrow(() -> new IllegalStateException("Publication Type not found!"));

            Author author = _iAuthorRepo.ofIdentity(publication.getAuthorId())
                    .orElseThrow(() -> new IllegalStateException("Author not found!"));

            listOfItemDetailsDTOs.add(ItemDetailsMapper.toDTO(
                    edition,
                    publication,
                    publicationType,
                    author));

        }

        return listOfItemDetailsDTOs;

    }
}
