package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.dto.request.DirectSaleRequestDTO;
import MITELOVERS.dto.response.DSFilteredItemsResponseDTO;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import MITELOVERS.mapper.DSFilteredItemsResponseMapper;
import MITELOVERS.mapper.DirectSaleResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

/**
 * Application service responsible for managing Direct Sales within the domain.
 *
 * <p>This service coordinates domain operations such as creating new direct
 * sales, validating item availability, retrieving existing sales, and
 * performing filtered queries (e.g., by genre). It acts as the orchestration
 * layer between repositories, factories, and DTO mappers, ensuring that
 * domain invariants are respected.</p>
 *
 * <p>All returned objects are mapped into DTOs suitable for exposure at the
 * API layer, keeping domain objects internal to the application.</p>
 */

@Service
public class DirectSaleService {

    private final IGenreRepo _iGenreRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;
    private final DirectSaleFactory _directSaleFactory;

    public DirectSaleService(IGenreRepo iGenreRepo,
                             IDirectSaleRepo iDirectSaleRepo,
                             IItemRepo iItemRepo,
                             DirectSaleFactory directSaleFactory) {

        _iGenreRepo = Objects.requireNonNull(iGenreRepo);
        _iDirectSaleRepo = Objects.requireNonNull(iDirectSaleRepo);
        _iItemRepo = Objects.requireNonNull(iItemRepo);
        _directSaleFactory = Objects.requireNonNull(directSaleFactory);
    }

    public DirectSale createDirectSale(DirectSaleRequestDTO request) {

        List<ItemId> itemsId = request.getItemsId()
                .stream()
                .map(ItemId::new)
                .toList();

        // 1. Fail fast on duplicates
        Set<ItemId> unique = new HashSet<>(itemsId);
        if (unique.size() != itemsId.size()) {
            throw new IllegalArgumentException("Duplicate items are not allowed in a DirectSale.");
        }

        // Validate items
        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

            if (item.getSaleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException(itemId + " is already on sale!");
            }
        }

        Price price = new Price(
                request.getPriceValue(),
                Currency.valueOf(request.getPriceCurrency())
        );

        Duration timeLimit = request.getTimeLimitSeconds() != null
                ? Duration.ofSeconds(request.getTimeLimitSeconds())
                : null;

        // Create the DS
        DirectSale newDirectSale =
                _directSaleFactory.createDirectSale(itemsId, price, timeLimit);

        if (_iDirectSaleRepo.containsOfIdentity(newDirectSale.identity())) {
            throw new IllegalStateException("DirectSale already exists");
        }

        // 5. Mark items as on sale (after validation)
        for (ItemId itemId : itemsId) {
            Item item = _iItemRepo.ofIdentity(itemId).get();
            item.markAsDirectSale();
            _iItemRepo.save(item);
        }

        return _iDirectSaleRepo.save(newDirectSale);
    }

    public List<DirectSale> getAllDirectSales() {

        List<DirectSale> result = new ArrayList<>();

        _iDirectSaleRepo.findAll().forEach(result::add);

        return result;
    }

    public DirectSale getDirectSaleById(String id) {

        DirectSaleId directSaleId = new DirectSaleId(id);

        return _iDirectSaleRepo.ofIdentity(directSaleId)
                .orElseThrow(() -> new NoSuchElementException("DirectSale not found"));
    }

    //-----------------------
    // Filtered Direct Sales
    //-----------------------

    public List<DirectSaleId> getDirectSaleItemsByGenreAsc(String genreId) {

        if (genreId == null || genreId.isBlank()) {
            return new ArrayList<>();
        }

        GenreId gid = new GenreId(genreId);

        if (!_iGenreRepo.containsOfIdentity(gid)) {
            throw new IllegalArgumentException("Genre not found!");
        }

        List<ItemId> filteredItems = _iItemRepo.findByGenreId(gid);

        if (filteredItems == null || filteredItems.isEmpty()) {
            return new ArrayList<>();
        }

        //Fetch active DirectSales
        Iterable<DirectSale> allSales = _iDirectSaleRepo.findAll();

        if (allSales == null) {
            return new ArrayList<>();
        }

        Set<DirectSaleId> distinctSaleIds = new LinkedHashSet<>();

        for (DirectSale ds : allSales) {

            if (ds != null && ds.getItemsId() != null) {

                boolean containsMatchingItem = ds.getItemsId()
                        .stream()
                        .anyMatch(filteredItems::contains);

                if (containsMatchingItem && ds.identity() != null) {
                    distinctSaleIds.add(ds.identity());
                }
            }
        }
        return new ArrayList<>(distinctSaleIds);
    }

}
