package TOPSECRET.controller;

import TOPSECRET.domain.IUserRepo;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;

/**
 * Controller responsible for handling the registration of a {@link User}.
 * Delegates creation/persistence to {@link IUserRepo}.
 */
public class RegisterNewUserController {

    private final IUserRepo _iUserRepo;

    public RegisterNewUserController(IUserRepo userRepo) {
        _iUserRepo = userRepo;
    }

    public User registerNewUser(User user, String name, String email) {
        if (!user.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register users");
        }
        return _iUserRepo.registerNewUser(name, email);
    }

}