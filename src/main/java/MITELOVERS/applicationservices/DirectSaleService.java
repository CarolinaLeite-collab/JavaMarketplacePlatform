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
    private final DirectSaleResponseDTOMapper _responseMapper;
    private final DSFilteredItemsResponseMapper _filteredResponseMapper;


    public DirectSaleService(IGenreRepo iGenreRepo,
                             IDirectSaleRepo iDirectSaleRepo,
                             IItemRepo iItemRepo,
                             DirectSaleFactory directSaleFactory,
                             DirectSaleResponseDTOMapper responseMapper,
                             DSFilteredItemsResponseMapper filteredResponseMapper) {

        _iGenreRepo = Objects.requireNonNull(iGenreRepo);
        _iDirectSaleRepo = Objects.requireNonNull(iDirectSaleRepo);
        _iItemRepo = Objects.requireNonNull(iItemRepo);
        _directSaleFactory = directSaleFactory;
        _responseMapper = responseMapper;
        _filteredResponseMapper = filteredResponseMapper;
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

    public DSFilteredItemsResponseDTO getDirectSaleItemsByGenreAsc(String genreId) {

        GenreId gid = new GenreId(genreId);

        if (!_iGenreRepo.containsOfIdentity(gid)) {
            throw new IllegalArgumentException("Genre not found: " + genreId);
        }

        List<ItemId> filtered = _iItemRepo.findByGenreId(gid);

        if (filtered.isEmpty()) {
            return new DSFilteredItemsResponseDTO(List.of());
        }

        List<String> directSaleIds =
                _iDirectSaleRepo.findByItemsIdSortedByPublicationDateAsc(filtered)
                .stream()
                .map(ds -> ds.identity().toString()) // Map to DirectSaleId
                .distinct()
                .toList();

        return _filteredResponseMapper.toDTO(directSaleIds);
    }

}
