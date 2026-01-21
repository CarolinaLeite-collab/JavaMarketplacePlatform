package TOPSECRET.controller;

import TOPSECRET.domain.User;
import TOPSECRET.domain.UserRepo;

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