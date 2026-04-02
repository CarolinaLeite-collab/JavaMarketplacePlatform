package TOPSECRET.controller;

import TOPSECRET.domain.IUserRepo;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.valueobject.Email;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.UserID;

/**
 * Controller responsible for handling the registration of a {@link User}.
 * Delegates creation/persistence to {@link IUserRepo}.
 */
// RegisterNewUserController.java
public class RegisterNewUserController {

    private final IUserRepo _iUserRepo;
    private final UserFactory _userFactory;

    public RegisterNewUserController(IUserRepo userRepo, UserFactory userFactory) {
        _iUserRepo = userRepo;
        _userFactory = userFactory;
    }

    public User registerNewUser(User admin, String name, String email) {
        if (!admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register users");
        }

        Email newEmail = new Email(email);
        UserID userId = new UserID(newEmail);

        if (_iUserRepo.containsOfIdentity(userId)) {
            throw new IllegalStateException("User already exists");
        }

        Name newName = new Name(name);
        User newUser = _userFactory.createUser(newName, newEmail);
        return _iUserRepo.save(newUser);
    }
}