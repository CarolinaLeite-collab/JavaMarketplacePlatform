package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Represents the possible sale statuses of an Item within the system.
 * <p>
 * This enumeration defines the lifecycle states that an Item can have
 * regarding its availability and selling process.
 * </p>
 *
 * <ul>
 *   <li><b>NotOnSale</b> – The item is not currently available for sale.</li>
 *   <li><b>OnAuction</b> – The item is being sold through an auction process.</li>
 *   <li><b>OnDirectSale</b> – The item is available for immediate purchase at a fixed price.</li>
 *   <li><b>Sold</b> – The item has already been sold and is no longer available.</li>
 * </ul>
 */

public enum SaleStatus implements ValueObject {

    NotOnSale,
    OnAuction,
    OnDirectSale,
    Sold
}
