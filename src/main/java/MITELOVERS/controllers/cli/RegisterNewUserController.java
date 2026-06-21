package MITELOVERS.controllers.cli;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.Address;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.Phone;
import org.springframework.stereotype.Controller;
import MITELOVERS.domain.repository.IShoppingCartRepo;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Controller responsible for handling the registration of a {@link User}.
 * Delegates creation/persistence to {@link IUserRepo}.
 */

@Controller
public class RegisterNewUserController {

    private final IUserRepo _iUserRepo;
    private final UserFactory _userFactory;
    private final IShoppingCartRepo _iShoppingCartRepo;
    private final ShoppingCartFactory _shoppingCartFactory;

    public RegisterNewUserController(IUserRepo userRepo, UserFactory userFactory, IShoppingCartRepo iShoppingCartRepo, ShoppingCartFactory shoppingCartFactory) {
        _iUserRepo = userRepo;
        _userFactory = userFactory;
        _iShoppingCartRepo = iShoppingCartRepo;
        _shoppingCartFactory = shoppingCartFactory;
    }

    @Transactional
    public User registerNewUser(Name name, Address address, Email email, Phone phone) {

        User newUser = _userFactory.createUser(name, address, email, phone);

        if (_iUserRepo.containsOfIdentity(newUser.identity())) {
            throw new IllegalStateException("User already exists");
        }

        User createdUser = _iUserRepo.save(newUser);

        ShoppingCart cart =
                _shoppingCartFactory.createShoppingCart(createdUser.identity());

        _iShoppingCartRepo.save(cart);

        return createdUser;    }

}
