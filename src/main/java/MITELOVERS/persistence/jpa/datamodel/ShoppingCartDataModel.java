package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.valueobject.Price;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data model object representing {@link ShoppingCart} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ShoppingCarts")

public class ShoppingCartDataModel {

    @Id
    @Column(name = "shopping_cart_id", nullable = false)
    private String shoppingCartId;

    @Column(name = "buyer_id", nullable = false)
    private String buyerId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numericValue", column = @Column(name = "total_amount_value", nullable = true)),
            @AttributeOverride(name = "currency", column = @Column(name = "total_amount_currency", nullable = true))
    })
    private PriceDataModel totalAmount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shopping_cart_id")
    private List<ShoppingCartLineDataModel> shoppingCartLines;

}
