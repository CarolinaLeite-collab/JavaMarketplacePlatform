package TOPSECRET.controller;

import TOPSECRET.domain.repository.IUserRepo;
import TOPSECRET.domain.valueobject.*;
import TOPSECRET.domain.user.User;

/**
 * Controller responsible for handling the registration of a {@link User}.
 * Delegates creation/persistence to {@link IUserRepo}.
 */
public class RegisterNewUserController {

    private final IUserRepo _iUserRepo;

    public RegisterNewUserController(IUserRepo userRepo, UserId adminId) {
        _iUserRepo = userRepo;
    }

    public User registerNewUser(User admin, Name name, Address address, Email email, Phone phone) {
        if (!admin.hasRole(Role.ADMIN)) {
            throw new SecurityException("User is not authorized to register users");
        }
        return _iUserRepo.addUser(name, address, email, phone);
    }
}