package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link User} entities.
 * <p>
 * This class provides operations to check existence by {@link Email} and create new users
 * with associated {@link Name} and {@link Email}. Ensures uniqueness by email
 * before creation, throwing {@link IllegalStateException} if a duplicate is detected.
 * </p>
 */

public class UserRepo {

    private List<User> _users;

    public UserRepo() {
        _users = new ArrayList<>();
    }

    //Register user to repo
    public User registerNewUser(String name, String email) {

        //verify if user is in repo
        if (UserExists(email)) {

            throw new IllegalStateException("User already exists");
        }
        //Register new name and email
        Email newUserEmail = new Email(email);
        Name newUserName = new Name(name);

        //Register New User
        User newUser = new User(newUserName, newUserEmail);

        //add user to userRepo
        _users.add(newUser);

        return newUser;
    }

    //verify if user is in repo, by checking email uniqueness
    private boolean UserExists(String email) {
        String emailFormat = email.trim().toLowerCase();

        for (User u1 : _users) {
            if (emailFormat.equals(u1.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
