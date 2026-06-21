package MITELOVERS.domain.sale;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a completed or pending purchase made by a buyer.
 * <p>
 * A sale aggregates one or more {@link SaleLine}s, records the buyer,
 * tracks the total amount of the transaction, and maintains its lifecycle
 * status from creation until completion or cancellation.
 * </p>
 * <p>
 * Enforces that a sale contains at least one sale line, that all monetary
 * values are valid, and that the total amount corresponds to the sum of
 * the sale lines.
 * </p>
 */

@Getter
public class Sale implements AggregateRoot<SaleId> {

    private final SaleId _saleId;
    private final UserId _buyerId;
    private final List<SaleLine> _saleLines;

    private Price _totalAmount;
    private SaleSaleStatus _saleSaleStatus;

    private final LocalDateTime _createdAt;
    private LocalDateTime _completedAt;

    // creation
    Sale(UserId buyerId,
         List<SaleLine> saleLines) {
        this(
                new SaleId(),
                buyerId,
                saleLines,
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );
    }

    //Rehydration
    Sale(SaleId saleId,
         UserId buyerId,
         List<SaleLine> saleLines,
         LocalDateTime createdAt,
         LocalDateTime completedAt,
         SaleSaleStatus saleSaleStatus) {

        _saleId = Objects.requireNonNull(saleId, "saleId cannot be null!");
        _buyerId = Objects.requireNonNull(buyerId, "buyerId cannot be null!");
        _saleLines = validateSaleLines(saleLines);
        _totalAmount = calculateTotalAmount();
        _createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null!");
        _completedAt = completedAt;
        _saleSaleStatus = Objects.requireNonNull(saleSaleStatus, "saleStatus cannot be null!");
    }

    private Price calculateTotalAmount() {

        //Checks actual currency
        Currency currency = _saleLines.getFirst()
                .get_priceAtSale()
                .getCurrency();

        double total = 0;

        //sums line values + checks all currencies are same
        for (SaleLine saleLine : _saleLines) {

            Price price = saleLine.get_priceAtSale();

            if (!currency.equals(price.getCurrency())) {
                throw new IllegalStateException(
                        "All SaleLines must have the same currency");
            }

            total += price.getValue();
        }

        return new Price(total,currency);
    }

    private List<SaleLine> validateSaleLines(List<SaleLine> saleLines) {

        Objects.requireNonNull(saleLines, "saleLines cannot be null!");

        if (saleLines.isEmpty()) {
            throw new IllegalArgumentException(
                    "saleLines cannot be empty!");
        }

        return new ArrayList<>(saleLines);
    }

    public void markSaleAsCompleted() {
        if (_saleSaleStatus == SaleSaleStatus.CANCELLED) {

            throw new IllegalStateException("Cannot complete a cancelled sale!");
        }

        if (_saleSaleStatus == SaleSaleStatus.COMPLETED) {
            throw new IllegalStateException("Sale is already completed!");
        }

        _saleSaleStatus = SaleSaleStatus.COMPLETED;
        _completedAt = LocalDateTime.now();
    }

    public void markSaleAsCancelled() {
        if (_saleSaleStatus == SaleSaleStatus.COMPLETED) {

            throw new IllegalStateException("Cannot cancel a completed sale!");
        }

        if (_saleSaleStatus == SaleSaleStatus.CANCELLED) {
            throw new IllegalStateException("Sale is already cancelled!");
        }

        _saleSaleStatus = SaleSaleStatus.CANCELLED;
    }

    @Override
    public SaleId identity() {
        return _saleId;
    }

    //compares business attributes, not the id
    @Override
    public boolean sameAs(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Sale sale)) {
            return false;
        }

        return _buyerId.equals(sale._buyerId)
                && _saleLines.equals(sale._saleLines)
                && _createdAt.equals(sale._createdAt)
                && Objects.equals(_completedAt, sale._completedAt)
                && _saleSaleStatus.equals(sale._saleSaleStatus);
    }

    //compares identity only
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o instanceof Sale) {
            Sale oSale = (Sale) o;
            if (_saleId.equals(oSale._saleId))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return _saleId.hashCode();
    }

    @Override
    public String toString() {
        return "\nSale Id: " + _saleId +
                "\nBuyer Id: " + _buyerId +
                "\nSale Status: " + _saleSaleStatus +
                "\nTotal Amount: " + _totalAmount +
                "\nCreated At: " + _createdAt +
                "\nCompleted At: " + _completedAt +
                "\nSale Lines: " + _saleLines;
    }

}