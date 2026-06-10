package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;
import MITELOVERS.domain.directsale.DirectSale;

/**
 * Represents the lifecycle status of a {@link DirectSale}.
 *
 * <p>
 * A direct sale transitions through the following states:
 * </p>
 *
 * <ul>
 *   <li>{@link #ACTIVE} — the sale is open and available for purchase.</li>
 *   <li>{@link #COMPLETED} — the sale was successfully concluded with a buyer.</li>
 *   <li>{@link #CANCELLED} — the sale was manually cancelled by the seller.</li>
 *   <li>{@link #EXPIRED} — the sale's time limit elapsed without a purchase.</li>
 * </ul>
 */

public enum DirectSaleStatus implements ValueObject {

    ACTIVE,
    COMPLETED,
    CANCELLED,
    EXPIRED

}
