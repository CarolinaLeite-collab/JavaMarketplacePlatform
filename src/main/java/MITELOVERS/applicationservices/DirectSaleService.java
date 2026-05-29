package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
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

    private final IDirectSaleRepo _directSaleRepo;
    private final DirectSaleFactory _directSaleFactory;
    private final DirectSaleResponseDTOMapper _responseMapper;

    public DirectSaleService(IDirectSaleRepo directSaleRepo,
                             DirectSaleFactory directSaleFactory,
                             DirectSaleResponseDTOMapper responseMapper) {

        _directSaleRepo = Objects.requireNonNull(directSaleRepo);
        _directSaleFactory = Objects.requireNonNull(directSaleFactory);
        _responseMapper = Objects.requireNonNull(responseMapper);
    }

    public DirectSaleResponseDTO createDirectSale(DirectSaleRequestDTO request) {

        List<ItemId> itemsId = request.get_itemsId().stream()
                .map(ItemId::new)
                .toList();

        Price price = new Price(
                request.get_priceValue(),
                Currency.valueOf(request.get_priceCurrency())
        );

        Duration timeLimit = request.get_timeLimitSeconds() != null
                ? Duration.ofSeconds(request.get_timeLimitSeconds())
                : null;

        DirectSale newDirectSale =
                _directSaleFactory.createDirectSale(itemsId, price, timeLimit);

        if (_directSaleRepo.containsOfIdentity(newDirectSale.identity())) {
            throw new IllegalStateException("DirectSale already exists");
        }

        DirectSale saved = _directSaleRepo.save(newDirectSale);

        return _responseMapper.toResponseDTO(saved);
    }

    public List<DirectSaleResponseDTO> getAllDirectSales() {

        Iterable<DirectSale> directSales = _directSaleRepo.findAll();

        List<DirectSaleResponseDTO> response = new ArrayList<>();

        for (DirectSale ds : directSales) {
            response.add(_responseMapper.toResponseDTO(ds));
        }

        return response;
    }

    public DirectSaleResponseDTO getDirectSaleById(String id) {

        DirectSaleId directSaleId = new DirectSaleId(id);

        DirectSale directSale = _directSaleRepo.ofIdentity(directSaleId)
                .orElseThrow(() -> new NoSuchElementException("DirectSale not found"));

        return _responseMapper.toResponseDTO(directSale);
    }

}
