package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;
import MITELOVERS.domain.directsale.DirectSale;

import java.util.Objects;
import java.util.UUID;


/**
 * Represents the unique identifier of an {@link DirectSale}.
 * <p>
 * The identifier is automatically generated when each object is created
 * attributing it a random code. The format is:
 * <pre>
 * "DS" + "-" + Random8CharCode
 * </pre>
 * Example: "DS-4F3K7A1B"
 *
 */


public final class DirectSaleId implements DomainId {

    private String _dsId;

    public DirectSaleId() {

        _dsId = "DS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DirectSaleId that)) return false;
        return Objects.equals(_dsId, that._dsId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_dsId);
    }
}
