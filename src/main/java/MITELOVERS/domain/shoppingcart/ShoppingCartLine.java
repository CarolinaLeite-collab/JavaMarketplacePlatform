package MITELOVERS.domain.shoppingcart;

import MITELOVERS.ddd.DomainEntity;
import MITELOVERS.domain.valueobject.ShoppingCartLineId;

public class ShoppingCartLine implements DomainEntity<ShoppingCartLineId> {

    @Override
    public ShoppingCartLineId identity() {
        return null;
    }

    @Override
    public boolean sameAs(Object object) {
        return false;
    }
}
