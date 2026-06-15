package MITELOVERS.domain.sale;

import MITELOVERS.ddd.DomainEntity;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SaleLine implements DomainEntity<SaleLineId> {

    private final SaleLineId _saleLineId;
    private final UserId _sellerId;
    private final Price _priceAtSale;
    private final DirectSaleId _directSaleId;

    //Re-hydration
    public SaleLine(SaleLineId saleLineId,
                    UserId sellerId,
                    Price priceAtSale,
                    DirectSaleId directSaleId) {

        _saleLineId = Objects.requireNonNull(saleLineId, "SaleLineId cannot be null!");
        _sellerId = Objects.requireNonNull(sellerId, "SellerId cannot be null!");
        _priceAtSale = Objects.requireNonNull(priceAtSale, "PriceAtSale cannot be null!");
        _directSaleId = Objects.requireNonNull(directSaleId, "DirectSaleId cannot be null!");
    }

    // creation
    public SaleLine(UserId sellerId,
                    Price priceAtSale,
                    DirectSaleId directSaleId) {

        this(new SaleLineId(), sellerId, priceAtSale, directSaleId);
    }

    @Override
    public SaleLineId identity() {
        return _saleLineId;
    }

    //compares business attributes, not the id
    @Override
    public boolean sameAs(Object object) {

        if (object instanceof SaleLine saleLine) {
            return _sellerId.equals(saleLine._sellerId)
                    && _priceAtSale.equals(saleLine._priceAtSale)
                    && _directSaleId.equals(saleLine._directSaleId);
        }

        return false;
    }

    //compares identity only
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o instanceof SaleLine) {
            SaleLine oSaleLine = (SaleLine) o;
            if (_saleLineId.equals(oSaleLine._saleLineId))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return _saleLineId.hashCode();
    }

    @Override
    public String toString() {
        return "\nSale Line Id: " + _saleLineId +
                "\nDirect Sale Id: " + _directSaleId +
                "\nSeller Id: " + _sellerId +
                "\nPrice At Sale: " + _priceAtSale;
    }
}

