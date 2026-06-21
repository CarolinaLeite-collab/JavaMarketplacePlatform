package MITELOVERS.applicationservices;

import MITELOVERS.domain.repository.ISaleRepo;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.sale.SaleLineFactory;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SaleService {

    private final ISaleRepo _saleRepo;

    public SaleService(ISaleRepo saleRepo,
                       DirectSaleService directSaleService,
                       SaleLineFactory saleLineFactory) {
        this._saleRepo = saleRepo;
    }

    @Transactional
    public List<Sale> findUserSales(User user) {

        List<Sale> userSales = new ArrayList<>();
        UserId userId = user.identity();

        return _saleRepo.findByUserId(userId);

    }

    @Transactional
    public Sale findSaleById(SaleId saleId) {

        return _saleRepo.ofIdentity(saleId)
                .orElseThrow(() -> new NoSuchElementException("Sale does not exist!"));


    }

    @Transactional
    public SaleLine getSaleLineById(SaleId saleId, SaleLineId saleLineId) {

        Sale sale = findSaleById(saleId);

        return sale.get_saleLines().stream()
                .filter(saleLine -> saleLine.identity().equals(saleLineId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("SaleLine not found: " + saleLineId));

    }
}
