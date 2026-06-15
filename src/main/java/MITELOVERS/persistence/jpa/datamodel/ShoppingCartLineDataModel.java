package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data model object representing {@link ShoppingCartLine} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ShoppingCartLines")

public class ShoppingCartLineDataModel {

    @Id
    @Column(name = "shopping_cart_line_id", nullable = false)
    private String shoppingCartLineId;

    @Column(name = "direct_sale_id", nullable = false)
    private String directSaleId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numericValue", column = @Column(name = "price_at_addition_value", nullable = true)),
            @AttributeOverride(name = "currency", column = @Column(name = "price_at_addition_currency", nullable = true))
    })
    private PriceDataModel priceAtAddition;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;


}
