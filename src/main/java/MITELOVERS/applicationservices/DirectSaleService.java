package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.domain.valueobject.Currency;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

/**
 * Application service responsible for managing Direct Sales within the domain.
 *
 * <p>This service coordinates domain operations such as creating new direct
 * sales, validating item availability, retrieving existing sales, and
 * performing filtered queries (e.g., by genre). It acts as the orchestration
 * layer between repositories and factories, ensuring that domain invariants
 * are respected.</p>
 */

@Service
public class DirectSaleService {

    private final IGenreRepo _iGenreRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;
    private final UserService _userService;
    private final IItemRepo _iItemRepo;
    private final DirectSaleFactory _directSaleFactory;

    public DirectSaleService(IGenreRepo iGenreRepo,
                             IDirectSaleRepo iDirectSaleRepo,
                             IItemRepo iItemRepo,
                             UserService userService,
                             DirectSaleFactory directSaleFactory) {

        _iGenreRepo = Objects.requireNonNull(iGenreRepo);
        _iDirectSaleRepo = Objects.requireNonNull(iDirectSaleRepo);
        _iItemRepo = Objects.requireNonNull(iItemRepo);
        _userService = Objects.requireNonNull(userService);
        _directSaleFactory = Objects.requireNonNull(directSaleFactory);
    }

    @Transactional
    public DirectSale createDirectSale(List<ItemId> itemsId, UserId sellerId, Price price, Duration timeLimit) {

        if (!(_userService.userIdExists(sellerId.getEmail().getValue()))) {
            throw new IllegalStateException("This is user does not exist!");
        }

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

        DirectSale newDirectSale =
                _directSaleFactory.createDirectSale(itemsId, sellerId, price, timeLimit);

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

    @Transactional(readOnly = true)
    public List<DirectSale> getAllDirectSales() {

        List<DirectSale> result = new ArrayList<>();

        _iDirectSaleRepo.findAll().forEach(result::add);

        return result;
    }

    @Transactional(readOnly = true)
    public List<DirectSale> getAllActiveDirectSales() {

        List<DirectSale> result = new ArrayList<>();
        Iterable<DirectSale> directSales = _iDirectSaleRepo.findAll();

        for (DirectSale directSale : directSales) {

            if (directSale.getDSStatus() == DirectSaleStatus.ACTIVE) {

                result.add(directSale);

            }

        }

        return result;

    }

    @Transactional(readOnly = true)
    public DirectSale getDirectSaleById(DirectSaleId directSaleId) {

        return _iDirectSaleRepo.ofIdentity(directSaleId)
                .orElseThrow(() -> new NoSuchElementException("DirectSale not found"));
    }

    //-----------------------
    // Filtered Direct Sales
    //-----------------------

    @Transactional(readOnly = true)
    public List<DirectSaleId> getDirectSaleItemsByGenreAsc(GenreId genreId) {

        if (!_iGenreRepo.containsOfIdentity(genreId)) {
            throw new IllegalArgumentException("Genre not found!");
        }

        List<ItemId> filteredItems = _iItemRepo.findByGenreId(genreId);

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

    @Transactional
    public void deleteDirectSale(DirectSaleId directSaleId) {

        _iDirectSaleRepo.deleteDirectSale(directSaleId);
    }

    @Transactional
    public DirectSale markDirectSaleAsCompleted(DirectSaleId directSaleId) {
        DirectSale directSale = getDirectSaleById(directSaleId);
        directSale.markAsCompleted();
        return _iDirectSaleRepo.save(directSale);
    }

}
