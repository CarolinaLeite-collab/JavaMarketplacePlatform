package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.DirectSaleRequestDTO;
import MITELOVERS.dto.DirectSaleResponseDTO;
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
    private final DirectSaleFactory _directSaleFactory;
    private final DirectSaleResponseDTOMapper _responseMapper;


    public DirectSaleService(ILibraryRepo iLibraryRepo,
                             IDirectSaleRepo iDirectSaleRepo,
                             IItemRepo iItemRepo,
                             DirectSaleFactory directSaleFactory,
                             DirectSaleResponseDTOMapper responseMapper) {

        _iLibraryRepo = Objects.requireNonNull(iLibraryRepo);
        _iDirectSaleRepo = Objects.requireNonNull(iDirectSaleRepo);
        _iItemRepo = Objects.requireNonNull(iItemRepo);
        _directSaleFactory = Objects.requireNonNull(directSaleFactory);
        _responseMapper = Objects.requireNonNull(responseMapper);
    }

    public DirectSaleResponseDTO createDirectSale(DirectSaleRequestDTO request) {

        List<ItemId> itemIds = request.getItemIds().stream()
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
                _directSaleFactory.createDirectSale(itemIds, price, timeLimit);

        if (_iDirectSaleRepo.containsOfIdentity(newDirectSale.identity())) {
            throw new IllegalStateException("DirectSale already exists");
        }

        for (ItemId itemId : itemIds) {
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

}
