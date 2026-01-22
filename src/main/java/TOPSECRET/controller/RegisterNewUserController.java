package TOPSECRET.controller;

import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.PublisherRepo;
import TOPSECRET.domain.User;
import TOPSECRET.domain.UserRepo;

/**
 * Controller responsible for handling the registration of a {@link User}.
 * Delegates creation/persistence to {@link UserRepo}.
 */

public class RegisterNewUserController {

    private final UserRepo _userRepo;


    public RegisterNewUserController(UserRepo userRepo, User admin){

        _userRepo=userRepo;

    }

    public User RegisterNewUser(String name, String email){

        User newUser= _userRepo.registerNewUser(name, email);

        return newUser;
    }
}