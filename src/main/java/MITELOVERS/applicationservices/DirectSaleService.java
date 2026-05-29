package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.DirectSaleResponseDTO;
import MITELOVERS.dto.DSFilteredItemsResponseDTO;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class DirectSaleService {

    private final ILibraryRepo _iLibraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;
    private final IPublicationRepo _iPublicationRepo;
    private final IEditionRepo _iEditionRepo;
    private final DirectSaleFactory _directSaleFactory;
    private final DirectSaleResponseDTOMapper _responseMapper;


    public DirectSaleService(ILibraryRepo iLibraryRepo,
                             IDirectSaleRepo iDirectSaleRepo,
                             IItemRepo iItemRepo,
                             IPublicationRepo iPublicationRepo,
                             IEditionRepo iEditionRepo,
                             DirectSaleFactory directSaleFactory,
                             DirectSaleResponseDTOMapper responseMapper) {

        _iLibraryRepo = Objects.requireNonNull(iLibraryRepo);
        _iDirectSaleRepo = Objects.requireNonNull(iDirectSaleRepo);
        _iItemRepo = Objects.requireNonNull(iItemRepo);
        _iPublicationRepo = Objects.requireNonNull(iPublicationRepo);
        _iEditionRepo = Objects.requireNonNull(iEditionRepo);
        _directSaleFactory = Objects.requireNonNull(directSaleFactory);
        _responseMapper = Objects.requireNonNull(responseMapper);
    }

    public DirectSaleResponseDTO createDirectSale(DirectSaleRequestDTO request) {

        List<ItemId> itemsId = request.getItemsId().stream()
                .map(ItemId::new)
                .toList();

        Price price = new Price(
                request.getPriceValue(),
                Currency.valueOf(request.getPriceCurrency())
        );

        Duration timeLimit = request.getTimeLimitSeconds() != null
                ? Duration.ofSeconds(request.getTimeLimitSeconds())
                : null;

        DirectSale newDirectSale =
                _directSaleFactory.createDirectSale(itemsId, price, timeLimit);

        if (_iDirectSaleRepo.containsOfIdentity(newDirectSale.identity())) {
            throw new IllegalStateException("DirectSale already exists");
        }

        for (ItemId itemId : itemsId) {
            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

            if (item.getSaleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException(itemId + " is already on sale!");
            }

            item.markAsDirectSale();
        }

        DirectSale saved = _iDirectSaleRepo.save(newDirectSale);

        return _responseMapper.toResponseDTO(saved);
    }

    public List<DirectSaleResponseDTO> getAllDirectSales() {

        Iterable<DirectSale> directSales = _iDirectSaleRepo.findAll();

        List<DirectSaleResponseDTO> response = new ArrayList<>();

        for (DirectSale ds : directSales) {
            response.add(_responseMapper.toResponseDTO(ds));
        }

        return response;
    }

    public DirectSaleResponseDTO getDirectSaleById(String id) {

        DirectSaleId directSaleId = new DirectSaleId(id);

        DirectSale directSale = _iDirectSaleRepo.ofIdentity(directSaleId)
                .orElseThrow(() -> new NoSuchElementException("DirectSale not found"));

        return _responseMapper.toResponseDTO(directSale);
    }

    //-----------------------
    // Filtered Direct Sales
    //-----------------------
    private List<ItemId> filterItemsByGenre(GenreId genreId) {

        List<ItemId> result = new ArrayList<>();

        for (DirectSale directSale : _iDirectSaleRepo.findAll()) {

            for (ItemId itemId : directSale.getItemsId()) {

                Item item = _iItemRepo.ofIdentity(itemId)
                        .orElseThrow(() -> new IllegalStateException("Item not found"));

                Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                        .orElseThrow(() -> new IllegalStateException("Edition not found"));

                Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                        .orElseThrow(() -> new IllegalStateException("Publication not found"));

                if (publication.isByGenreId(genreId)) {
                    result.add(itemId);
                }
            }
        }

        return result;
    }

    public DSFilteredItemsResponseDTO getDirectSaleItemsByGenreAsc(String genreId) {

        GenreId gid = new GenreId(genreId);

        List<ItemId> filtered = filterItemsByGenre(gid);

        if (filtered.isEmpty()) {
            throw new IllegalStateException("No matching DirectSales");
        }

        List<String> sorted = _iDirectSaleRepo
                .findByItemsIdSortedByPublicationDateAsc(filtered)
                .stream()
                .map(ItemId::toString)
                .toList();

        return new DSFilteredItemsResponseDTO(sorted);
    }

}
