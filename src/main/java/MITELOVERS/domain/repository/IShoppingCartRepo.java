package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.Optional;

/**
 * Repository interface for managing persistence and retrieval of {@link ShoppingCart} aggregates.
 */

public interface IShoppingCartRepo extends IRepository<ShoppingCartId, ShoppingCart> {

    Optional<ShoppingCart> findShoppingCartByUserId(UserId userId);

}
