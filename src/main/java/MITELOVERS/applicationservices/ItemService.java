package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.ItemResponseDTO;
import MITELOVERS.mapper.ItemResponseDTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class ItemService {

    private final IItemRepo _iItemRepo;
    private final ItemFactory _itemFactory;
    private final IEditionRepo _iEditionRepo;
    private final IPublicationRepo _iPublicationRepo;
    private final IAuthorRepo _iAuthorRepo;
    private final IGenreRepo _iGenreRepo;
    private final ItemResponseDTOMapper _itemResponseDTOMapper;

    public ItemService(IItemRepo iItemRepo,
                       ItemFactory itemFactory,
                       IEditionRepo iEditionRepo,
                       IPublicationRepo iPublicationRepo,
                       IAuthorRepo iAuthorRepo,
                       IGenreRepo iGenreRepo,
                       ItemResponseDTOMapper mapper) {

        _iItemRepo = Objects.requireNonNull(iItemRepo, "ItemRepo is required");
        _itemFactory = Objects.requireNonNull(itemFactory, "ItemFactory is required");
        _iEditionRepo = Objects.requireNonNull(iEditionRepo, "EditionRepo is required");
        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo, "PublicationRepo is required");
        _iAuthorRepo = Objects.requireNonNull(iAuthorRepo, "AuthorRepo is required");
        _iGenreRepo = Objects.requireNonNull(iGenreRepo, "GenreRepo is required");
        _itemResponseDTOMapper = Objects.requireNonNull(mapper, "ItemResponseDTOMapper is required");
    }

    @Transactional
    public ItemResponseDTO registerItem(EditionId editionId,
                                        Condition condition,
                                        Description description) {

        Edition edition = _iEditionRepo.ofIdentity(editionId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Edition does not exist in the repository"));

        Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Publication does not exist in the repository"));

        Author author = _iAuthorRepo.ofIdentity(publication.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Author does not exist in the repository"));

        Genre genre = _iGenreRepo.ofIdentity(publication.getGenreId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Genre does not exist in the repository"));

        Item newItem = _itemFactory.createItem(editionId, condition, description);
        Item savedItem = _iItemRepo.save(newItem);

        return _itemResponseDTOMapper.toResponseDTO(savedItem, edition, publication, author, genre);
    }

    public List<ItemResponseDTO> getAllItems() {

        Iterable<Item> items = _iItemRepo.findAll();

        List<ItemResponseDTO> response = new ArrayList<>();

        for (Item item : items) {

            Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Edition does not exist in the repository"));

            Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Publication does not exist in the repository"));

            Author author = _iAuthorRepo.ofIdentity(publication.getAuthorId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Author does not exist in the repository"));

            Genre genre = _iGenreRepo.ofIdentity(publication.getGenreId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Genre does not exist in the repository"));

            response.add(_itemResponseDTOMapper.toResponseDTO(item, edition, publication, author, genre));
        }

        return response;
    }

    public ItemResponseDTO getItemById(String itemId) {

        Item item = _iItemRepo.ofIdentity(new ItemId(itemId))
                .orElseThrow(() -> new NoSuchElementException(
                        "Item does not exist in the repository"));

        Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Edition does not exist in the repository"));

        Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Publication does not exist in the repository"));

        Author author = _iAuthorRepo.ofIdentity(publication.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Author does not exist in the repository"));

        Genre genre = _iGenreRepo.ofIdentity(publication.getGenreId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Genre does not exist in the repository"));

        return _itemResponseDTOMapper.toResponseDTO(item, edition, publication, author, genre);
    }
}