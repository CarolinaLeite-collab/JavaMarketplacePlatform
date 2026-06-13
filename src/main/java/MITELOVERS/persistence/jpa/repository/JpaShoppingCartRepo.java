package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.repository.IShoppingCartRepo;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.assembler.ShoppingCartAssembler;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartDataModel;
import MITELOVERS.persistence.springdata.IShoppingCartSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaShoppingCartRepo implements IShoppingCartRepo {

    @Autowired
    private IShoppingCartSpringDataRepo _shoppingCartSpringDataRepo;

    @Autowired
    private ShoppingCartAssembler _shoppingCartAssembler;


    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {

        ShoppingCartDataModel dmToSave = _shoppingCartAssembler.toDataModel(shoppingCart);

        ShoppingCartDataModel savedDm = _shoppingCartSpringDataRepo.save(dmToSave);

        return _shoppingCartAssembler.toDomain(savedDm);

    }

    @Override
    public Iterable<ShoppingCartId> findAllKeys() {

        Iterable<ShoppingCartDataModel> shoppingCartDataModels = _shoppingCartSpringDataRepo.findAll();

        List<ShoppingCartId> shoppingCartIdList = new ArrayList<>();

        for (ShoppingCartDataModel shoppingCartDataModel : shoppingCartDataModels) {

            shoppingCartIdList.add(new ShoppingCartId(shoppingCartDataModel.getShoppingCartId()));

        }

        return shoppingCartIdList;

    }

    @Override
    public Iterable<ShoppingCart> findAll() {

        Iterable<ShoppingCartDataModel> shoppingCartDataModels = _shoppingCartSpringDataRepo.findAll();

        List<ShoppingCart> shoppingCartList = new ArrayList<>();

        for (ShoppingCartDataModel shoppingCartDataModel : shoppingCartDataModels) {

            shoppingCartList.add(_shoppingCartAssembler.toDomain(shoppingCartDataModel));

        }

        return shoppingCartList;

    }


    @Override
    public Optional<ShoppingCart> ofIdentity(ShoppingCartId shoppingCartId) {

        return _shoppingCartSpringDataRepo.findById(shoppingCartId.toString())
                .map(_shoppingCartAssembler::toDomain);

    }

    @Override
    public boolean containsOfIdentity(ShoppingCartId shoppingCartId) {

        return _shoppingCartSpringDataRepo.existsById(shoppingCartId.toString());

    }

    @Override
    public Optional<ShoppingCart> findShoppingCartByUserId(UserId userId) {

        return _shoppingCartSpringDataRepo.findByBuyerId(userId.toString())
                .map(_shoppingCartAssembler::toDomain);

    }

}
