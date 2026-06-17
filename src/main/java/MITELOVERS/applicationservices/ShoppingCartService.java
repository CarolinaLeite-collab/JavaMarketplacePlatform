package MITELOVERS.applicationservices;

import MITELOVERS.domain.repository.IShoppingCartRepo;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.ShoppingCartLineId;
import MITELOVERS.domain.valueobject.UserId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ShoppingCartService {

    private final IShoppingCartRepo _shoppingCartRepo;

    @Transactional
    public ShoppingCart findCartByCartId(String cartId) {

        ShoppingCartId shoppingCartId = new ShoppingCartId(cartId);

        return _shoppingCartRepo.ofIdentity(shoppingCartId)
                .orElseThrow(() -> new NoSuchElementException("ShoppingCart not found!"));

    }

    @Transactional
    public ShoppingCart findCartByUserId(String email) {

        UserId userId = new UserId(new Email(email));

        return _shoppingCartRepo.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("ShoppingCart not found for user: " + userId));

    }

    @Transactional
    public ShoppingCartLine findCartLineByUserId(String cartId, String cartLineId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);
        ShoppingCartLineId shoppingCartLineId = new ShoppingCartLineId(cartLineId);

        return shoppingCart.getCartLines().stream()
                .filter(shoppingCartLine -> shoppingCartLine.identity().equals(shoppingCartLineId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("ShoppingCartLine not found: " + cartLineId));


    }



}
