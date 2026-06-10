package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SaleExpirationService {

    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;

    public SaleExpirationService(IDirectSaleRepo iDirectSaleRepo,
                                 IItemRepo iItemRepo) {

        _iDirectSaleRepo = iDirectSaleRepo;
        _iItemRepo = iItemRepo;
    }

    @Transactional
    public void expireAllExpiredSales() {
        expireDirectSales();
    }

    private void expireDirectSales() {

        List<DirectSaleId> expired = _iDirectSaleRepo.findExpired();

        for (DirectSaleId directSaleId : expired) {

            DirectSale sale = _iDirectSaleRepo.ofIdentity(directSaleId)
                    .orElseThrow(() -> new IllegalStateException("Expired DirectSale not found: " + directSaleId));

            releaseItems(sale.getItemsId());
            sale.markAsExpired();

            _iDirectSaleRepo.save(sale);
        }
    }

    private void releaseItems(List<ItemId> items) {

        for (ItemId id : items) {

            Item item = _iItemRepo.ofIdentity(id)
                    .orElseThrow(() -> new IllegalStateException("Item not found: " + id));

            item.markAsNotOnSale();

            _iItemRepo.save(item);
        }
    }

}
