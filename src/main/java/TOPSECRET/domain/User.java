package TOPSECRET.domain;

import java.util.Objects;

/**
 *A registered entity (may be a person, company, or even an AI agent)
 *  on the platform who may play one or more roles.
 */

public class User {

    private final Name _name;
    private final Address _address;
    private final Email _email;
    private final Phone _phone;

    public User (Name name, Address address, Email email, Phone phone) {

        _name = Objects.requireNonNull(name, "name is required");
        _address = Objects.requireNonNull(address, "address is required");
        _email = Objects.requireNonNull(email, "email is required");
        _phone = Objects.requireNonNull(phone, "phoneNumber is required");

    }

        public Name getName(){
            return _name;
        }

        public Address getAddress(){
            return _address;
        }

        public Email getEmail(){
            return _email;
        }

        public Phone getPhone() {
            return _phone;
        }
}
