package MITELOVERS.domain.valueobject;

/**
 * Enumeration representing the possible states of a sale.
 *
 * <ul>
 *   <li>{@code PENDING} - The sale has been created but not yet completed.</li>
 *   <li>{@code COMPLETED} - The sale has been successfully finalized.</li>
 *   <li>{@code CANCELLED} - The sale has been cancelled and will not be processed.</li>
 * </ul>
 */

public enum SaleSaleStatus {

    PENDING,
    COMPLETED,
    CANCELLED

}
