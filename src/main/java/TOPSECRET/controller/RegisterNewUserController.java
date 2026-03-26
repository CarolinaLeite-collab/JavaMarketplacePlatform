package TOPSECRET.controller;

import TOPSECRET.domain.*;

/**
 * Controller responsible for handling the registration of a {@link User}.
 * Delegates creation/persistence to {@link IUserRepo}.
 */
public class RegisterNewUserController {

    private final IUserRepo _iUserRepo;

    public RegisterNewUserController(IUserRepo userRepo, User admin) {
        _iUserRepo = userRepo;
    }

    public User registerNewUser(String name, String email) {

        return _iUserRepo.registerNewUser(name, email);
    }

}