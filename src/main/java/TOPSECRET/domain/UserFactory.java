package TOPSECRET.domain;

public class UserFactory {

    public User createUser(Name name, Email email) {
        return new User(name, email);
    }
}