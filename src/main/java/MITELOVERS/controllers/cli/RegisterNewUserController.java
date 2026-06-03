package MITELOVERS.controllers.cli;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.Address;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.Phone;
import org.springframework.stereotype.Controller;

/**
 * Controller responsible for handling the registration of a {@link User}.
 * Delegates creation/persistence to {@link IUserRepo}.
 */

@Controller
public class RegisterNewUserController {

    private final IUserRepo _iUserRepo;
    private final UserFactory _userFactory;

    public RegisterNewUserController(IUserRepo userRepo, UserFactory userFactory) {
        _iUserRepo = userRepo;
        _userFactory = userFactory;
    }

    public User registerNewUser(Name name, Address address, Email email, Phone phone) {

        User newUser = _userFactory.createUser(name, address, email, phone);

        if (_iUserRepo.containsOfIdentity(newUser.identity())) {
            throw new IllegalStateException("User already exists");
        }

        return _iUserRepo.save(newUser);
    }

}
