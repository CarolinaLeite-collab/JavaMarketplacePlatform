package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;
import MITELOVERS.domain.sale.Sale;

import java.util.UUID;
import java.util.Objects;

/**
 * Value object representing the unique identifier of a {@link Sale}.
 * <p>
 * Identifiers follow the format {@code SA-XXXXXXXX}, where {@code X} is an
 * uppercase letter or digit.
 * </p>
 */

public class SaleId implements DomainId {

    private final String _id;
    private static final String ID_PATTERN = "SA-[A-Z0-9]{8}";

    public SaleId() {

        _id = "SA-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

    }

    public SaleId(String saleId) {

        Objects.requireNonNull(saleId, "SaleId cannot be null");

        if (!saleId.matches(ID_PATTERN)) {
            throw new IllegalArgumentException("Invalid SaleId format");
        }

        _id = saleId;
    }

    @Override
    public String toString() {
        return _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleId saleId)) return false;
        return Objects.equals(_id, saleId._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }
}
