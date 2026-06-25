package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Application service responsible for orchestrating item-related use cases,
 * including item registration and retrieval.
 */
@Service
public class ItemService {

    public record ItemRelated(Edition edition, Publication publication, Author author, Genre genre, PublishingCompany publisher) {}

    private final IItemRepo _iItemRepo;
    private final ItemFactory _itemFactory;
    private final IEditionRepo _iEditionRepo;
    private final IPublicationRepo _iPublicationRepo;
    private final IAuthorRepo _iAuthorRepo;
    private final IGenreRepo _iGenreRepo;
    private final IPublishingCompanyRepo _iPublishingCompanyRepo;

    public ItemService(IItemRepo iItemRepo,
                       ItemFactory itemFactory,
                       IEditionRepo iEditionRepo,
                       IPublicationRepo iPublicationRepo,
                       IAuthorRepo iAuthorRepo,
                       IGenreRepo iGenreRepo,
                       IPublishingCompanyRepo iPublishingCompanyRepo) {

        _iItemRepo              = Objects.requireNonNull(iItemRepo,              "ItemRepo is required");
        _itemFactory            = Objects.requireNonNull(itemFactory,            "ItemFactory is required");
        _iEditionRepo           = Objects.requireNonNull(iEditionRepo,           "EditionRepo is required");
        _iPublicationRepo       = Objects.requireNonNull(iPublicationRepo,       "PublicationRepo is required");
        _iAuthorRepo            = Objects.requireNonNull(iAuthorRepo,            "AuthorRepo is required");
        _iGenreRepo             = Objects.requireNonNull(iGenreRepo,             "GenreRepo is required");
        _iPublishingCompanyRepo = Objects.requireNonNull(iPublishingCompanyRepo, "PublishingCompanyRepo is required");
    }

    @Transactional
    public Item registerItem(EditionId editionId,
                             Condition condition,
                             Description description) {

        _iEditionRepo.ofIdentity(editionId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Edition does not exist in the repository"));

        Item newItem = _itemFactory.createItem(editionId, condition, description);
        ItemId itemId = newItem.identity();

        if (_iItemRepo.containsOfIdentity(itemId)) {
            return _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Item with id '" + itemId + "' does not exist"));
        } else {
            return _iItemRepo.save(newItem);
        }
    }

    @Transactional
    public List<Item> getAllItems() {
        List<Item> result = new ArrayList<>();
        _iItemRepo.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public Item getItemById(String itemId) {
        return _iItemRepo.ofIdentity(new ItemId(itemId))
                .orElseThrow(() -> new NoSuchElementException(
                        "Item does not exist in the repository"));
    }

    @Transactional
    public Item markItemAsSold(ItemId itemId) {
        Item item = _iItemRepo.ofIdentity(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item does not exist in the repository"));
        item.markAsSold();
        return _iItemRepo.save(item);
    }

    public ItemRelated resolveRelated(Item item) {

        Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                .orElseThrow(() -> new NoSuchElementException("Edition does not exist in the repository"));

        Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                .orElseThrow(() -> new NoSuchElementException("Publication does not exist in the repository"));

        Author author = _iAuthorRepo.ofIdentity(publication.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException("Author does not exist in the repository"));

        Genre genre = _iGenreRepo.ofIdentity(publication.getGenreId())
                .orElseThrow(() -> new NoSuchElementException("Genre does not exist in the repository"));

        PublishingCompany publisher = _iPublishingCompanyRepo.ofIdentity(edition.getPublishingCompanyId())
                .orElseThrow(() -> new NoSuchElementException("PublishingCompany does not exist in the repository"));

        return new ItemRelated(edition, publication, author, genre, publisher);
    }
}