package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;
import MITELOVERS.domain.sale.SaleLine;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing the unique identifier of a {@link SaleLine}.
 * <p>
 * Identifiers follow the format {@code SL-XXXXXXXX}, where {@code X} is an
 * uppercase letter or digit.
 * </p>
 */

public class SaleLineId implements DomainId {

    private final String _id;
    private static final String ID_PATTERN = "SL-[A-Z0-9]{8}";

    public SaleLineId() {

        _id = "SL-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

    public SaleLineId(String saleLineId) {

        Objects.requireNonNull(saleLineId, "SaleLineId cannot be null");

        if (!saleLineId.matches(ID_PATTERN)) {
            throw new IllegalArgumentException("Invalid SaleLineId format");
        }

        _id = saleLineId;
    }

    @Override
    public String toString() {
        return _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleLineId that)) return false;
        return Objects.equals(_id, that._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }
}